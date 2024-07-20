package com.shellonfire.trackitms;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAdminServer
@EnableJpaRepositories("com.shellonfire.trackitms.repository")
@EntityScan(basePackages = "com.shellonfire.trackitms.entity")
@EnableAspectJAutoProxy
@SpringBootApplication
public class TrackItMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackItMsApplication.class, args);
    }

}
