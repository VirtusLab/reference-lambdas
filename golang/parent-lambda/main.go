package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"

	"shared/model"

	"github.com/aws/aws-lambda-go/lambda"
	"github.com/aws/aws-sdk-go-v2/aws"
	awsconfig "github.com/aws/aws-sdk-go-v2/config"
	awslambda "github.com/aws/aws-sdk-go-v2/service/lambda"
	"github.com/aws/aws-sdk-go-v2/service/lambda/types"
)

var (
	lambdaClient *awslambda.Client
	childLambdaA string
	childLambdaB string
)

func init() {
	cfg, err := awsconfig.LoadDefaultConfig(context.TODO())
	if err != nil {
		log.Fatal(err)
	}
	lambdaClient = awslambda.NewFromConfig(cfg)

	childLambdaA = getChildLambdaAName()
	childLambdaB = getChildLambdaBName()
}

func getChildLambdaAName() string {
	if isRunningInLambda() {
		name := os.Getenv("CHILD_LAMBDA_A_NAME")
		if name == "" {
			log.Fatal("Environment variable CHILD_LAMBDA_A_NAME is not set")
		}
		return name
	}
	return "test-child-lambda-a"
}

func getChildLambdaBName() string {
	if isRunningInLambda() {
		name := os.Getenv("CHILD_LAMBDA_B_NAME")
		if name == "" {
			log.Fatal("Environment variable CHILD_LAMBDA_B_NAME is not set")
		}
		return name
	}
	return "test-child-lambda-b"
}

func isRunningInLambda() bool {
	return os.Getenv("AWS_LAMBDA_FUNCTION_NAME") != ""
}

func handleRequest(ctx context.Context, input string) (string, error) {
	// Create request payload
	request := model.Bar{
		Foo: model.Foo{
			Str: input,
		},
	}

	payload, err := json.Marshal(request)
	if err != nil {
		return "", fmt.Errorf("failed to marshal request: %v", err)
	}

	// Async call to child-lambda-a
	_, err = lambdaClient.Invoke(ctx, &awslambda.InvokeInput{
		FunctionName:   aws.String(childLambdaA),
		InvocationType: types.InvocationTypeEvent,
		Payload:        payload,
	})
	if err != nil {
		return "", fmt.Errorf("failed to invoke child-lambda-a: %v", err)
	}

	// Sync call to child-lambda-b
	resp, err := lambdaClient.Invoke(ctx, &awslambda.InvokeInput{
		FunctionName:   aws.String(childLambdaB),
		InvocationType: types.InvocationTypeRequestResponse,
		Payload:        payload,
	})
	if err != nil {
		return "", fmt.Errorf("failed to invoke child-lambda-b: %v", err)
	}

	// Parse and print response from child-lambda-b
	var baz model.Baz
	if err := json.Unmarshal(resp.Payload, &baz); err != nil {
		return "", fmt.Errorf("failed to unmarshal response: %v", err)
	}

	log.Printf("Response from child-lambda-b: %s", baz.Str)
	return "Processing completed", nil
}

func main() {
	lambda.Start(handleRequest)
}
