# RelayChat Project

## Overview

RelayChat is a web-based real-time communication platform where users can engage in text and video chats with randomly matched peers. The application implements several microservices for session management, matching, video chat, and user termination. The project is built using **Spring Boot**, **WebSockets**, **WebRTC**, and databases such as **MySQL** and **MongoDB**. It also incorporates an **API Gateway** for seamless communication between the frontend and backend services.

## Project Architecture

The system is composed of the following services:

1. **Session Management Service** (Port: 8081)
   - Handles session creation and management using **MySQL**.
   - Users are added to active sessions and tracked here.
  
2. **Matching Service** (Port: 8082)
   - Matches users based on availability using an in-memory queue.
   - Manages match creation, termination, and stores matches in **MongoDB**.
   - Real-time notifications of matches are sent via **WebSockets**.

3. **Video Chat Service** (Port: 8083)
   - Manages WebRTC signaling (ICE candidates and SDP offers/answers) for real-time video communication.
   - Uses **STUN/TURN** servers for WebRTC connectivity.
  
4. **Termination Service** (Port: 8084)
   - Handles session termination requests from users.
   - Updates the session and matching records, and ensures clean disconnects.

5. **API Gateway** (Port: 8080)
   - Central point of communication between the frontend and the backend microservices.
   - Routes requests from the frontend to the appropriate backend services.

6. **Frontend**
   - Developed using **HTML**, **CSS**, and **JavaScript**.
   - Connects to the backend using the API Gateway for session management, matching, and WebRTC for video communication.

## Technologies Used

- **Spring Boot**: Core framework for microservices.
- **WebSockets**: Enables real-time notifications for user matches.
- **WebRTC**: Provides real-time peer-to-peer video communication.
- **MySQL**: Stores session-related data in the session management service.
- **MongoDB**: Stores matched user data in the matching service.
- **API Gateway**: Centralized routing system using **Spring Cloud Gateway**.
- **Docker (Optional)**: Used for setting up services in containers if needed for deployment.
- **SockJS and STOMP**: Used for WebSocket fallback and real-time message handling between the frontend and backend.

## Prerequisites

Before running the project, make sure the following are installed:

- **Java 17+**
- **Maven** (for building the project)
- **MySQL** (for session data)
- **MongoDB** (for matching data)
- **Ngrok** (optional, for exposing local services to the internet for testing)
- **Python (for Frontend server)**

### Setting Up MySQL

1. Install MySQL and start the service.
2. Create a database named `relaychat`.
3. In **session-management-service**'s `application.properties`, configure your MySQL connection:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/relaychat
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

### Setting Up MongoDB

1. Install MongoDB and start the service.
2. In **matching-service**'s `application.properties`, configure the MongoDB connection:

    ```properties
    spring.data.mongodb.host=localhost
    spring.data.mongodb.port=27017
    spring.data.mongodb.database=relaychat
    ```

## Steps to Run the Project

### 1. Clone the Project

```bash
git clone https://github.com/your-repository/relaychat.git
cd relaychat
```

### 2. Set Up and Start MySQL and MongoDB
Ensure both MySQL and MongoDB are running and properly configured.

### 3. Build the Project
Each service has its own directory. Navigate to each service and run the following commands:
```bash
cd session-management-service
mvn clean install
mvn spring-boot:run
```

Repeat the above for the following services:

- matching-service
- video-chat-service
- termination-service
- api-gateway

### 4. Run the Frontend Server
You can use a simple Python HTTP server to serve the frontend:

```bash
Copy code
cd frontend
python -m http.server 8000
```
Now, your frontend will be accessible at `http://localhost:8000`.

### 5. API Endpoints
Here are some key endpoints exposed through the API Gateway:

- Session Management:

  - `POST /api/sessions/create`: Create a new user session.
  - `POST /api/sessions/terminate`: Terminate a session.

-Matching Service:

  - `POST /api/matching/match`: Match a user with another available user.
  - `POST /api/matching/newmatch`: Request a new match.
  - `POST /api/matching/close`: Close the current match.
- Video Chat:

  - `POST /api/video/sendCandidate`: Send an ICE candidate.
  - `GET /api/video/receiveCandidate`: Get an ICE candidate.
  - `POST /api/video/sendOffer`: Send an SDP offer.
