package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProvisioningController {

    @Autowired
    private ProvisioningService provisioningService;

    /**
     * GET API for provisioning request.
     * @param macAddress
     * @return
     */
    @GetMapping("/provisioning/{macAddress}")
    public ResponseEntity<String> provisioning(@PathVariable final String macAddress) {
        final String response = provisioningService.getProvisioningFile(macAddress);
        return response == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(response);
    }
}