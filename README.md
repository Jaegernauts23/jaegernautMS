🔐 Modern Authentication Demo (Spring Boot + WebAuthn + JWT)

This is a Spring Boot application that demonstrates both traditional password-based authentication and modern passwordless authentication using WebAuthn (Passkeys) with Yubico’s Java framework.

This project explores how to transition from legacy password login to secure, phishing-resistant passkey authentication while still supporting JWT-based stateless authorization.

🚀 What This Project Demonstrates

Traditional Signup & Login with Password

Passwordless authentication using WebAuthn (FIDO2 Passkeys)

Cryptographic challenge-response verification

Signature counter validation (anti-cloning protection)

JWT token issuance after successful authentication

Protected endpoints secured via Bearer token

MySQL persistence

Docker-ready setup



This project shows how to implement that architecture in a real Spring Boot application.

🛠️ Tech Stack

Java 21

Spring Boot

Gradle

Yubico WebAuthn Server

MySQL

JWT (Bearer Token)

Docker

⚙️ Running the Project Locally
📌 Prerequisites

Make sure you have installed:

Java 21

Gradle (or use Gradle Wrapper)

MySQL (running locally)

A modern browser (Chrome / Edge recommended for WebAuthn)

🗄️ 1️⃣ Configure MySQL

Create a database:

CREATE DATABASE jaegernaut;


Update your application.yml or environment variables with:

spring.datasource.url=jdbc:mysql://localhost:3306/jaegernaut
spring.datasource.username=your_username
spring.datasource.password=your_password

▶️ 2️⃣ Build the Project

docker compose up

or 

sh buildscript.sh

The application will start on:

http://localhost:8080

🧪 How to Test
🔑 Password Flow

Register user

Login

Copy JWT token

Access protected endpoint with:

Authorization: Bearer <your-token>

🔐 Passkey Flow

Register user (email required)

Initiate passkey registration

Complete biometric / device verification

Login with passkey

Receive JWT

Access protected endpoint

📂 Project Structure
controller/
service/
repository/
dto/
entity/
security/
config/


🐳 Docker Support

If using Docker:

docker-compose up --build


This will:

Start MySQL container

Start Spring Boot application


📜 License

This project is for educational and demonstration purposes.

✨ Final Note

This repository is a hands-on exploration of moving beyond password-based systems toward cryptographic authentication models using modern WebAuthn standards.
