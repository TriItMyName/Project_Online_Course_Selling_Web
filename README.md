# Project_Online_Course_Selling_Web

## Backend Description

Backend is built with Spring Boot and exposes REST APIs for an online course selling platform. The system handles authentication, course catalog management, ordering, payment integration, and learning progress tracking.

### Core Technologies

- Java 17
- Spring Boot (Web MVC, Data JPA, Security)
- MySQL
- JWT-based authentication
- Maven
- MoMo payment gateway integration

### Main Backend Modules

- Authentication and authorization
	- User register/login APIs
	- JWT token generation and validation
	- Role model for Admin and Student flows
- Course domain
	- Category management
	- Course management
	- Lesson management and ordered lesson retrieval
- Learning lifecycle
	- Enrollment management per user/course
	- Lesson progress persistence (watched percentage, completion state, last watched time)
- Commerce and payment
	- Order and order-detail management
	- MoMo payment create/callback/IPN handling
- Administration support
	- User, role, permission, and role-permission modules
	- Dashboard-oriented aggregate/count APIs for admin pages

### Architecture Overview

- Layered structure: Controller -> Service -> Repository -> Model/DTO
- Persistence with Spring Data JPA repositories
- Security and JWT utilities under dedicated config package
- Environment-based configuration for JWT and MoMo secrets in application properties

### API Scope (Representative)

- /api/auth/*
- /api/categories/*
- /api/courses/*
- /api/lessons/*
- /api/enrollments/*
- /api/lesson-progress/*
- /api/orders/*
- /api/order-details/*
- /api/payment/momo/*

### Portfolio/CV Summary (Suggested)

Designed and implemented a Spring Boot backend for an online course marketplace, including JWT authentication, role-based access support, course and lesson management, order processing, MoMo payment callback handling, and per-lesson learning progress tracking with MySQL persistence.