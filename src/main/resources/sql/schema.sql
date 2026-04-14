CREATE TABLE IF NOT EXISTS department (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    open_time TIME NOT NULL,
    close_time TIME NOT NULL
);

CREATE TABLE IF NOT EXISTS product (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(12, 2) NOT NULL,
    CONSTRAINT fk_product_department
        FOREIGN KEY (department_id)
        REFERENCES department(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_product_department_id ON product(department_id);