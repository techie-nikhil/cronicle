package com.tech.cronicle.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.cronicle.constants.JobConstants;
import com.tech.cronicle.exception.RequestAssertionException;
import com.tech.cronicle.model.JobModel;

@Component
public class RestTemplateUtil {

	private final static LoggerWrapper LOG = LoggerWrapper.getLogger(RestTemplateUtil.class);

	@Autowired
	private RestTemplate restTemplate;

	public void performRequest(JobModel jobModel, HttpMethod httpMethod, RetryContext context) throws RequestAssertionException {
		ResponseEntity<String> responseEntity = null;
		HttpEntity<Object> httpEntity = this.prepareHttpEntity(jobModel);
		try {
			responseEntity = this.restTemplate.exchange(jobModel.getRequestUrl(), httpMethod, httpEntity, String.class);
			if (responseEntity == null) {
				LOG.error("Response for jobModelId " + jobModel.getId() + " is null");
			} else {
				LOG.info("Response for jobModelId " + jobModel.getId() + " is status: " + responseEntity.getStatusCode() + ", body: "
						+ responseEntity.getBody());
			}

		} catch (Exception ex) {
			LOG.error("Error Found for jobModelId " + +jobModel.getId(), ex.getMessage());
		}
		context.setAttribute("entity", responseEntity);
		if (responseEntity == null) {
			throw new RequestAssertionException(JobConstants.UNREACHABLE_SERVER_ISSUE);
		} else if (responseEntity.getStatusCode().is5xxServerError()) {
			throw new RequestAssertionException(JobConstants.SERVER_SIDE_ISSUE);
		} else if (responseEntity.getStatusCode().is4xxClientError()) {
			throw new RuntimeException(JobConstants.CLIENT_SIDE_ISSUE);
		}
	}

	public HttpEntity<Object> prepareHttpEntity(JobModel jobModel) {
		try {
			HttpHeaders headers = new HttpHeaders();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(jobModel.getHeaders().toString());
			Map<String, String> result = mapper.convertValue(jsonNode, new TypeReference<>() {
			});
			if (result != null) {
				for (String key : result.keySet()) {
					headers.add(key, result.get(key));
				}
			}
			HttpEntity<Object> httpEntity = new HttpEntity<>(jobModel.getBody(), headers);
			return httpEntity;
		} catch (JsonProcessingException ex) {
			LOG.error("Error in prepareHttpEntity for jobModelId " + jobModel.getId(), ex.getMessage());
		}
		return null;
	}
}
