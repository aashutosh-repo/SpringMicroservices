package com.spring.batch.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "input.file")
@PropertySource(value = "classpath:input-files-location.yml", factory = YamlPropertySourceFactory.class)
public class InputFileProperties {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
