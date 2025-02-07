CREATE TABLE IF NOT EXISTS clients (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   city VARCHAR(200),
   birthday DATE,
   create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* 
INSERT INTO clients (id, name, email, city, birthday) VALUES 
('1fef5e47-5ab0-4391-b0a0-49592e977578', 
'João Pereira', 'joao@exemplo.com.br', 'Rio de Janeiro', '1991-02-04'); */

CREATE TABLE IF NOT EXISTS brands (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   img_ref VARCHAR(150) 
);

CREATE TABLE IF NOT EXISTS products (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   price DECIMAL(10,2),
   stock INTEGER DEFAULT 0, 
   image_ref VARCHAR(150),
   brand_id VARCHAR(150),
   FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE SET NULL
);

-- ✅ Insert a product without a brand_id (NULL)/* 

/* 
INSERT INTO products (id, name, price, stock, image_ref, brand_id) VALUES
('284791a5-5a40-4a31-a60c-d2df68997569', 'iPhone 16 Pro Apple (256GB) - Black Steel', 9800.99, 20, 'http://img.com/2324', NULL); 
 */