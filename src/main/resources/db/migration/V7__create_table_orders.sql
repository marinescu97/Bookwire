CREATE TABLE orders(
    id SERIAL PRIMARY KEY,
    status VARCHAR(10) NOT NULL,
    customer_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_date TIMESTAMP(6) DEFAULT current_timestamp(6),
    updated_date TIMESTAMP(6) DEFAULT current_timestamp(6)
);