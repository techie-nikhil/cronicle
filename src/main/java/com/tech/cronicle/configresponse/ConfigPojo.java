package com.tech.cronicle.configresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ConfigDeserializer.class)
public class ConfigPojo {

	private String env;

	private Environment environment;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConfigPojo [env=");
		builder.append(env);
		builder.append(", environment=");
		builder.append(environment);
		builder.append("]");
		return builder.toString();
	}

}
