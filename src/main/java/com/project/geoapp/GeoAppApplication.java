package com.project.geoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.project.geoapp")
public class GeoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoAppApplication.class, args);
	}

}
