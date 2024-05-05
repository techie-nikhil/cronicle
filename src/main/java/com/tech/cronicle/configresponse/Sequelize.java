
package com.tech.cronicle.configresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Sequelize {

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;

	@JsonProperty("database")
	private String database;

	@JsonProperty("host")
	private String host;

	@JsonProperty("port")
	private Integer port;

	@JsonProperty("maxActiveConnection")
	private Integer maxActiveConnection;

	@JsonProperty("initialPoolSize")
	private Integer initialPoolSize;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Sequelize [username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", database=");
		builder.append(database);
		builder.append(", host=");
		builder.append(host);
		builder.append(", port=");
		builder.append(port);
		builder.append(", maxActiveConnection=");
		builder.append(maxActiveConnection);
		builder.append(", initialPoolSize=");
		builder.append(initialPoolSize);
		builder.append("]");
		return builder.toString();
	}

}
