#!/bin/bash

echo "Building parent-lambda..."
cd parent-lambda
./gradlew clean shadowJar
cd ..

echo "Building child-lambda-a..."
cd child-lambda-a
./gradlew clean shadowJar
cd ..

echo "Building child-lambda-b..."
cd child-lambda-b
./gradlew clean shadowJar
cd ..

echo "All builds completed!" 