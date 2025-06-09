# Library Management System

A JavaFX application for managing a library's books and members. The system allows librarians to manage books, register members, and handle book borrowing and returns.

## Features

- Book Management
  - Add new books
  - Remove books
  - View available books
  - Search books

- Member Management
  - Register new members
  - View member details
  - Search members

- Borrowing System
  - Borrow books (up to 3 per member)
  - Return books
  - View borrowed books
  - Track due dates

## Requirements

- Java 17 or higher
- MySQL 8.0 or higher
- Maven

## Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/e-library.git
cd e-library
```

2. Create the database:
```bash
mysql -u root -p < src/main/resources/database/schema.sql
```

3. Update database connection settings in `src/main/java/com/library/util/DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

4. Build the project:
```bash
mvn clean package
```

5. Run the application:
```bash
mvn javafx:run
```

## Default Login

- Username: admin
- Password: admin

## Usage

1. Login with librarian credentials
2. Use the main menu to navigate between different functions:
   - Manage Books: Add, remove, and search books
   - Manage Members: Register and search members
   - Borrow Books: Process book borrowing
   - Return Books: Process book returns

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

Use case diagram
![image](https://github.com/user-attachments/assets/2559577d-28c8-4cf1-84ce-bf340a9f3ab1)

class diagram
![image](https://github.com/user-attachments/assets/42ff8fa3-abb1-47e1-96dc-e2dd8b4cb132)



## License

This project is licensed under the MIT License - see the LICENSE file for details. 
