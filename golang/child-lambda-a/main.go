package main

import (
	"context"
	"log"
	"strings"

	"shared/model"

	"github.com/aws/aws-lambda-go/lambda"
)

func handleRequest(ctx context.Context, event model.Bar) error {
	log.Printf("Received input: %+v", event)
	upperCased := strings.ToUpper(event.Foo.Str)
	log.Println(upperCased)
	return nil
}

func main() {
	lambda.Start(handleRequest)
}
