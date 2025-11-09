#!/bin/bash
set -e

echo "Building and deploying..."


./gradlew clean build -x test
docker build -t rambhargav017/myapp:latest .
docker push rambhargav017/myapp:latest
docker-compose down
docker-compose up -d

echo "✅ Done! App running at http://localhost:8080"
