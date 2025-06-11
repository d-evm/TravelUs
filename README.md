# TravelUs 🌍✈️

A comprehensive group travel management web application that simplifies travel planning by bringing all your travel needs into one unified platform.

## 📋 Table of Contents

- [About](##about)
- [Features](#features)
- [Tech Stack](##tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Development Roadmap](#development-roadmap)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)

## 🎯 About

TravelUs is a solo-developed group travel management application designed to eliminate the hassle of coordinating group trips. Whether you're planning a family vacation, corporate retreat, or adventure with friends, TravelUs provides all the tools you need to organize, communicate, and manage your travel experience seamlessly.

### What TravelUs Can Do

- **Centralized Group Management**: Create travel groups and invite members through secure invite links
- **Document Organization**: Upload and store important travel documents (flight tickets, hotel bookings, etc.) with PDF preview
- **Smart Expense Tracking**: Track shared expenses with automatic balance calculations and settle-up functionality
- **Interactive Itinerary Planning**: Create detailed travel itineraries with destinations, dates, and activities
- **Real-time Communication**: In-group chat system for travel planning
- **Secure Authentication**: JWT-based user authentication and role-based access control

## ✨ Features

### 👤 User Management
- User registration and authentication with JWT
- Profile management and customization
- Secure group joining via invite links

### 👥 Group Management
- Create and manage travel groups
- Generate shareable invite links
- Admin role assignment and permissions
- Member management and group settings

### 📄 Document Management
- PDF upload and storage (flight tickets, hotel bookings, visas, etc.)
- Document preview and organization
- Cloud storage integration for reliable access

### 📝 Travel Itinerary
- Add destinations with dates and details
- Activity planning and scheduling
- Visual timeline view of your trip
- Collaborative itinerary editing

### 💸 Expense Tracking
- Record and categorize shared expenses
- Automatic balance calculations
- Split expenses among group members
- Settle-up functionality with payment tracking

### 💬 Communication
- Real-time group chat
- Message history and search
- Implemented using Sockets

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot (Java)
- **Security**: Spring Security with JWT authentication
- **Database**: PostgreSQL with Hibernate/JPA ORM
- **Build Tool**: Maven
- **File Storage**: Supabase
- **Real-time Communication**: WebSocket/STOMP
- **Testing**: JUnit + Mockito

### Frontend (In Development)
- **Framework**: React.js
- **Styling**: TailwindCSS
- **State Management**: Redux Toolkit
- **HTTP Client**: Axios
- **Real-time**: Socket.IO
- **Notifications**: Firebase

### DevOps & Deployment
- **Backend Hosting**: Render.com / Railway (planned)
- **Frontend Hosting**: Vercel / Netlify (planned)
- **Database**: Supabase
- **Version Control**: Git + GitHub

## 📂 Project Structure

```
TravelUs/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── travelus/
│   │   │   │           └── backend/
│   │   │   │               ├── config/          # Security & WebSocket configuration
│   │   │   │               ├── controllers/     # REST API endpoints
│   │   │   │               ├── dtos/           # Data Transfer Objects
│   │   │   │               ├── models/         # JPA Entity classes
│   │   │   │               ├── repositories/   # Data access layer
│   │   │   │               ├── security/       # JWT & Auth utilities
│   │   │   │               ├── services/       # Business logic layer
│   │   │   │               └── utils/          # Helper utilities
│   │   │   └── resources/
│   │   │       ├── application.properties      # App configuration
│   │   │       └── static/                     # Static resources
│   │   └── test/                               # Unit & integration tests
│   ├── pom.xml                                 # Maven dependencies
│   └── target/                                 # Compiled classes (ignored in git)
│
└── frontend/                                   # React app (coming soon)
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/travelus.git
   cd travelus/backend
   ```

2. **Database Setup**
   ```bash
   # Create PostgreSQL database
   createdb travelus_db
   
   # Update application.properties with your database credentials
   ```

3. **Configure Application Properties**
   ```properties
   # refer to application-sample.properties
   
   # src/main/resources/application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/travelus_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # JWT Configuration
   jwt.secret=your_jwt_secret_key
   jwt.expiration=86400000
   
   # File Upload Configuration
   spring.servlet.multipart.max-file-size=10MB
   spring.servlet.multipart.max-request-size=10MB
   ```

4. **Build and Run**
   ```bash
   # Using Maven
   mvn clean install
   mvn spring-boot:run
   ```

5. **Verify Installation**
    - Application will start on `http://localhost:8080`
    - Access Swagger UI at `http://localhost:8080/swagger-ui.html` (if configured)

## 📚 API Documentation

### Testing the APIs

Since the frontend is currently in development, you can test the APIs using:

**Swagger UI** (Recommended)
- Navigate to `http://localhost:8080/swagger-ui.html`
- Interactive API documentation with testing capabilities

**Postman**
- Import the API collection
- Base URL: `http://localhost:8080/api`

### Key Endpoints

```
Authentication:
POST /api/auth/register - User registration
POST /api/auth/login - User login

Groups:
GET /api/groups - Get user's groups
POST /api/groups - Create new group
GET /api/groups/{id} - Get group details
POST /api/groups/{id}/invite - Generate invite link

Expenses:
GET /api/groups/{groupId}/expenses - Get group expenses
POST /api/groups/{groupId}/expenses - Add new expense
PUT /api/expenses/{id} - Update expense

Itinerary:
GET /api/groups/{groupId}/itinerary - Get group itinerary
POST /api/groups/{groupId}/itinerary - Add itinerary item
```

## 🔮 Future Enhancements

- **Google Maps Integration**: Location tagging and directions
- **AI-Powered Suggestions**: Smart itinerary recommendations using OpenAI API
- **Multi-Currency Support**: International travel expense tracking
- **Offline Capabilities**: Progressive Web App (PWA) functionality
- **Mobile App**: Native iOS and Android applications
- **Advanced Analytics**: Travel statistics and insights

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


⭐ If you found this project helpful, please give it a star!