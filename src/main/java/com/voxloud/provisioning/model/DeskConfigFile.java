package com.voxloud.provisioning.model;

import com.voxloud.provisioning.exception.VoxloudException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class DeskConfigFile extends ConfigFile {

    public DeskConfigFile(Map<String, Object> config) {
        super(config);
    }

    @Override
    public Map<Object, Object> parseOverridenConfig(String overridenConfig) {
        if(overridenConfig == null || overridenConfig.isBlank()) {
            return Map.of();
        }

        Properties props = new Properties();
        try {
            props.load(new StringReader(overridenConfig));
            return props;
        } catch (IOException e) {
            throw new VoxloudException("Failed to parse the override fragment properties string: " + e.getMessage());
        }
    }

    @Override
    public void addDefaultCodecs(String codecString) {
        getConfig().put(CODECS, codecString);
    }

    @Override
    public String generateFile() {
        Map<String, Object> config = super.getConfig();

        // Generate the config following properties format. A new line is also added at the end.
        return String.format("%s\n",
                config.entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining("\n")));
    }
}
