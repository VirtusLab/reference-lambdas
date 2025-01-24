# Build and Package Lambda Functions

Run this command from the `golang` directory to build and package all Lambda functions:

```bash
(cd child-lambda-a && GOOS=linux GOARCH=amd64 go build -o bootstrap && zip function.zip bootstrap) && \
(cd child-lambda-b && GOOS=linux GOARCH=amd64 go build -o bootstrap && zip function.zip bootstrap) && \
(cd parent-lambda && GOOS=linux GOARCH=amd64 go build -o bootstrap && zip function.zip bootstrap)
```

This command will:
1. Build each Lambda function for Linux/amd64 (required for AWS Lambda)
2. Name the binary 'bootstrap' (required for provided.al2 runtime)
3. Package the binary into function.zip
4. Repeat for all three Lambda functions

After running this command, you can deploy the functions using Terraform from the `infra` directory.
