package com.voxloud.provisioning.factory;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.VoxloudException;
import com.voxloud.provisioning.model.ConferenceConfigFile;
import com.voxloud.provisioning.model.ConfigFile;
import com.voxloud.provisioning.model.DeskConfigFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.voxloud.provisioning.model.ConfigFile.CODECS;
import static com.voxloud.provisioning.model.ConfigFile.DOMAIN;
import static com.voxloud.provisioning.model.ConfigFile.PASSWORD;
import static com.voxloud.provisioning.model.ConfigFile.PORT;
import static com.voxloud.provisioning.model.ConfigFile.USERNAME;

@Service
public class ConfigFileFactory extends BaseConfigFileFactory{

    @Value("${provisioning.domain}")
    private String domain;

    @Value("${provisioning.port}")
    private String port;

    @Value("${provisioning.codecs}")
    private String codecs;

    /**
     * Creates a configuration file based on the device model.
     *
     * @param device The device for which the configuration file is generated.
     * @return ConfigFile instance configured for the given device.
     */
    @Override
    public ConfigFile build(Device device) {
        if(device == null) {
            return null;
        }

        final String overrideFragment = device.getOverrideFragment();
        ConfigFile configFile;

        // Instantiate the approriate ConfigFile class based device model
        switch (device.getModel()) {
            case CONFERENCE:
                configFile = new ConferenceConfigFile(new HashMap<>());
                break;
            case DESK:
                configFile = new DeskConfigFile(new HashMap<>());
                break;
            default:
                throw new VoxloudException("Unsupported device model: " + device.getModel());
        }

        // Set user data and settings for the device in the config file
        setUserData(device, configFile);
        setSettings(configFile.parseOverridenConfig(overrideFragment), configFile);

        return configFile;
    }

    private void setUserData(Device device, ConfigFile configFile) {
        Map<String, Object> config = configFile.getConfig();
        config.put(USERNAME, String.format("****%s",device.getUsername().substring(device.getUsername().length()-2))); // mask username, show only 2 latest characters.
        config.put(PASSWORD, "****"); // Mask the password as it is sensitive
    }

    private void setSettings(Map<Object, Object> overrideSetting, ConfigFile configFile) {
        Map<String, Object> config = configFile.getConfig();

        // Convert overriden settings to Map<String, Object> and put it to config file
        config.putAll(overrideSetting.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> String.valueOf(e.getKey()), // Convert key to String
                        Map.Entry::getValue              // Keep value as is
                )));

        // Set default values if necessary for domain, port, and codecs
        if (isDefaultNeeded(config.get(DOMAIN))) {
            config.put(DOMAIN, domain);
        }

        if (isDefaultNeeded(config.get(PORT))) {
            config.put(PORT, port);
        }

        if (isDefaultNeeded(config.get(CODECS))) {
            configFile.addDefaultCodecs(codecs);
        }
    }

    private boolean isDefaultNeeded(Object value) {
        return value == null || String.valueOf(value).isBlank();
    }
}
