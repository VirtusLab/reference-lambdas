#!/bin/bash

echo "Starting full redeployment process..."

# First destroy existing infrastructure
echo "Destroying existing infrastructure..."
./destroy-all.sh

# Check if destroy was successful
if [ $? -ne 0 ]; then
    echo "Destroy failed! Stopping redeployment."
    exit 1
fi

# Run rebuild-all script
echo "Rebuilding all lambdas..."
./rebuild-all.sh

# Check if rebuild was successful
if [ $? -ne 0 ]; then
    echo "Build failed! Stopping redeployment."
    exit 1
fi

# Run deploy
echo "Deploying new infrastructure..."
./deploy-all.sh

# Find the highest existing number
HIGHEST_NUM=0
for f in out-*.txt; do
    if [ -f "$f" ]; then
        NUM=${f#out-}
        NUM=${NUM%.txt}
        if [[ $NUM =~ ^[0-9]+$ ]] && [ $NUM -gt $HIGHEST_NUM ]; then
            HIGHEST_NUM=$NUM
        fi
    fi
done

# Increment for new file
NEXT_NUM=$((HIGHEST_NUM + 1))

echo "Invoking parent lambda..."
aws lambda invoke \
    --function-name parent-lambda-dev \
    --cli-binary-format raw-in-base64-out \
    --payload '"Hello from AWS Lambda!"' \
    --region eu-north-1 \
    "out-${NEXT_NUM}.txt"

echo "Redeployment process completed!" 

cat "out-${NEXT_NUM}.txt"
