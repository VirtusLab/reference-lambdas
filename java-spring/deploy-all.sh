#!/bin/bash

echo "Starting full deployment process..."

# First run rebuild-all script
echo "Running rebuild-all script..."
./rebuild-all.sh

# Check if rebuild was successful
if [ $? -ne 0 ]; then
    echo "Build failed! Stopping deployment."
    exit 1
fi

# Run terraform apply
echo "Running terraform apply..."
cd infra
terraform apply -auto-approve

echo "Deployment process completed!" 