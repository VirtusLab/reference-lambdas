package com.virtuslab.parent_lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.parent_lambda.model.Bar;
import com.virtuslab.parent_lambda.model.Baz;
import com.virtuslab.parent_lambda.model.Foo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.InvocationType;

import java.util.function.Function;

@SpringBootApplication
public class ParentLambdaApplication {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final LambdaClient lambdaClient = LambdaClient.builder()
			.build();

	private final String childLambdaAName = getChildLambdaAName();
	private final String childLambdaBName = getChildLambdaBName();

	public static void main(String[] args) {
		SpringApplication.run(ParentLambdaApplication.class, args);
	}

	private String getChildLambdaAName() {
		if (isRunningInLambda()) {
			String name = System.getenv("CHILD_LAMBDA_A_NAME");
			if (name == null || name.trim().isEmpty()) {
				throw new IllegalStateException("Environment variable CHILD_LAMBDA_A_NAME is not set");
			}
			return name;
		} else {
			return System.getProperty("CHILD_LAMBDA_A_NAME", "test-child-lambda-a");
		}
	}

	private String getChildLambdaBName() {
		if (isRunningInLambda()) {
			String name = System.getenv("CHILD_LAMBDA_B_NAME");
			if (name == null || name.trim().isEmpty()) {
				throw new IllegalStateException("Environment variable CHILD_LAMBDA_B_NAME is not set");
			}
			return name;
		} else {
			return System.getProperty("CHILD_LAMBDA_B_NAME", "test-child-lambda-b");
		}
	}

	private boolean isRunningInLambda() {
		return System.getenv("AWS_LAMBDA_FUNCTION_NAME") != null;
	}

	@Bean
	public Function<String, String> runParentLambda() {
		return input -> {
			try {
				// Create the request payload
				Bar request = new Bar(new Foo(input));
				String requestJson = objectMapper.writeValueAsString(request);

				// Async call to child-lambda-a
				InvokeRequest asyncRequest = InvokeRequest.builder()
						.functionName(childLambdaAName)
						.payload(SdkBytes.fromUtf8String(requestJson))
						.invocationType(InvocationType.EVENT)
						.build();

				lambdaClient.invoke(asyncRequest);

				// Sync call to child-lambda-b
				InvokeRequest syncRequest = InvokeRequest.builder()
						.functionName(childLambdaBName)
						.payload(SdkBytes.fromUtf8String(requestJson))
						.invocationType(InvocationType.REQUEST_RESPONSE)
						.build();

				InvokeResponse response = lambdaClient.invoke(syncRequest);

				// Parse and print the response from child-lambda-b
				String responseJson = response.payload().asUtf8String();
				Baz baz = objectMapper.readValue(responseJson, Baz.class);
				System.out.println("Response from child-lambda-b: " + baz.getStr());

				return "Processing completed";
			} catch (Exception e) {
				e.printStackTrace();
				return "Error: " + e.getMessage();
			}
		};
	}

}
