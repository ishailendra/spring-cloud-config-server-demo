package dev.techsphere.ms.config;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        String url = environment.getProperty("vault.secrets.url");

        if (StringUtils.hasText(url)) {
            RestTemplate template = new RestTemplate();
            String secret = template.getForObject(url, String.class);
            JSONObject root = new JSONObject(secret);
            JSONArray propSrcArr = root.getJSONArray("propertySources");

            for (Object obj : propSrcArr) {
                JSONObject propSrcObj = (JSONObject) obj;

                var name = propSrcObj.getString("name");

                if (StringUtils.hasText(name) && name.startsWith("vault")) {
                    JSONObject source = propSrcObj.getJSONObject("source");

                    Map<String, Object> dynamicProperties = source.toMap();

                    // Registering/Adding to the environment
                    environment.getPropertySources().addFirst(new MapPropertySource("customProperties", dynamicProperties));
                }
            }

        }

    }


}
