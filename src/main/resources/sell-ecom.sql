
CREATE TABLE IF NOT EXISTS clients (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   city VARCHAR(200),
   birthday DATE
);

ALTER TABLE clients ADD COLUMN create_at DATETIME DEFAULT (NOW());

INSERT INTO clients (id, name, email, city, birthday) VALUES 
('1fef5e47-5ab0-4391-b0a0-49592e977578', 
'João Pereira', 'joao@exemplo.com.br', 'Rio de Janeiro', '1999-02-06');


CREATE TABLE brands (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   img_ref VARCHAR(150) 
) 


CREATE TABLE IF NOT EXISTS products (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   price REAL,
   stoke INTEGER DEFAULT 0, 
   image_ref VARCHAR(150)
);

ALTER TABLE products ADD COLUMN brand_id VARCHAR(150) NOT NULL;

ALTER TABLE products ADD FOREIGN KEY (brand_id) REFERENCES brands(id); 

INSERT INTO products (id, name, price, stoke, image_ref) VALUES
('284791a5-5a40-4a31-a60c-d2df68997569', 'Iphone 16 Pro Apple (256G) - Black Steal', 9800.99, 20, "http://img.com/2324");


SELECT name FROM products WHERE ID = "284791a5-5a40-4a31-a60c-d2df68997569";
