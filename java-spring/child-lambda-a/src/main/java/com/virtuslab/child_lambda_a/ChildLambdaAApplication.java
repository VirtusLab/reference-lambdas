package com.virtuslab.child_lambda_a;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.virtuslab.child_lambda_a.model.Bar;

@SpringBootApplication
public class ChildLambdaAApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildLambdaAApplication.class, args);
	}

	@Bean
	public Function<Bar, Void> uppercaseMessage() {
		return event -> {
			System.out.println("Received input: " + event);
			String upperCased = event.getFoo().getStr().toUpperCase();
			System.out.println(upperCased);
			return null;
		};
	}

}
