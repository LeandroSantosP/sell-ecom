
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


CREATE TABLE IF NOT EXISTS products (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   city VARCHAR(200),
   birthday DATE,
   image_ref VARCHAR(150)
);



