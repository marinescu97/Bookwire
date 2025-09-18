# 📚 Bookwire

Bookwire is a demo backend application that simulates the behavior of an online bookstore.  
The project demonstrates expertise in building robust **Spring Boot** applications while following **SOLID principles**, **clean code practices**, and **RESTful API design**.

## 🚀 Key Features

- **📖 Book Management**
    - **Admin**: Add, update, delete books
    - **User & Admin**: View books

- **🔍 Search & Filtering**: Search by title, author, or category, and filter results.

- **🛒 Order Placement**
    - **User**: Place orders and update the database

- **🔐 Authentication & Authorization**: Secure login and role-based access using JWT.

- **🧪 Testing**: Unit and integration tests are implemented to ensure code quality and reliability.

## 👥 Actors & Permissions

| Actor | Permissions |
|-------|-------------|
| **Admin** | Add, update, delete, and view books |
| **User** | View books, place orders |

## 🛠️ Technologies & Principles

- **💻 Backend**: Spring Boot, Java, Maven
- **🗄️ Database**: PostgreSQL
- **🛠️ Database Migrations**: Flyway
- **📄 API Documentation**: Swagger / OpenAPI
- **🔒 Security**: JWT authentication & authorization
- **🎯 Design Principles**: SOLID, clean code, RESTful API

## 🌐 REST API

The application exposes a **REST API** fully documented with Swagger. Key endpoints include:

- `/api/admin/books` – book management
- `/api/orders` – order placement and management
- `/api/auth` – user authentication and registration

**Swagger UI is available at:** http://localhost:8080/swagger-ui/index.html#/

## 📁 Project Structure

- [`src/main/java`](src/main/java) – application source code
- [`src/main/resources`](src/main/resources) – configurations, SQL scripts, Flyway migrations
- [`src/test/java`](src/test/java) – unit and integration tests
- [`pom.xml`](pom.xml) – Maven dependencies

## ⚡ Running the Application

1. **Clone the project**
    ```bash
    git clone https://github.com/marinescu97/Bookwire.git
    cd Bookwire
   
2. **Configure PostgreSQL**  
   Modify application.properties with your database credentials:

    ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/bookwire
   spring.datasource.username=<username>
   spring.datasource.password=<password>
   
3. **Run the application**

    ```bash
   mvn spring-boot:run

**The application will be available at** http://localhost:8080.

## 💡 Notes
- Flyway handles database migrations automatically.
- JWT protects endpoints and ensures controlled access.
- Code is designed following SOLID principles and clean code practices for maintainability and scalability.
- Unit and integration tests ensure correctness and reliability of core functionality.

## 📜 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

You are free to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of this project, under the following conditions:

- The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
- The software is provided "as is", without warranty of any kind.