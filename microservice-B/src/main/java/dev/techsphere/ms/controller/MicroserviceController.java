package dev.techsphere.ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MicroserviceController {

    @Autowired
    private Environment env;

    @GetMapping("/vault-props")
    public Map<String, Object> printVaultProps() {



        return null;
    }

}
