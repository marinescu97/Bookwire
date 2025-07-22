CREATE TABLE addresses(
    id SERIAL PRIMARY KEY,
    street VARCHAR(30) NOT NULL,
    zip_code VARCHAR(4) NOT NULL,
    city VARCHAR(20) NOT NULL,
    state VARCHAR(20) NOT NULL
)