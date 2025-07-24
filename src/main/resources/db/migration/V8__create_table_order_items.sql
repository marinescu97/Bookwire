CREATE TABLE order_items(
    id SERIAL PRIMARY KEY,
    book_id INT NOT NULL REFERENCES books(id),
    order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    quantity INT NOT NULL
);