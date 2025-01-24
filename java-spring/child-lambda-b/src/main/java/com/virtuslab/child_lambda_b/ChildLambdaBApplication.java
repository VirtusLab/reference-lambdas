package com.virtuslab.child_lambda_b;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.virtuslab.child_lambda_b.model.Bar;
import com.virtuslab.child_lambda_b.model.Baz;

@SpringBootApplication
public class ChildLambdaBApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildLambdaBApplication.class, args);
	}

	@Bean
	public Function<Bar, Baz> reverseMessage() {
		return event -> {
			System.out.println("Received input: " + event);
			return new Baz(new StringBuilder(event.getFoo().getStr()).reverse().toString());
		};
	}
}
