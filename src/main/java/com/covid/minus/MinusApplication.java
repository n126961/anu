package com.covid.minus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.covid.minus.repo" })
public class MinusApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MinusApplication.class, args);
	}

}
