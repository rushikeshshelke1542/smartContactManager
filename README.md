# Smart Contact Manager

Smart Contact Manager is a web application built using Spring Boot that allows users to manage their contacts. It provides various features like contact search, editing, deletion, uploading profile pictures, and password management. The application also includes a forgot password module to help users recover their accounts.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- User Registration and Login: Users can create an account and log in securely to the application.
- Forgot Password: Users can reset their passwords using the email verification process.
- Contact Management: Users can view, add, update, and delete their contacts.
- Profile Picture Upload: Users can upload and change their profile pictures.
- Password Change: Users can change their account passwords securely.

## Prerequisites

Before running the Smart Contact Manager application, ensure you have the following installed:

- Java 17 or higher
- MySQL database

## Installation

1. Clone the repository:
https://github.com/rushikeshshelke1542/smartContactManager
2. Import the project into your preferred IDE (Eclipse, IntelliJ, etc.).

3. Update the database configuration in `application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_contact_manager_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```
4. Do the SMTP Server Configuration

##Technologies Used

Spring Boot
Spring Security
Spring Data JPA
Thymeleaf (for server-side HTML rendering)
MySQL (Database)
JavaMail (for email verification)

