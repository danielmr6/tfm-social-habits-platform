# Social Habits Platform

Web platform designed for monitoring healthy habits in vulnerable groups, oriented to social organizations and professionals who perform follow-up activities.

The platform allows professionals to register users, monitor healthy habits, record observations, detect warning situations and centralize information related to user evolution.

---

# Features

## Authentication & Security

- Secure login system
- JWT authentication
- Protected endpoints
- Role-based access

## User Management

- Create vulnerable users
- Edit user information
- Delete users
- Search and pagination
- Detailed user profiles

## Habit Tracking

- Register healthy habits
- Visualize habit history
- Habit status calculation
- Automatic alerts for missing records
- Delete individual habits

## Professional Follow-up

- Register observations
- Track user evolution
- View historical information

## Alerts & Monitoring

- Missing habit detection
- Warning states
- Critical situations visualization
- Automatic status calculation

---

# Tech Stack

## Backend

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Maven
- PostgreSQL / MySQL
- JPA / Hibernate

## Frontend

- React
- React Router
- Axios
- CSS Modules

---

# Project Structure

Backend:

```
backend/
├── controllers/
├── services/
├── repositories/
├── entities/
├── dto/
├── security/
└── config/
```

Frontend:

```
frontend/
├── pages/
├── components/
├── services/
├── routes/
└── styles/
```

---

# Installation

## Clone repository

```bash
git clone https://github.com/danielmr6/tfm-social-habits-platform.git

cd tfm-social-habits-platform
```

## Backend Setup

```bash
cd backend

mvn clean install

mvn spring-boot:run
```

Backend runs at:

```
http://localhost:8080
```

## Frontend Setup

```bash
cd frontend

npm install

npm start
```

Frontend runs at:

```
http://localhost:3000
```

---

# Environment Variables

Backend:

```env
DATABASE_URL=
DATABASE_USERNAME=
DATABASE_PASSWORD=

JWT_SECRET=
JWT_EXPIRATION=
```

Frontend:

```env
REACT_APP_API_URL=http://localhost:8080
```

---

# Main Functionalities

- Professional authentication
- User registration
- Habit registration
- Habit monitoring
- Observation tracking
- Alerts generation
- User status visualization
- User management dashboard

---

# Agile Methodology

The project follows an incremental development process inspired by agile methodologies:

- Sprint-based iterations
- Story Point estimations
- Product Backlog organization
- Incremental delivery
- Continuous refinement

This approach allows progressive validation and adaptation during development.

---

# Future Improvements

- Reports generation
- Statistics dashboard
- Notifications system
- Mobile version
- Advanced analytics
- Multi-organization support

---

# Author

Daniel Morón Roces

Master Thesis Project

Universidad Internacional de La Rioja (UNIR)