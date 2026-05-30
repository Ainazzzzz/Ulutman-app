package com.ulutman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
@EnableScheduling
public class UlutmanApplication {


	public static void main(String[] args) {

		SpringApplication.run(UlutmanApplication.class, args);
		System.out.println("auto deploy works!");
	}
	@GetMapping
	public String greetings(){
		return "index";

	}
}
