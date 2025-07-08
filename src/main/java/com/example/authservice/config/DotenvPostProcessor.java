package com.example.authservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.stream.Collectors;

public class DotenvPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("🚀 DotenvPostProcessor is running");

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.out.println("✔️ Loaded from .env: " + dotenv.get("DB_URI"));

        //log.info("Mongo URI from .env = {}", dotenv.get("DB_URI"));
        MapPropertySource dotenvSource = new MapPropertySource(
                "dotenv",
                dotenv.entries().stream().collect(
                        Collectors.toMap(e -> e.getKey(), e -> e.getValue())
                )
        );

        environment.getPropertySources().addLast(dotenvSource);
    }
}








