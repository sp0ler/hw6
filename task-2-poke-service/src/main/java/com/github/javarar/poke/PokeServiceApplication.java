package com.github.javarar.poke;

import com.github.javarar.poke.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfig.class)
public class PokeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokeServiceApplication.class, args);
    }

}
