package com.tech.cronicle.configresponse;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ConfigDeserializer extends StdDeserializer<ConfigPojo> {

	private static final long serialVersionUID = 1L;

	public ConfigDeserializer() {
		this(null);
	}

	protected ConfigDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public ConfigPojo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String env = (String) node.get("env").asText();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Environment local = mapper.treeToValue(node.get(env), Environment.class);
		ConfigPojo config = new ConfigPojo();
		config.setEnv(env);
		config.setEnvironment(local);
		return config;
	}

}
