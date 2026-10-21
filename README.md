StressLess – Stress Relief & Mental Wellness Platform
A full-stack wellness application that helps students and professionals manage everyday stress through breathing exercises, meditation, relaxing sounds, mood tracking, private journaling, and wellness goals.
Tech Stack
- Frontend: Angular 17, TypeScript, Bootstrap 5
- Backend: Java 17+, Spring Boot 3.3.5, Spring Security, Spring Data JPA
- Database: MySQL 8+
- Authentication: JWT and BCrypt
Key Features
- Secure registration and role-based login
- Personalized dashboard and stress self-assessment
- Guided breathing and meditation sessions
- Relaxing ambient sounds
- Daily mood tracking and analytics
- Private journal entries
- Wellness progress, streaks, and goals
- Admin dashboard for content and platform management
The stress assessment is for self-reflection only and is not a medical diagnosis.

Project Structure
stressless/
├── backend/       # Spring Boot REST API
├── frontend/      # Angular application
├── database/      # SQL schema and seed data
└── postman/       # API testing collection
Prerequisites
Install Java 17+, Maven 3.8+, Node.js 18+, npm, and MySQL 8+.
Setup
1. Database
Create the database and import the SQL scripts:
CREATE DATABASE stressless_db;
mysql -u root -p stressless_db < database/schema.sql
mysql -u root -p stressless_db < database/seed_data.sql
2. Backend
Set your MySQL credentials in backend/src/main/resources/application.properties, then run:
cd backend
mvn spring-boot:run
Backend: http://localhost:8081
3. Frontend
cd frontend
npm install
npm start
Frontend: http://localhost:4200
API Testing
Import postman/StressLess_API_Collection.json into Postman to test the REST APIs.
Security & Privacy
- Passwords are hashed using BCrypt.
- JWT tokens protect authenticated endpoints.
- Journal entries are private to their owners, including from administrators.
StressLess – Empowering balanced minds and calm days.