
package com.tech.cronicle.configresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Environment {
	@JsonProperty("platformDb")
	private Sequelize platformDb;

	@JsonProperty("credentials")
	private Credentials credentials;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Environment [platformDb=");
		builder.append(platformDb);
		builder.append(", credentials=");
		builder.append(credentials);
		builder.append("]");
		return builder.toString();
	}

}
