#!/bin/bash

echo "Starting infrastructure destruction process..."

# Run terraform destroy
echo "Running terraform destroy..."
cd infra
terraform destroy -auto-approve

echo "Infrastructure destruction completed!" 