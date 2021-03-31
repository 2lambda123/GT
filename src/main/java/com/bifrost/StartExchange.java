package com.bifrost;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log
@SpringBootApplication
public class StartExchange {

	public static void main(String[] args) throws InterruptedException {

		SpringApplication.run(StartExchange.class, args);

	}

}
