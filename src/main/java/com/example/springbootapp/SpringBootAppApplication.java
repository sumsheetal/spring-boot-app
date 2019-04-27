package com.example.springbootapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.example.*")
public class SpringBootAppApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(SpringBootAppApplication.class, args);
			System.out.println("application started............");
		}catch (Exception ex)
		{
			System.out.println("Error occured............");
			ex.printStackTrace();
		}
	}

}
