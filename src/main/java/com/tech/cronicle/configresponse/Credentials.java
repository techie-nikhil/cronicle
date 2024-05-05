
package com.tech.cronicle.configresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Credentials {

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Credentials [username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}

}
