#!/bin/bash
set -e

echo "Building and deploying..."

./gradlew clean build -x test
docker build -t myappimg:latest .
docker-compose down
docker-compose up -d

echo "✅ Done! App running at http://localhost:8080"
