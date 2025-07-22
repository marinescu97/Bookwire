create table users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(8) NOT NULL,
    address_id INT,
    created_date TIMESTAMP(6) DEFAULT current_timestamp(6),
    updated_date TIMESTAMP(6) DEFAULT current_timestamp(6)
);