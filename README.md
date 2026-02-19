# JaugernautMS - Spring Boot Microservice with Passkey Authentication

A Spring Boot application demonstrating password-based and FIDO2/WebAuthn passkey authentication with JWT tokens.

## Features
- Password-based signup and login
- FIDO2/WebAuthn passkey registration and authentication
- JWT token generation
- MySQL database integration
- Docker containerization

## Prerequisites
- Java 21
- Docker & Docker Compose
- Gradle
- Docker Hub account (for building and pushing images)

## Setup Instructions

### 1. Configure Docker Account
Replace `DOCKERACCOUNT` with your Docker Hub username in the following files:

**buildScript.sh:**
```bash
docker build -t YOUR_DOCKERHUB_USERNAME/myapp:latest .
docker push YOUR_DOCKERHUB_USERNAME/myapp:latest
```

**docker-compose.yml:**
```yaml
app:
  image: YOUR_DOCKERHUB_USERNAME/myapp:latest
```

### 2. Build the Application

**Using Gradle:**
```bash
./gradlew clean build -x test
```

**Using the build script:**
```bash
chmod +x buildScript.sh
./buildScript.sh
```

### 3. Run with Docker Compose

```bash
docker-compose up -d
```

The application will be available at: `http://localhost:8080`

### 4. Run Locally (without Docker)

Set environment variables:
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/mydb
export SPRING_DATASOURCE_USERNAME=myuser
export SPRING_DATASOURCE_PASSWORD=mypass
export SPRING_DATASOURCE_DRIVER=com.mysql.cj.jdbc.Driver
export SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

compile the application application:
```bash
./gradlew bootRun
```


Start MySQL:
```bash
docker-compose up db -d
```

Otherwise execute the build scirpt
./buildScript.sh -> git bash
bash buildScript.sh -> windows bash


## API Endpoints

### Password Authentication
- `GET /password/signup` - Signup page
- `POST /password/signup` - Register user
- `GET /password/login` - Login page
- `POST /password/login` - Authenticate user

### Passkey Authentication
- `GET /passkey/register` - Passkey registration page
- `POST /passkey/register/start` - Start passkey registration
- `POST /passkey/register/finish` - Complete passkey registration
- `GET /passkey/login` - Passkey login page
- `POST /passkey/login/start` - Start passkey authentication
- `POST /passkey/login/finish` - Complete passkey authentication

## Usage Flow

1. **Create Account:** Visit `/password/signup` and register with email/password
2. **Optional - Add Passkey:** Visit `/passkey/register` and register a passkey using biometrics
3. **Login:** Use either `/password/login` or `/passkey/login`
4. **Access Protected Resources:** JWT token stored in session


## Technology Stack
- Spring Boot 3.3.3
- Java 21
- Yubico WebAuthn Server 2.5.0
- MySQL 8
- Docker
- Thymeleaf
- JWT (jjwt 0.11.5)
- Lombok
- MapStruct

## Database Configuration
Default MySQL credentials (change in docker-compose.yml):
- Database: `mydb`
- Username: `myuser`
- Password: `mypass`
- Port: `3307`

## Project Structure
```
src/main/java/com/microservices/
├── configurations/          # WebAuthn and app configurations
├── controllers/
│   ├── PasskeyControllers/  # Passkey registration and login
│   └── PasswordControllers/ # Password-based auth
├── models/
│   ├── DAO/                 # Database entities
│   ├── DTO/                 # Data transfer objects
│   └── Mappers/             # MapStruct mappers
├── repositories/            # JPA repositories
├── services/
│   ├── PasskeyServices/     # Passkey business logic
│   └── PasswordServices/    # Password auth logic
└── JWTservices/             # JWT token generation

src/main/resources/
└── templates/
    ├── PasskeyFlowTemplates/    # Passkey HTML pages
    └── PasswordFlowTemplates/   # Password HTML pages
```

## Stopping the Application
```bash
docker-compose down
```

## License
MIT
