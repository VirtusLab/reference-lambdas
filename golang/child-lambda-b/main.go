package main

import (
	"context"
	"log"

	"shared/model"

	"github.com/aws/aws-lambda-go/lambda"
)

func handleRequest(ctx context.Context, event model.Bar) (model.Baz, error) {
	log.Printf("Received input: %+v", event)

	// Reverse the string
	runes := []rune(event.Foo.Str)
	for i, j := 0, len(runes)-1; i < j; i, j = i+1, j-1 {
		runes[i], runes[j] = runes[j], runes[i]
	}

	return model.Baz{Str: string(runes)}, nil
}

func main() {
	lambda.Start(handleRequest)
}
