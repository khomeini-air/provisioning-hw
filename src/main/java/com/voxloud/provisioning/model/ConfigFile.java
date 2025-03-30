package com.voxloud.provisioning.model;

import java.util.Map;

/**
 * Abstract base class representing a configuration file for provisioning devices.
 */
public abstract class ConfigFile {
    // Constants representing common configuration keys
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DOMAIN = "domain";
    public static final String PORT = "port";
    public static final String CODECS = "codecs";

    // Configuration map storing key-value pairs of settings
    private Map<String, Object> config;

    public ConfigFile(Map<String, Object> config) {
        this.config = config;
    }

    /**
     * Parses an overridden configuration string and returns a map of key-value pairs.
     * The implementation of this method depends on the specific format of the configuration.
     *
     * @param overridenConfig The overridden configuration string.
     * @return A map containing parsed configuration key-value pairs.
     */
    public abstract Map<Object, Object> parseOverridenConfig(String overridenConfig);

    /**
     * Adds default codec settings to the configuration.
     * The implementation determines how codec settings are handled.
     *
     * @param codecString A comma-separated string of codecs.
     */
    public abstract void addDefaultCodecs(String codecString);

    /**
     * Generates the configuration file content in a specific format.
     * This method is implemented by subclasses to generate different file formats.
     *
     * @return A string representation of the generated configuration file.
     */
    public abstract String generateFile();

    /**
     * Retrieves the configuration map containing key-value pairs.
     *
     * @return The configuration map.
     */
    public Map<String, Object> getConfig() {
        return config;
    }
}
