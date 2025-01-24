package com.virtuslab.parent_lambda;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParentLambdaApplicationTests {

	@BeforeAll
	static void setup() {
		System.setProperty("CHILD_LAMBDA_A_NAME", "test-child-lambda-a");
		System.setProperty("CHILD_LAMBDA_B_NAME", "test-child-lambda-b");
	}

	@Test
	void contextLoads() {
	}
}
