-- Database schema for the Library Management System

-- Drop tables if they exist to ensure a clean slate on re-running the script
DROP TABLE IF EXISTS `reviews`;
DROP TABLE IF EXISTS `reservations`;
DROP TABLE IF EXISTS `borrowing_records`;
DROP TABLE IF EXISTS `books`;
DROP TABLE IF EXISTS `users`;

-- Table for users (both members and librarians)
CREATE TABLE `users` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `password_hash` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(255) NOT NULL,
    `role` ENUM('MEMBER', 'LIBRARIAN') NOT NULL,
    `registration_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for books
CREATE TABLE `books` (
    `book_id` INT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `author` VARCHAR(255) NOT NULL,
    `genre` VARCHAR(100),
    `publisher` VARCHAR(255),
    `isbn` VARCHAR(20) UNIQUE,
    `availability_status` ENUM('AVAILABLE', 'BORROWED') NOT NULL DEFAULT 'AVAILABLE',
    `average_rating` DECIMAL(3, 2) DEFAULT 0.00
);

-- Table for borrowing records
CREATE TABLE `borrowing_records` (
    `record_id` INT AUTO_INCREMENT PRIMARY KEY,
    `book_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `borrow_date` DATE NOT NULL,
    `due_date` DATE NOT NULL,
    `return_date` DATE,
    `fine_amount` DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (`book_id`) REFERENCES `books`(`book_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
);

-- Table for book reservations
CREATE TABLE `reservations` (
    `reservation_id` INT AUTO_INCREMENT PRIMARY KEY,
    `book_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `reservation_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `status` ENUM('ACTIVE', 'FULFILLED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (`book_id`) REFERENCES `books`(`book_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
);

-- Table for book ratings and reviews
CREATE TABLE `reviews` (
    `review_id` INT AUTO_INCREMENT PRIMARY KEY,
    `book_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `rating` INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    `review_text` TEXT,
    `review_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`book_id`) REFERENCES `books`(`book_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
); 