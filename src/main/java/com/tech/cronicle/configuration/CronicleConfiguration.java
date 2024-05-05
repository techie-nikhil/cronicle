package com.tech.cronicle.configuration;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import com.tech.cronicle.configresponse.ConfigPojo;
import com.tech.cronicle.configresponse.Environment;
import com.tech.cronicle.configresponse.Sequelize;
import com.tech.cronicle.constants.JobConstants;
import com.tech.cronicle.util.HttpStatusHandler;
import com.tech.cronicle.util.LoggerWrapper;
import com.zaxxer.hikari.HikariDataSource;

@Primary
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.tech.cronicle.dao", entityManagerFactoryRef = "masterEntityManagerFactory", transactionManagerRef = "masterTransactionManager")
public class CronicleConfiguration implements WebMvcConfigurer {

	private final static LoggerWrapper LOGGER = LoggerWrapper.getLogger(CronicleConfiguration.class);

	@Value("${spring.profiles.active}")
	private String currentEnvironment;

	@Value("${config.key}")
	private String configKey;

	@Value("${config.url}")
	private String configUrl;

	@Value("${api.connection.timeout}")
	private String apiConnectionTimeout;

	@Value("${api.read.timeout}")
	private String apiReadTimeout;

	@Bean
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(Integer.parseInt(apiConnectionTimeout));
		requestFactory.setReadTimeout(Integer.parseInt(apiReadTimeout));
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.setErrorHandler(new HttpStatusHandler());
		return restTemplate;
	}

	private void shutdown() {
		LOGGER.error("Shutting down application");
		Runtime.getRuntime().halt(1);
	}

	@Bean
	public ConfigPojo pojoBean(RestTemplate restTemplate) {
		LOGGER.info("Environment set is " + currentEnvironment);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("config", configKey);
		String config = configUrl + "/get_config";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config).queryParam("env", currentEnvironment).queryParam("service",
				"cronicle");
		HttpEntity<ConfigPojo> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<ConfigPojo> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, ConfigPojo.class);
			if (response != null) {
				LOGGER.info("Response : " + response.getBody());
				return response.getBody();
			} else {
				LOGGER.error("No response from config service");
				throw new RuntimeException("No response from config service");
			}
		} catch (Exception e) {
			LOGGER.error("Error in reading from config service from " + builder.toUriString());
			LOGGER.error(String.valueOf(e));
			shutdown();
		}
		return null;
	}

	@Primary
	@Bean
	@QuartzDataSource
	public DataSource masterDataSource(ConfigPojo configPojo) {
		Environment currentEnv = configPojo.getEnvironment();
		Sequelize sqlConfig = currentEnv.getPlatformDb();

		String host = sqlConfig.getHost();
		Integer port = sqlConfig.getPort();
		String dbName = sqlConfig.getDatabase();
		String userName = sqlConfig.getUsername();
		String password = sqlConfig.getPassword();
		StringBuilder urlSb = new StringBuilder("jdbc:mysql://").append(host).append(":").append(port).append("/").append(dbName)
				.append("?zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true");

		String url = urlSb.toString();
		HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setJdbcUrl(url);
		hikariDataSource.setUsername(userName);
		hikariDataSource.setPassword(password);
		hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariDataSource.setMaximumPoolSize(sqlConfig.getMaxActiveConnection());
		hikariDataSource.setMinimumIdle(sqlConfig.getInitialPoolSize());
		hikariDataSource.setAutoCommit(true);
		hikariDataSource.setConnectionTestQuery("SELECT 'CRONICLE'");
		hikariDataSource.setValidationTimeout(JobConstants.DB_CONNECTION_TIMEOUT_MILLISECONDS);
		hikariDataSource.setConnectionTimeout(JobConstants.DB_CONNECTION_TIMEOUT_MILLISECONDS * 2);
		hikariDataSource.setIdleTimeout(MINUTES.toMillis(4));
		hikariDataSource.setMaxLifetime(MINUTES.toMillis(10));
		try {
			LOGGER.info("Testing db connection for " + url);
			Connection result = hikariDataSource.getConnection();
			if (result == null) {
				throw new SQLException("Db connection failed for " + url);
			} else {
				LOGGER.info("Db connection passed for " + url);
			}

		} catch (SQLException e) {
			LOGGER.info("Error occured in db connection validation" + url);
			LOGGER.info(String.valueOf(e));
			shutdown();
		}
		return hikariDataSource;
	}

	@Primary
	@Bean(name = "masterEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(DataSource masterDataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(masterDataSource);
		em.setPackagesToScan("com.tech.cronicle.model");
		em.setPersistenceUnitName("masterdb-persistence-unit");
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(hibernateProperties());
		return em;
	}

	@Bean(name = "masterTransactionManager")
	public JpaTransactionManager masterTransactionManager(@Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
		properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
		properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
		return properties;
	}
}
