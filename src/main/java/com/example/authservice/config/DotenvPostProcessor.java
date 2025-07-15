package com.example.authservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DotenvPostProcessor implements EnvironmentPostProcessor {

    private static final Logger LOGGER = Logger.getLogger(DotenvPostProcessor.class.getName());

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        LOGGER.log( Level.FINE, "🚀 DotenvPostProcessor is running");
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        LOGGER.log(Level.FINE, "✅ Loaded from .env: {0}", dotenv.get("DB_URI"));

        MapPropertySource dotenvSource = new MapPropertySource(
                "dotenv",
                dotenv.entries().stream().collect(
                        Collectors.toMap(e -> e.getKey(), e -> e.getValue())
                )
        );

        environment.getPropertySources().addLast(dotenvSource);
    }
}