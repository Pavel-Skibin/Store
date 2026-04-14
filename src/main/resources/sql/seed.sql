INSERT INTO department(name, open_time, close_time)
VALUES
    ('Groceries', '08:00', '22:00'),
    ('Electronics', '10:00', '21:00'),
    ('Books', '09:00', '20:00'),
    ('Clothes', '10:00', '22:00'),
    ('Home', '09:00', '21:00')
ON CONFLICT DO NOTHING;

INSERT INTO product(department_id, name, price)
VALUES
    (1, 'Milk', 89.90),
    (1, 'Bread', 54.00),
    (2, 'Headphones', 2990.00),
    (2, 'Mouse', 1490.00),
    (3, 'Java Core', 1200.00),
    (4, 'T-shirt', 1290.00),
    (5, 'Pan', 2190.00)
ON CONFLICT DO NOTHING;