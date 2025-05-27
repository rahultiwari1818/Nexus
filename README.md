# Nexus: Digital and Secure Library Management System

## Overview

Nexus is a modern, secure, and efficient digital library management system built using Java. It leverages **system design patterns** and adheres to the **SOLID principles of OOP** to ensure scalability, maintainability, and robustness. The system supports various user roles (Admin, Student, Faculty, Researcher) and provides features like borrowing/returning items, viewing transaction history, and managing library inventory with a focus on security and user-friendly console-based interaction.

## Features

- **Role-Based Access Control**: Different functionalities for Admin and regular users (Student, Faculty, Researcher).
- **Transaction Management**: Borrow and return library items with due date tracking.
- **Fine Management**: Calculate, pay, or waive fines for overdue items.
- **Secure Operations**: Input validation and exception handling for secure transactions.
- **History Tracking**: View current borrowings and borrowing history for users, and all transactions for admins.
- **Search Functionality**: Dynamic search for users and books (using Strategy pattern for search algorithms).
- **Scalable Design**: Built with system design patterns and SOLID principles for easy extension.

## Architecture

### System Design Patterns

Nexus incorporates several design patterns to ensure a robust architecture:

- **Singleton Pattern**: Applied in `DBConnection` to ensure a single database connection instance, optimizing resource usage.
- **Factory Pattern**: Used in potential item creation (e.g., `LibraryItem` types like PhysicalBook, EBook), allowing flexible instantiation of different item types.
- **DAO Pattern**: Implemented in classes like `TransactionDAO` and `UserDAO` to abstract database operations, promoting loose coupling between business logic and data access.
- **Facade Pattern**: The `LibraryService` class acts as a facade, providing a simplified interface for complex operations like borrowing and returning items, hiding the underlying complexity.
- **Strategy Pattern**: Used for search functionality, allowing dynamic selection of search strategies (e.g., search by title, author, or ISBN for books; search by email or role for users).
- **MVC Pattern**: Adapted for a console application where:
    - **Model**: `Transaction`, `User`, `LibraryItem` classes represent the data and business logic.
    - **View**: Console output (e.g., Formatted Tables for displaying data in tables).
    - **Controller**: `LibraryController` handles user input and orchestrates interactions between the model and view.

### SOLID Principles

The project adheres to SOLID principles for maintainable and scalable code:

- **Single Responsibility Principle (SRP)**: Each class has a single responsibility. For example, `TransactionDAO` handles database operations for transactions, while `LibraryController` manages user interactions.
- **Open/Closed Principle (OCP)**: The system is open for extension but closed for modification. New transaction types can be added by extending `Transaction` without modifying existing code.
- **Liskov Substitution Principle (LSP)**: Subtypes like `PhysicalBook` and `EBook` (if implemented) can substitute `LibraryItem` without affecting the system's behavior.
- **Interface Segregation Principle (ISP)**: Classes only implement interfaces relevant to their functionality. For example, `LibraryItemController` doesn’t force unrelated methods on its users.
- **Dependency Inversion Principle (DIP)**: High-level modules like `LibraryController` depend on abstractions (e.g., `TransactionDAO`) rather than concrete implementations, facilitating testing and flexibility.

### Database Schema

The system uses a relational database (PostgreSQL) with the following tables:

- **`users`**: Stores user details.
    - `user_id` (SERIAL, PK)
    - `first_name` (VARCHAR(50), NOT NULL)
    - `last_name` (VARCHAR(50), NOT NULL)
    - `email` (VARCHAR(100), NOT NULL, UNIQUE)
    - `password` (VARCHAR(255), NOT NULL)
    - `registration_date` (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
    - `role` (VARCHAR(20), NOT NULL, CHECK: 'Faculty', 'Student', 'Admin', 'Researcher')

- **`library_items`**: Stores library item details.
    - `item_id` (SERIAL, PK)
    - `title` (VARCHAR(200), NOT NULL)
    - `author` (VARCHAR(100))
    - `isbn` (VARCHAR(13), UNIQUE)
    - `is_available` (BOOLEAN, NOT NULL, DEFAULT TRUE)
    - `added_date` (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
    - `type` (VARCHAR(20), NOT NULL, CHECK: 'EBook', 'PhysicalBook', 'ResearchPaper', 'AudioBook')
    - `extra_param` (VARCHAR(100))

- **`transactions`**: Stores borrowing/returning records.
    - `transaction_id` (SERIAL, PK)
    - `user_id` (INTEGER, NOT NULL, FK: `users(user_id)`)
    - `item_id` (INTEGER, NOT NULL, FK: `library_items(item_id)`)
    - `transaction_type` (VARCHAR(20), NOT NULL, CHECK: 'Borrow', 'Return')
    - `transaction_date` (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
    - `due_date` (TIMESTAMP)
    - `return_date` (TIMESTAMP)
    - `status` (VARCHAR(20), NOT NULL, CHECK: 'Active', 'Completed', 'Overdue')

- **`fines`**: Stores fine details for overdue transactions.
    - `fine_id` (SERIAL, PK)
    - `transaction_id` (INT, NOT NULL, FK: `transactions(transaction_id)`, UNIQUE)
    - `user_id` (INT, NOT NULL, FK: `users(user_id)`)
    - `fine_amount` (DECIMAL(10,2), NOT NULL)
    - `fine_calculated_date` (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
    - `payment_status` (VARCHAR(20), NOT NULL, CHECK: 'Pending', 'Paid', 'Waived')
    - `payment_date` (TIMESTAMP)
    - `waived_by` (INT, FK: `users(user_id)`, nullable)
    - `waived_reason` (TEXT)

- **`fine_settings`**: Configures fine rates per user type.
    - `fine_setting_id` (SERIAL, PK)
    - `user_type` (VARCHAR(20), NOT NULL, CHECK: 'Faculty', 'Student', 'Admin', 'Researcher')
    - `fine_amt` (INT, NOT NULL)
    - `active` (BOOLEAN, NOT NULL, DEFAULT TRUE)

- **`borrowing_settings`**: Configures borrowing limits per user type.
    - `borrowing_setting_id` (SERIAL, PK)
    - `user_type` (VARCHAR(20), NOT NULL, CHECK: 'Faculty', 'Student', 'Admin', 'Researcher')
    - `borrowing_limit` (INT, NOT NULL)
    - `active` (BOOLEAN, NOT NULL, DEFAULT TRUE)

## Technologies Used

- **Java**: Core programming language.
- **JDBC**: For database connectivity.
- **PostgreSQL**: Database for storing library data.
- **Maven**: For dependency management.

## Installation and Setup

### Prerequisites

- Java 8 or higher
- PostgreSQL Server
- Maven (for dependency management)
- Git

### Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/rahultiwari1818/Nexus.git
   cd Nexus
   ```

2. **Set Up the Database**

    - Create a PostgreSQL database named `nexus_library`.
    - Run the following SQL script to set up the tables:

      ```sql
      CREATE DATABASE nexus_library;
      \c nexus_library
 
      CREATE TABLE public.users (
          user_id SERIAL PRIMARY KEY,
          first_name VARCHAR(50) NOT NULL,
          last_name VARCHAR(50) NOT NULL,
          email VARCHAR(100) NOT NULL UNIQUE,
          password VARCHAR(255) NOT NULL,
          registration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
          role VARCHAR(20) NOT NULL,
          CONSTRAINT users_role_check CHECK (role IN ('Faculty', 'Student', 'Admin', 'Researcher'))
      );
 
      CREATE TABLE public.library_items (
          item_id SERIAL PRIMARY KEY,
          title VARCHAR(200) NOT NULL,
          author VARCHAR(100),
          isbn VARCHAR(13) UNIQUE,
          is_available BOOLEAN NOT NULL DEFAULT TRUE,
          added_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
          type VARCHAR(20) NOT NULL,
          extra_param VARCHAR(100),
          CONSTRAINT library_items_type_check CHECK (type IN ('EBook', 'PhysicalBook', 'ResearchPaper', 'AudioBook'))
      );
 
      CREATE TABLE public.transactions (
          transaction_id SERIAL PRIMARY KEY,
          user_id INTEGER NOT NULL,
          item_id INTEGER NOT NULL,
          transaction_type VARCHAR(20) NOT NULL,
          transaction_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
          due_date TIMESTAMP WITHOUT TIME ZONE,
          return_date TIMESTAMP WITHOUT TIME ZONE,
          status VARCHAR(20) NOT NULL,
          CONSTRAINT transactions_status_check CHECK (status IN ('Active', 'Completed', 'Overdue')),
          CONSTRAINT transactions_transaction_type_check CHECK (transaction_type IN ('Borrow', 'Return')),
          CONSTRAINT transactions_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(user_id),
          CONSTRAINT transactions_item_id_fkey FOREIGN KEY (item_id) REFERENCES library_items(item_id)
      );
 
      CREATE TABLE public.fines (
          fine_id SERIAL PRIMARY KEY,
          transaction_id INT NOT NULL,
          user_id INT NOT NULL,
          fine_amount DECIMAL(10, 2) NOT NULL,
          fine_calculated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
          payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('Pending', 'Paid', 'Waived')),
          payment_date TIMESTAMP,
          waived_by INT,
          waived_reason TEXT,
          CONSTRAINT fk_transaction_id FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id),
          CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
          CONSTRAINT fk_waived_by FOREIGN KEY (waived_by) REFERENCES users(user_id),
          CONSTRAINT unique_transaction_fine UNIQUE (transaction_id)
      );
 
      CREATE TABLE public.fine_settings (
          fine_setting_id SERIAL PRIMARY KEY,
          user_type VARCHAR(20) NOT NULL,
          fine_amt INT NOT NULL,
          active BOOLEAN NOT NULL DEFAULT TRUE,
          CONSTRAINT fine_settings_user_type_check CHECK (user_type IN ('Faculty', 'Student', 'Admin', 'Researcher'))
      );
 
      CREATE TABLE public.borrowing_settings (
          borrowing_setting_id SERIAL PRIMARY KEY,
          user_type VARCHAR(20) NOT NULL,
          borrowing_limit INT NOT NULL,
          active BOOLEAN NOT NULL DEFAULT TRUE,
          CONSTRAINT borrowing_settings_user_type_check CHECK (user_type IN ('Faculty', 'Student', 'Admin', 'Researcher'))
      );
      ```

    - Insert sample data:

      ```sql
      INSERT INTO users (first_name, last_name, email, password, role) 
      VALUES ('Bob', 'Smith', 'bob@example.com', 'pass123', 'Student'),
             ('Alice', 'Jones', 'alice@example.com', 'pass456', 'Admin');
 
      INSERT INTO library_items (title, author, isbn, is_available, type, extra_param) 
      VALUES ('Sample Book 1', 'Author 1', '1234567890123', TRUE, 'PhysicalBook', NULL),
             ('Sample EBook', 'Author 2', '9876543210987', TRUE, 'EBook', 'http://example.com/ebook');
 
      INSERT INTO transactions (user_id, item_id, transaction_type, transaction_date, due_date, return_date, status) 
      VALUES (1, 1, 'Borrow', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '14 days', NULL, 'Active');
 
      INSERT INTO fines (transaction_id, user_id, fine_amount, payment_status) 
      VALUES (1, 1, 5.00, 'Pending');
 
      INSERT INTO fine_settings (user_type, fine_amt, active) 
      VALUES ('Student', 2, TRUE),
             ('Faculty', 1, TRUE);
 
      INSERT INTO borrowing_settings (user_type, borrowing_limit, active) 
      VALUES ('Student', 5, TRUE),
             ('Faculty', 10, TRUE);
      ```

3. **Configure Database Connection**

   Update the database connection configuration with your PostgreSQL credentials:

   ```java
   private static final String URL = "jdbc:postgresql://localhost:5432/nexus_library";
   private static final String USER = "your_username";
   private static final String PASSWORD = "your_password";
   ```

   Ensure you have the PostgreSQL JDBC driver in your `pom.xml`:

   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <version>42.7.3</version>
   </dependency>
   ```

4. **Build the Project**

   ```bash
   mvn clean install
   ```

5. **Run the Application**

   ```bash
   java -jar target/nexus-library-system-1.0-SNAPSHOT.jar
   ```

## Usage

1. **Start the Application**

   Run the main class to start the application:

   ```bash
   java -cp target/nexus-library-system-1.0-SNAPSHOT.jar com.Nexus_Library.LibraryManagementApp
   ```

2. **Interact with the System**

    - The application starts with a console-based menu.
    - Log in as a user (e.g., Bob: Student, Alice: Admin).
    - Choose options based on your role:
        - **Student/Faculty/Researcher**: Borrow/return items, view current borrowings, view history, pay fines.
        - **Admin**: View all transactions, manage library items, manage fines.

   **Sample Interaction** (as a Student):

   ```
   ===== Nexus Library - Welcome =====
   1. Login
   2. Exit
   Enter your choice: 1

   Enter email: bob@example.com
   Enter password: pass123
   Login successful! Welcome, Bob (Student)

   ===== Nexus Library - Welcome, Bob (Student) =====
   1. Borrow Book
   2. Return Book
   3. View Current Borrowings
   4. View Borrowing History
   5. Pay Fines
   6. Logout
   Enter your choice: 3

   ===== Active Transactions =====
   +----------+---------------+--------------------------+--------------------------+--------+
   | Item ID  | Item Title    | Borrowed                 | Due Date                 | Status |
   +----------+---------------+--------------------------+--------------------------+--------+
   | 1        | Sample Book 1 | 2025-05-27 19:59:00.0    | 2025-06-10 19:59:00.0    | Active |
   +----------+---------------+--------------------------+--------------------------+--------+
   ```

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit (`git commit -m "Add your feature"`).
4. Push to your branch (`git push origin feature/your-feature`).
5. Open a pull request.

Please ensure your code follows the SOLID principles and adheres to the project’s coding style.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.