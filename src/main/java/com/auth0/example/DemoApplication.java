package com.auth0.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
        System.setProperty("MAIN_DOMAIN", dotenv.get("MAIN_DOMAIN"));
		System.setProperty("MAIN_CLIENT_ID", dotenv.get("MAIN_CLIENT_ID"));
		System.setProperty("MAIN_CLIENT_SECRET", dotenv.get("MAIN_CLIENT_SECRET"));
		SpringApplication.run(DemoApplication.class, args);
	}

}
