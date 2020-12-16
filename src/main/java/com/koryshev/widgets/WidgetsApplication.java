package com.koryshev.widgets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Starts the Widgets application.
 *
 * @author Ivan Koryshev
 */
@SpringBootApplication
@EnableJpaAuditing
public class WidgetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WidgetsApplication.class, args);
    }
}
