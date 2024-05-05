package com.tech.cronicle.requests;

import lombok.Data;

@Data
public class TestAPIRequest {

	private String name;
	private String value;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestAPIRequest [name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

}
