# Child Lambda A
resource "aws_lambda_function" "child_lambda_a" {
  filename      = "../child-lambda-a/build/libs/child-lambda-a-0.0.1-SNAPSHOT-all.jar"
  function_name = "child-lambda-a-${var.environment}"
  role          = aws_iam_role.child_lambda_a_role.arn
  handler       = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
  runtime       = "java21"
  timeout       = 30
  memory_size   = 512

  environment {
    variables = {
      SPRING_CLOUD_FUNCTION_DEFINITION = "uppercaseMessage"
    }
  }
}

# Child Lambda B
resource "aws_lambda_function" "child_lambda_b" {
  filename      = "../child-lambda-b/build/libs/child-lambda-b-0.0.1-SNAPSHOT-all.jar"
  function_name = "child-lambda-b-${var.environment}"
  role          = aws_iam_role.child_lambda_b_role.arn
  handler       = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
  runtime       = "java21"
  timeout       = 30
  memory_size   = 512

  environment {
    variables = {
      SPRING_CLOUD_FUNCTION_DEFINITION = "reverseMessage"
    }
  }
}

# Parent Lambda
resource "aws_lambda_function" "parent_lambda" {
  filename      = "../parent-lambda/build/libs/parent-lambda-0.0.1-SNAPSHOT-all.jar"
  function_name = "parent-lambda-${var.environment}"
  role          = aws_iam_role.parent_lambda_role.arn
  handler       = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
  runtime       = "java21"
  timeout       = 30
  memory_size   = 512

  environment {
    variables = {
      SPRING_CLOUD_FUNCTION_DEFINITION = "runParentLambda"
      CHILD_LAMBDA_A_NAME              = aws_lambda_function.child_lambda_a.function_name
      CHILD_LAMBDA_B_NAME              = aws_lambda_function.child_lambda_b.function_name
    }
  }

  depends_on = [
    aws_lambda_function.child_lambda_a,
    aws_lambda_function.child_lambda_b,
    aws_iam_role_policy_attachment.parent_lambda_invoke
  ]
} 
