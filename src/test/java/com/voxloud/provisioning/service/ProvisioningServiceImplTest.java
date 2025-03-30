package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.factory.BaseConfigFileFactory;
import com.voxloud.provisioning.model.ConfigFile;
import com.voxloud.provisioning.model.DeskConfigFile;
import com.voxloud.provisioning.repository.DeviceRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Optional;

@SpringBootTest(classes = ProvisioningServiceImpl.class)
@RunWith(SpringJUnit4ClassRunner.class)
class ProvisioningServiceImplTest {
    @MockBean
    private BaseConfigFileFactory configFileFactory;

    @MockBean
    private DeviceRepository deviceRepository;

    @Autowired
    private ProvisioningServiceImpl provisioningServiceImpl;

    private Device device;

    @BeforeEach
    void setup() {
        String macAddress = "aa-bb-cc-dd-ee-ff";
        String password = "blah";
        String username = "johny";

        device = new Device();
        device.setMacAddress(macAddress);
        device.setPassword(password);
        device.setUsername(username);
        device.setModel(Device.DeviceModel.DESK);
    }

    @Test
    void testGetDeskProvisioningFile() {
        Map<String, Object> configMap = Map.of("port","5060", "username", "****ny", "password","****", "domain", "sip.voxloud.com", "codecs","G711,G729,OPUS");
        ConfigFile deskConfigFile = new DeskConfigFile(configMap);

        Mockito.when(deviceRepository.findById(device.getMacAddress())).thenReturn(Optional.of(device));
        Mockito.when(configFileFactory.build(device)).thenReturn(deskConfigFile);
        String result = provisioningServiceImpl.getProvisioningFile(device.getMacAddress());

        Mockito.verify(deviceRepository).findById(device.getMacAddress());
        Mockito.verify(configFileFactory).build(device);
        Assert.assertTrue(result.contains("username=****ny\n"));
        Assert.assertTrue(result.contains("codecs=G711,G729,OPUS\n"));
        Assert.assertTrue(result.contains("domain=sip.voxloud.com\n"));
        Assert.assertTrue(result.contains("password=****\n"));
        Assert.assertTrue(result.contains("port=5060\n"));
    }

    @Test
    void testGetDeskProvisioningFileDeviceNotFound() {
        Map<String, Object> configMap = Map.of("port","5060", "username", "****ny", "password","****", "domain", "sip.voxloud.com", "codecs","G711,G729,OPUS");
        ConfigFile deskConfigFile = new DeskConfigFile(configMap);

        Mockito.when(deviceRepository.findById(device.getMacAddress())).thenReturn(Optional.empty());
        Assertions.assertNull(provisioningServiceImpl.getProvisioningFile(device.getMacAddress()));
    }
}
