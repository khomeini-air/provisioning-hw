package com.voxloud.provisioning.factory;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.model.ConfigFile;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ConfigFileFactory.class)
@RunWith(SpringJUnit4ClassRunner.class)
class ConfigFileFactoryTest {
    @Value("${provisioning.domain}")
    private String domain;

    @Value("${provisioning.port}")
    private String port;

    @Value("${provisioning.codecs}")
    private String codecs;

    @Autowired
    private ConfigFileFactory configFileFactory;

    @Test
    void testCreateDeskFileNoOverriding() {

        String macAddress = "aa-bb-cc-dd-ee-ff";
        String password = "blah";
        String username = "johny";
        String configFileString= "password=****\n" +
                "port=5060\n" +
                "domain=sip.voxloud.com\n" +
                "codecs=G711,G729,OPUS\n" +
                "username=****ny\n";

        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setPassword(password);
        device.setUsername(username);
        device.setModel(Device.DeviceModel.DESK);

        ConfigFile configFile = configFileFactory.build(device);

        // Act and Assert
        assertEquals(5, configFile.getConfig().size());
        assertEquals(configFileString, configFile.generateFile());
    }

    @Test
    void testCreateDeskFileWithOverriding() {

        String macAddress = "aa-bb-cc-dd-ee-override";
        String password = "123456";
        String username = "roberto";
        String overrideString = "domain=sip.anotherdomain.com\n" +
                "port=5161\n" +
                "timeout=10";
        String configFileString = "password=****\n" +
                "port=5161\n" +
                "domain=sip.anotherdomain.com\n" +
                "codecs=G711,G729,OPUS\n" +
                "timeout=10\n" +
                "username=****to\n";

        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setPassword(password);
        device.setUsername(username);
        device.setOverrideFragment(overrideString);
        device.setModel(Device.DeviceModel.DESK);

        ConfigFile configFile = configFileFactory.build(device);

        assertEquals(6, configFile.getConfig().size());
        assertEquals(configFileString, configFile.generateFile());
    }

    @Test
    void testCreateDeskFileWithOverridingCodecAndMore() {

        String macAddress = "aa-bb-cc-dd-ee-override";
        String password = "123456";
        String username = "inzaghi";
        String overrideString = "domain=sip.acmilan.com\n" +
                "port=5161\n" +
                "codecs=7788,9654\n" +
                "isFast=true\n" +
                "max=9";
        String configFileString = "password=****\n" +
                "isFast=true\n" +
                "port=5161\n" +
                "max=9\n" +
                "domain=sip.acmilan.com\n" +
                "codecs=7788,9654\n" +
                "username=****hi\n";

        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setPassword(password);
        device.setUsername(username);
        device.setOverrideFragment(overrideString);
        device.setModel(Device.DeviceModel.DESK);

        ConfigFile configFile = configFileFactory.build(device);

        assertEquals(7, configFile.getConfig().size());
        assertEquals(configFileString, configFile.generateFile());
    }
    @Test
    void testCreateConferenceFileNoOverriding() {
        String macAddress = "aa-bb-cc-dd-ee-ff";
        String password = "nnnonono";
        String username = "mia";
        String configFileString = "{\"password\":\"****\",\"port\":\"5060\",\"domain\":\"sip.voxloud.com\",\"codecs\":[\"G711\",\"G729\",\"OPUS\"],\"username\":\"****ia\"}";

        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setPassword(password);
        device.setUsername(username);
        device.setModel(Device.DeviceModel.CONFERENCE);

        ConfigFile configFile = configFileFactory.build(device);

        // Act and Assert
        assertEquals(5, configFile.getConfig().size());
        assertEquals(configFileString, configFile.generateFile());
    }

    @Test
    void testCreateConferenceFileWithOverriding() {
        String macAddress = "aa-bb-cc-dd-ee-ff-override";
        String password = "italypass";
        String username = "rome";
        String overrideString = "{\"domain\":\"sip.anotherdomain.com\",\"port\":\"5161\",\"timeout\":10}";
        String configFileString = "{\"password\":\"****\",\"port\":\"5161\",\"domain\":\"sip.anotherdomain.com\",\"codecs\":[\"G711\",\"G729\",\"OPUS\"],\"timeout\":10,\"username\":\"****me\"}";

        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setPassword(password);
        device.setUsername(username);
        device.setOverrideFragment(overrideString);
        device.setModel(Device.DeviceModel.CONFERENCE);

        ConfigFile configFile = configFileFactory.build(device);

        // Act and Assert
        assertEquals(6, configFile.getConfig().size());
        assertEquals(configFileString, configFile.generateFile());
    }

    @Test
    void testCreateConferenceFileWithOverridingCodecsAndMore() {
        String macAddress = "aa-bb-cc-dd-ee-ff-codec";
        String password = "godfather";
        String username = "sicily";
        String overrideString = "{\"domain\":\"sip.juve.com\",\"port\":\"5161\",\"max\":10,\"isTrue\":true,\"codecs\":[\"3423\",\"7883\"]}";
        String configFileString = "{\"isTrue\":true,\"password\":\"****\",\"port\":\"5161\",\"max\":10,\"domain\":\"sip.juve.com\",\"codecs\":[\"3423\",\"7883\"],\"username\":\"****ly\"}";

        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setPassword(password);
        device.setUsername(username);
        device.setOverrideFragment(overrideString);
        device.setModel(Device.DeviceModel.CONFERENCE);

        ConfigFile configFile = configFileFactory.build(device);

        // Act and Assert
        assertEquals(7, configFile.getConfig().size());
        assertEquals(configFileString, configFile.generateFile());
    }
}
