package com.bitcoin.blockchain.api;

import com.bitcoin.blockchain.api.application.ApplicationConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;
import java.security.Security;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource("classpath:spring-config.xml")
public class Application extends SpringBootServletInitializer {

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
        System.out.println("Started up " + name + " on " + config.getEnvironment().name());
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    private static Class<Application> applicationClass = Application.class;

    @Value("${app.name}")
    private String name;

    @Autowired
    public ApplicationConfig config;
}

