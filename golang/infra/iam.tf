# Basic Lambda execution role
data "aws_iam_policy_document" "lambda_assume_role" {
  statement {
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["lambda.amazonaws.com"]
    }
  }
}

# Child Lambda A - IAM Role
resource "aws_iam_role" "child_lambda_a_role" {
  name               = "child-lambda-a-role-${var.environment}"
  assume_role_policy = data.aws_iam_policy_document.lambda_assume_role.json
}

resource "aws_iam_role_policy_attachment" "child_lambda_a_basic" {
  role       = aws_iam_role.child_lambda_a_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

# Child Lambda B - IAM Role
resource "aws_iam_role" "child_lambda_b_role" {
  name               = "child-lambda-b-role-${var.environment}"
  assume_role_policy = data.aws_iam_policy_document.lambda_assume_role.json
}

resource "aws_iam_role_policy_attachment" "child_lambda_b_basic" {
  role       = aws_iam_role.child_lambda_b_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

# Parent Lambda - IAM Role and additional permissions
resource "aws_iam_role" "parent_lambda_role" {
  name               = "parent-lambda-role-${var.environment}"
  assume_role_policy = data.aws_iam_policy_document.lambda_assume_role.json
}

resource "aws_iam_role_policy_attachment" "parent_lambda_basic" {
  role       = aws_iam_role.parent_lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

# Custom policy for parent lambda to invoke child lambdas
resource "aws_iam_policy" "lambda_invoke_policy" {
  name = "lambda-invoke-policy-${var.environment}"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "lambda:InvokeFunction",
          "lambda:InvokeAsync"
        ]
        Resource = [
          aws_lambda_function.child_lambda_a.arn,
          aws_lambda_function.child_lambda_b.arn
        ]
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "parent_lambda_invoke" {
  role       = aws_iam_role.parent_lambda_role.name
  policy_arn = aws_iam_policy.lambda_invoke_policy.arn
}
