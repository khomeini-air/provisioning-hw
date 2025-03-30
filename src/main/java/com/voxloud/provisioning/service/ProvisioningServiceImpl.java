package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.factory.BaseConfigFileFactory;
import com.voxloud.provisioning.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvisioningServiceImpl implements ProvisioningService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BaseConfigFileFactory configFileFactory;

    public String getProvisioningFile(String macAddress) {
        return buildResponse(deviceRepository.findById(macAddress).orElse(null));
    }

    private String buildResponse(Device device) {
        if (device == null) {
            return null;
        }

        return configFileFactory.build(device).generateFile();
    }
}
