package dev.techsphere.ms.config;

import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class VaultPropertiesLoader {

    Map<String, Object> vaultProps = new HashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void loadVaultProperties() {
        String secret = restTemplate.getForObject("http://localhost:8888/config-server/services-sit1-props/sit1", String.class);
        JSONObject root = new JSONObject(secret);
        JSONArray propSrcArr = root.getJSONArray("propertySources");

        for (Object obj : propSrcArr) {
            JSONObject propSrcObj = (JSONObject) obj;

            var name = propSrcObj.getString("name");

            if (StringUtils.hasText(name) && name.startsWith("vault")) {
                JSONObject source = propSrcObj.getJSONObject("source");
                vaultProps = source.toMap();
            }
        }

    }

    public Object getProperty (String key) {
        return vaultProps.get(key);
    }

    public Map<String, Object> getAllVaultProps() {
        return vaultProps;
    }
}
