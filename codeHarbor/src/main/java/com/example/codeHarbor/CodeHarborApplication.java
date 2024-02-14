package com.example.codeHarbor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class CodeHarborApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeHarborApplication.class, args);
	}

}
