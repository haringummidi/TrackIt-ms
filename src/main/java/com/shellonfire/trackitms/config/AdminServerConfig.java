package com.shellonfire.trackitms.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AdminServerConfig {
    @Bean
    @Primary
    public AdminServerProperties adminServerProperties() {
        return new AdminServerProperties();
    }
}
