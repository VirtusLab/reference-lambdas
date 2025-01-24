output "child_lambda_a_arn" {
  value = aws_lambda_function.child_lambda_a.arn
}

output "child_lambda_b_arn" {
  value = aws_lambda_function.child_lambda_b.arn
}

output "parent_lambda_arn" {
  value = aws_lambda_function.parent_lambda.arn
}
