package dev.techsphere.ms.controller;

import dev.techsphere.ms.config.VaultPropertiesLoader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MicroserviceController {

    @Autowired
    private VaultPropertiesLoader loader;

    @GetMapping("/vault-props")
    public Map<String, Object> printVaultProps() {
        return loader.getAllVaultProps();
    }

}
