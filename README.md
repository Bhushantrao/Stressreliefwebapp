# StressLess – Stress Relief & Mental Wellness Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17.3-red.svg)](https://angular.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue.svg)](https://www.mysql.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)](https://getbootstrap.com/)
[![JWT](https://img.shields.io/badge/Security-JWT%20%2B%20BCrypt-orange.svg)](https://jwt.io/)

A full-stack web application engineered to help students and working professionals manage everyday stress through guided breathing exercises, meditation, ambient soundscapes, daily mood tracking, private reflective journaling, and personalized wellness goals.

---

## Table of Contents
1. [Architecture & Technology Stack](#1-architecture--technology-stack)
2. [Project Structure](#2-project-structure)
3. [Key Features](#3-key-features)
4. [Prerequisites](#4-prerequisites)
5. [Database Setup (MySQL)](#5-database-setup-mysql)
6. [Backend Setup & Execution (Spring Boot)](#6-backend-setup--execution-spring-boot)
7. [Frontend Setup & Execution (Angular)](#7-frontend-setup--execution-angular)
8. [Default User & Admin Accounts](#8-default-user--admin-accounts)
9. [REST API Documentation & Endpoints](#9-rest-api-documentation--endpoints)
10. [Postman API Testing](#10-postman-api-testing)
11. [Security & Privacy Guarantees](#11-security--privacy-guarantees)
12. [Troubleshooting & FAQs](#12-troubleshooting--faqs)

---

## 1. Architecture & Technology Stack

```
+-------------------------------------------------------------------------+
|                         Angular 17 SPA (Client)                         |
|   Bootstrap 5 + Bootstrap Icons | Glassmorphic Theme | Dark Mode Toggle |
|   Components | Services | HTTP Interceptors | Route Guards (JWT Auth)   |
+------------------------------------+------------------------------------+
                                     |  REST APIs (JSON over HTTP)
                                     v
+-------------------------------------------------------------------------+
|                       Spring Boot 3.3.5 (Port 8081)                     |
|  Controllers -> Services -> Repositories -> Entities (Layered Pattern)  |
|  Spring Security 6 + JJWT Filter | BCrypt Hashing | Global Exceptions   |
+------------------------------------+------------------------------------+
                                     |  Spring Data JPA (Hibernate)
                                     v
+-------------------------------------------------------------------------+
|                            MySQL 8.0+ Database                          |
|         Users, Roles, Assessments, Moods, Journals, Meditations         |
+-------------------------------------------------------------------------+
```

- **Frontend:** Angular 17.3, TypeScript 5.4, HTML5, Vanilla CSS Design System with CSS variables and glassmorphism, Bootstrap 5.3, Bootstrap Icons 1.11.
- **Backend:** Java 17+, Spring Boot 3.3.5, Spring Data JPA, Spring Security, JJWT 0.11.5, Jakarta Validation.
- **Database:** MySQL 8.0+ / 9.x (`stressless_db`).
- **Audio Processing:** HTML5 Audio streaming + Web Audio API synthesizer fallback (white noise, brown noise, pink noise, and 432 Hz sine tone generator).

---

## 2. Project Structure

```
stressless/
├── backend/                              # Spring Boot Application
│   ├── pom.xml                           # Maven dependencies (JJWT, JPA, Validation, MySQL)
│   └── src/main/
│       ├── java/com/stressless/
│       │   ├── StressLessApplication.java # Spring Boot main entry
│       │   ├── config/                   # SecurityConfig, CorsConfig, DataInitializer
│       │   ├── controller/               # 11 REST Controllers
│       │   ├── dto/                      # Request & Response DTOs
│       │   ├── entity/                   # 11 JPA Entities
│       │   ├── exception/                # Centralized Exception Handler & Custom Exceptions
│       │   ├── repository/               # 10 Spring Data JPA Repositories
│       │   ├── security/                 # JJWT token provider, AuthTokenFilter, UserDetails
│       │   └── service/                  # Business logic services
│       └── resources/
│           └── application.properties    # MySQL connection & server configuration (Port 8081)
│
├── database/                             # Database Scripts
│   ├── schema.sql                        # DDL for tables, constraints, foreign keys
│   └── seed_data.sql                     # Initial seed for users, quotes, meditations, sounds
│
├── frontend/                             # Angular 17 Application
│   ├── package.json                      # Angular & Bootstrap dependencies
│   ├── angular.json                      # Build & workspace configuration
│   └── src/
│       ├── index.html                    # HTML entry with Google Fonts (Outfit & Plus Jakarta)
│       ├── styles.css                    # Calming design system, themes, animations
│       └── app/
│           ├── components/               # Navbar, Footer
│           ├── guards/                   # AuthGuard, AdminGuard
│           ├── interceptors/             # AuthInterceptor (Bearer token injection)
│           ├── models/                   # TypeScript interfaces
│           ├── pages/                    # 11 Feature Pages:
│           │   ├── landing/              # Hero showcase & wellness pillars
│           │   ├── login/                # Quick demo login buttons
│           │   ├── register/             # User sign-up with validation
│           │   ├── dashboard/            # Overview, stats, daily quote
│           │   ├── assessment/           # Stress questionnaire with disclaimer
│           │   ├── breathing/            # Animated orb, audio chime, box breathing
│           │   ├── meditation/           # Catalog, audio streaming, player
│           │   ├── music/                # Ambient soundscapes & volume mixer
│           │   ├── mood/                 # Daily mood log, trend analytics
│           │   ├── journal/              # Private reflective journal
│           │   ├── progress/             # Streak counter, 7-day bar heatmap, goals
│           │   ├── profile/              # User settings & password change
│           │   └── admin/                # Platform management & metrics
│           └── services/                 # Angular Injectable HTTP Services
│
└── postman/                              # API Testing
    └── StressLess_API_Collection.json    # Complete Postman collection with test scripts
```

---

## 3. Key Features

### 🌟 1. User Registration & Role-Based Authentication
- Secure registration and login using Spring Security and JSON Web Tokens (JJWT).
- Passwords securely hashed with BCrypt.
- Role-based access control (`ROLE_USER`, `ROLE_ADMIN`) with Angular route guards (`AuthGuard`, `AdminGuard`).
- Quick demo one-click login buttons on the login page for rapid evaluation.

### 🧘 2. Personalized Dashboard
- Welcomes user dynamically by name and current time of day.
- Displays the daily inspirational quote, active streak counter, and current mood status.
- Quick action hubs to immediately start breathing exercises, listen to soundscapes, or write in the journal.
- Real-time weekly activity metrics feed.

### 📋 3. Stress Assessment & Self-Reflection
- Validated questionnaire measuring perceived stress, workload pressures, sleep quality, and physical tension.
- Computes a wellness score (0–100) and categorizes stress as **Low**, **Moderate**, or **High**.
- Provides tailored wellness recommendations (breathing routines, meditations, break reminders).
- **Mandatory Medical Disclaimer:** Prominently highlights that the questionnaire is designed for self-reflection and personal awareness, not medical or clinical diagnosis.

### 💨 4. Guided Breathing Exercises
- Multi-technique support:
  - **Box Breathing (4-4-4-4):** Inhale 4s -> Hold 4s -> Exhale 4s -> Rest 4s.
  - **Slow-Paced Breathing (4-6):** Inhale 4s -> Exhale 6s.
  - **4-7-8 Relaxing Breath:** Inhale 4s -> Hold 7s -> Exhale 8s.
- Serene expanding and contracting animated glowing orb synchronized with phase transitions.
- High-quality audio chime bell indicator on phase changes (built with Web Audio API synthesis so no external audio downloads fail).
- Adjustable timer (1 to 15 minutes) with Start, Pause, Resume, and Stop controls.
- Automatically saves completed sessions to the database to update streaks and metrics.

### 🎧 5. Guided Meditation & Mindfulness
- Browse categorized meditation sessions: *Stress Relief*, *Sleep & Wind-down*, *Focus & Clarity*, *Anxiety Calm*.
- Interactive audio player with progress slider, time display, play/pause, and completion confirmation.
- Tracks completed sessions in the user's history.

### 🌧️ 6. Relaxing Sounds & Ambient Audio Mixer
- Nature soundscapes: *Gentle Rainfall*, *Ocean Waves*, *Deep Forest*, *Campfire Warmth*, *Soft Piano*, *432 Hz Calm Drone*.
- Multi-track volume mixer allowing users to blend soundscapes together.
- Built-in Web Audio API synthesizer for instant audio playback without relying on external CDNs.

### 😊 7. Daily Mood Tracker & Analytics
- Log daily mood (*Happy*, *Calm*, *Neutral*, *Anxious*, *Sad*) with 1–5 energy level slider and optional reflection notes.
- Visual mood distribution progress bars showing percentage breakdown.
- Weekly and monthly mood trend analysis with psychological insights based on dominant moods.

### 📖 8. Personal Reflective Journal (Strictly Private)
- Create, view, search, edit, and delete personal journal entries.
- Filter entries by keyword search or mood tag.
- **Privacy Assurance:** Each user's journal is completely isolated. Even platform administrators cannot view individual user journal entries.

### 📈 9. Wellness Progress & Personal Goals
- Streak counter tracking consecutive days of wellness activities.
- 7-day activity bar heatmap (breathing + meditation).
- Personal wellness goals management: set targets (e.g., 30 Days of Box Breathing), increment progress, and mark as completed.

### 🛡️ 10. Administrator Module
- Platform overview dashboard displaying user counts, completed assessments, and activity statistics.
- Content Management System (CMS): Add, edit, or delete motivational quotes, guided meditations, and soundscapes.
- User directory with role inspection (protects private user journal data).

---

## 4. Prerequisites

Before running the application on Windows, verify that you have installed:
1. **Java Development Kit (JDK) 17 or higher**
   ```powershell
   java -version
   ```
2. **Apache Maven 3.8+**
   ```powershell
   mvn -version
   ```
3. **Node.js 18+ and npm**
   ```powershell
   node -v
   npm -v
   ```
4. **MySQL Server 8.0+**
   Verify MySQL service is running in Windows Services (`services.msc`) or:
   ```powershell
   Get-Service -Name "*mysql*"
   ```

---

## 5. Database Setup (MySQL)

### Step 1: Log into MySQL
Open PowerShell or MySQL Command Line Client:
```powershell
mysql -u root -p
```
*(Enter your MySQL root password when prompted)*

### Step 2: Create Database and Apply Schema
Run the following SQL commands or execute the provided scripts:
```sql
CREATE DATABASE IF NOT EXISTS stressless_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stressless_db;
```

Execute the schema file:
```powershell
mysql -u root -p stressless_db < "d:\MCA\projects\stressless\database\schema.sql"
```

Execute the seed data file:
```powershell
mysql -u root -p stressless_db < "d:\MCA\projects\stressless\database\seed_data.sql"
```

### Step 3: Verify Tables
```sql
USE stressless_db;
SHOW TABLES;
```
You will see 11 tables:
- `users`, `roles`, `user_roles`
- `motivational_quotes`
- `stress_assessments`
- `mood_entries`
- `journal_entries`
- `meditation_sessions`, `user_meditation_completions`
- `breathing_sessions`
- `relaxation_resources`
- `wellness_goals`

---

## 6. Backend Setup & Execution (Spring Boot)

### Configuration
Open `backend/src/main/resources/application.properties` and verify your MySQL credentials:
```properties
# Server Configuration (Port 8081 avoids conflicts with Apache on port 8080)
server.port=8081

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/stressless_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root

# Hibernate JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
stressless.jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
stressless.jwt.expiration-ms=86400000
```
> **Note:** If your MySQL root password is not `root`, update `spring.datasource.password` accordingly.

### Compile and Run
Open PowerShell in the `backend` directory:
```powershell
cd d:\MCA\projects\stressless\backend
mvn clean compile
mvn spring-boot:run
```
When the backend starts successfully, you will see:
```
Tomcat started on port 8081 (http) with context path '/'
Started StressLessApplication in X.XXX seconds
```
Test that the backend is responding:
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/quotes/daily"
```

---

## 7. Frontend Setup & Execution (Angular)

Open a new PowerShell window in the `frontend` directory:
```powershell
cd d:\MCA\projects\stressless\frontend
npm install
npm start
```
*(Or use `npx ng serve --port 4200`)*

Open your browser and navigate to:
👉 **`http://localhost:4200`**

---

## 8. Default User & Admin Accounts

The database comes pre-seeded with two active accounts for immediate evaluation:

| Role | Email | Password | Permissions |
| :--- | :--- | :--- | :--- |
| **User (Student/Professional)** | `alex@example.com` | `User@123` | Full access to Dashboard, Assessment, Breathing, Meditation, Music, Mood, Journal, Goals |
| **Administrator** | `admin@stressless.com` | `Admin@123` | Full access to User features + Admin Content CMS, User Directory, Platform Metrics |

> **Pro Tip:** On the Login page (`http://localhost:4200/login`), click either the **"Login as Demo User"** or **"Login as Demo Admin"** button for instant one-click login without typing!

---

## 9. REST API Documentation & Endpoints

All protected endpoints require an `Authorization: Bearer <JWT>` header.

### 1. Authentication (`/api/auth`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Public | Register new user account |
| `POST` | `/api/auth/login` | Public | Authenticate user & return JWT token |

### 2. Motivational Quotes & Dashboard (`/api/quotes`, `/api/dashboard`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/quotes/daily` | Public | Get active daily inspirational quote |
| `GET` | `/api/quotes` | Public | Get all motivational quotes |
| `GET` | `/api/dashboard/stats` | User | Get personalized dashboard metrics & activities |

### 3. Stress Assessment (`/api/assessments`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/assessments` | User | Submit 10-item assessment; returns score & category |
| `GET` | `/api/assessments/latest` | User | Get most recent assessment result |
| `GET` | `/api/assessments/history` | User | Get user's assessment history |

### 4. Guided Breathing (`/api/breathing`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/breathing/sessions` | User | Log completed breathing session & feelings |
| `GET` | `/api/breathing/history` | User | Get completed breathing sessions |

### 5. Meditation Sessions (`/api/meditations`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/meditations` | Public | Browse meditation catalog (filters: `category`, `search`) |
| `GET` | `/api/meditations/{id}` | Public | Get meditation details |
| `POST` | `/api/meditations/{id}/complete` | User | Record meditation completion |
| `GET` | `/api/meditations/history` | User | Get user's completed meditations |

### 6. Relaxing Sounds (`/api/music`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/music/resources` | Public | List ambient soundscapes & nature tracks |
| `GET` | `/api/music/resources/{id}` | Public | Get soundscape details |

### 7. Mood Tracker (`/api/mood`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/mood` | User | Record daily mood, energy level (1-5), and note |
| `GET` | `/api/mood/today` | User | Get today's recorded mood |
| `GET` | `/api/mood/history` | User | List past mood entries |
| `GET` | `/api/mood/trends` | User | Get mood distribution counts, avg energy, insight |

### 8. Personal Journal (`/api/journal`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/journal` | User | Create a new private journal entry |
| `GET` | `/api/journal` | User | List user's entries (filters: `search`, `moodTag`) |
| `GET` | `/api/journal/{id}` | User | Get entry by ID (strictly isolated to owner) |
| `PUT` | `/api/journal/{id}` | User | Update entry |
| `DELETE` | `/api/journal/{id}` | User | Delete entry |

### 9. Wellness Progress & Goals (`/api/progress`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/progress/report` | User | Streak, total minutes, 7-day activity timeline |
| `GET` | `/api/progress/goals` | User | List user's wellness goals |
| `POST` | `/api/progress/goals` | User | Create a new wellness goal |
| `PATCH` | `/api/progress/goals/{id}/progress`| User | Update goal progress or mark completed |
| `DELETE` | `/api/progress/goals/{id}` | User | Delete goal |

### 10. User Profile (`/api/profile`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/profile` | User | Get current user's profile |
| `PUT` | `/api/profile` | User | Update name, bio, and avatar |
| `PUT` | `/api/profile/password` | User | Change user password |

### 11. Administrator Operations (`/api/admin`)
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/stats` | Admin | Platform metrics (users, sessions, assessments) |
| `GET` | `/api/admin/users` | Admin | List registered user directory |
| `POST` | `/api/admin/quotes` | Admin | Create motivational quote |
| `PUT` | `/api/admin/quotes/{id}` | Admin | Update motivational quote |
| `DELETE` | `/api/admin/quotes/{id}` | Admin | Delete motivational quote |
| `POST` | `/api/admin/meditations` | Admin | Create guided meditation session |
| `PUT` | `/api/admin/meditations/{id}`| Admin | Update meditation session |
| `DELETE` | `/api/admin/meditations/{id}`| Admin | Delete meditation session |
| `POST` | `/api/admin/music` | Admin | Create ambient soundscape |
| `PUT` | `/api/admin/music/{id}` | Admin | Update soundscape |
| `DELETE` | `/api/admin/music/{id}` | Admin | Delete soundscape |

---

## 10. Postman API Testing

A complete Postman collection is included in:
👉 `postman/StressLess_API_Collection.json`

### How to Import & Run in Postman:
1. Open **Postman**.
2. Click **Import** (top left) -> choose `d:\MCA\projects\stressless\postman\StressLess_API_Collection.json`.
3. The collection **"StressLess Platform REST APIs"** will appear with all 11 folders.
4. Execute **"1. Authentication -> Login - Regular User (Alex)"**:
   - The test script will automatically capture the returned JWT and store it in `{{token}}`.
5. Execute **"1. Authentication -> Login - Administrator"**:
   - The test script stores the admin JWT in `{{adminToken}}`.
6. Now run any request in the collection — authenticated requests automatically include the Bearer token!

---

## 11. Security & Privacy Guarantees

1. **Password Protection:** Plain-text passwords are never saved. All passwords undergo salted BCrypt hashing.
2. **Stateless JWT Authorization:** Client requests transmit signed JWTs in the standard `Authorization: Bearer <token>` header, verified by `AuthTokenFilter`.
3. **Data Isolation (HIPAA/Privacy Principles):**
   - Journal entries and individual mood notes are scoped strictly to the authenticated `User` entity via `findByUser(currentUser)`.
   - Even administrators cannot view, search, or export another user's journal entries.
4. **Assessment Educational Disclaimer:**
   - The stress assessment is explicitly presented as a self-reflection questionnaire and displays a notice stating it is not a clinical medical diagnosis.

---

## 12. Troubleshooting & FAQs

### Q: Port 8080 is already in use by another application.
**A:** We deliberately configured the Spring Boot backend to run on **port 8081** in `application.properties` to avoid conflict with Apache or other local web servers. The frontend environment is already configured to communicate with `http://localhost:8081/api`.

### Q: Audio fails to play if an external link is blocked.
**A:** The platform includes an intelligent Web Audio API fallback engine. Even if external audio URLs fail or you are offline, sound generation (white noise, brown noise, pink noise, 432 Hz drone) and breathing bell chimes will play smoothly using the browser's native audio synthesizer.

### Q: How do I toggle Dark Mode?
**A:** Click the Moon/Sun icon in the navigation bar at the top right of any page. Your preference is persisted in browser local storage.

---

*StressLess Platform – Empowering balanced minds and calm days.*
