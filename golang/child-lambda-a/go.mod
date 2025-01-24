module child-lambda-a

go 1.21

require (
	github.com/aws/aws-lambda-go v1.46.0
	shared v0.0.0-00010101000000-000000000000
)

replace shared => ../shared
