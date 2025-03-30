package com.voxloud.provisioning.factory;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.model.ConfigFile;

public abstract class BaseConfigFileFactory {

    /**
     * Abstract method to create a configuration file based on the given device.
     * Subclasses must implement this method to provide specific configurations.
     *
     * @param device The device for which the configuration file needs to be created.
     * @return ConfigFile instance specific to the device model.
     */
    public abstract ConfigFile build(Device device);
}
