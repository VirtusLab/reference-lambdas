# Child Lambda A
resource "aws_lambda_function" "child_lambda_a" {
  filename         = "../child-lambda-a/function.zip"
  function_name    = "child-lambda-a-${var.environment}"
  role            = aws_iam_role.child_lambda_a_role.arn
  handler         = "bootstrap"
  source_code_hash = filebase64sha256("../child-lambda-a/function.zip")
  runtime         = "provided.al2"
  timeout         = 10
  memory_size     = 128

  environment {
    variables = {
      GOOS   = "linux"
      GOARCH = "amd64"
    }
  }
}

# Child Lambda B
resource "aws_lambda_function" "child_lambda_b" {
  filename         = "../child-lambda-b/function.zip"
  function_name    = "child-lambda-b-${var.environment}"
  role            = aws_iam_role.child_lambda_b_role.arn
  handler         = "bootstrap"
  source_code_hash = filebase64sha256("../child-lambda-b/function.zip")
  runtime         = "provided.al2"
  timeout         = 10
  memory_size     = 128

  environment {
    variables = {
      GOOS   = "linux"
      GOARCH = "amd64"
    }
  }
}

# Parent Lambda
resource "aws_lambda_function" "parent_lambda" {
  filename         = "../parent-lambda/function.zip"
  function_name    = "parent-lambda-${var.environment}"
  role            = aws_iam_role.parent_lambda_role.arn
  handler         = "bootstrap"
  source_code_hash = filebase64sha256("../parent-lambda/function.zip")
  runtime         = "provided.al2"
  timeout         = 30
  memory_size     = 128

  environment {
    variables = {
      CHILD_LAMBDA_A_NAME = aws_lambda_function.child_lambda_a.function_name
      CHILD_LAMBDA_B_NAME = aws_lambda_function.child_lambda_b.function_name
      GOOS               = "linux"
      GOARCH             = "amd64"
    }
  }

  depends_on = [
    aws_lambda_function.child_lambda_a,
    aws_lambda_function.child_lambda_b,
    aws_iam_role_policy_attachment.parent_lambda_invoke
  ]
}
