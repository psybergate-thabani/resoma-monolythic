package com.psybergate.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.psybergate.*")
@EnableJpaRepositories(basePackages = "com.psybergate.*")
@ComponentScan(basePackages = "com.psybergate.*")
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
