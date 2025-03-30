package com.voxloud.provisioning.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.exception.VoxloudException;

import java.util.Arrays;
import java.util.Map;

public class ConferenceConfigFile extends ConfigFile {

    public ConferenceConfigFile(Map<String, Object> config) {
        super(config);
    }

    @Override
    public Map<Object, Object> parseOverridenConfig(String overridenConfig) {
        if(overridenConfig == null || overridenConfig.isBlank()) {
            return Map.of();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(overridenConfig, Map.class);
        } catch (Exception e) {
            throw new VoxloudException("Failed to parse the override fragment json string: " + e.getMessage());
        }
    }

    @Override
    public void addDefaultCodecs(String codecString) {
        getConfig().put(CODECS, Arrays.asList(codecString.split(",")));
    }

    @Override
    public String generateFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(super.getConfig());
        } catch (JsonProcessingException e) {
            throw new VoxloudException("Failed to generate Conference config file: " + e.getMessage());
        }
    }
}
