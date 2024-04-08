package com.example.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SsrReactSpringBootGraalvmJsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsrReactSpringBootGraalvmJsApplication.class, args);
	}

}
