create table books (
	id SERIAL PRIMARY KEY,
	title VARCHAR(100) NOT NULL UNIQUE,
	author VARCHAR(50) NOT NULL,
	category VARCHAR(30) NOT NULL,
	isbn VARCHAR(13) NOT NULL UNIQUE,
	publication_year VARCHAR(4) NOT NULL,
	number_of_pages VARCHAR(4) NOT NULL,
	price NUMERIC(5,2) NOT NULL,
	quantity INTEGER NOT NULL,
	created_date TIMESTAMP(6) DEFAULT current_timestamp(6),
	updated_date TIMESTAMP(6) DEFAULT current_timestamp(6)
);