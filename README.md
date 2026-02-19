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

Start MySQL:
```bash
docker-compose up db -d
```

Run application:
```bash
./gradlew bootRun
```

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

## Passkey Authentication Flows

### Passkey Registration Flow (FIDO2/WebAuthn)

1. User creates account via password signup at `/password/signup`, creating entries in UserDetailsDAO and PasswordLoginDAO
2. User navigates to `/passkey/register` page to add passkey as optional authentication method
3. User enters email (must match existing account) and optional authenticator name
4. Frontend calls POST `/passkey/register/start` with email
5. Server verifies user exists in UserDetailsDAO, generates unique userHandle from email, creates PublicKeyCredentialCreationOptions containing challenge, RP ID (localhost), user info, and cryptographic parameters (ES256/RS256 algorithms)
6. Server stores challenge in memory map keyed by email and returns options to frontend
7. Frontend receives options, converts base64url challenge and userHandle to Uint8Array format
8. Frontend invokes `navigator.credentials.create()` with converted options
9. Browser prompts user for biometric enrollment (fingerprint/face) or device PIN setup
10. Authenticator generates new public-private key pair in device's secure enclave, creates attestation object containing public key, credential ID, and authenticator metadata
11. Authenticator returns credential containing rawId, response.clientDataJSON, response.attestationObject to frontend
12. Frontend sends credential JSON to POST `/passkey/register/finish` with email and authenticator name
13. Server retrieves stored challenge, verifies attestation signature, extracts public key and credential ID from attestation object
14. Server creates PasskeyCredentialDAO entry storing email, credentialId (base64), publicKey (base64), signatureCount (0), authenticatorName, and saves to database
15. Server removes challenge from memory map and returns success
16. User can now login with either password or passkey, multiple passkeys allowed per user

### Passkey Login Flow (FIDO2/WebAuthn)

1. User navigates to `/passkey/login` page
2. User clicks "Login with Passkey" button
3. Frontend calls POST `/passkey/login/start`
4. Server generates cryptographic challenge using Yubico's RelyingParty and returns PublicKeyCredentialRequestOptions containing challenge, RP ID, timeout, and allowed credentials
5. Frontend receives challenge and invokes `navigator.credentials.get()` with the options
6. Browser prompts user for biometric authentication (fingerprint/face) or device PIN
7. Authenticator (device's secure enclave) retrieves private key associated with the credential
8. Authenticator signs the challenge with private key and returns signed assertion containing authenticatorData, clientDataJSON, signature, and credentialId
9. Frontend sends signed credential to POST `/passkey/login/finish`
10. Server verifies signature using stored public key from PasskeyCredentialDAO, validates challenge matches, checks signature counter to prevent replay attacks
11. If verification succeeds, server extracts user email from credential, generates JWT token, stores in session, and returns success
12. User is authenticated and redirected to protected resources

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
