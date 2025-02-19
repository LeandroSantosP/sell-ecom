CREATE TABLE IF NOT EXISTS clients (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   city VARCHAR(200),
   birthday DATE
);

ALTER TABLE
   clients
ADD
   COLUMN create_at DATETIME DEFAULT (NOW());

CREATE TABLE IF NOT EXISTS brands (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   img_ref VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS products (
   id VARCHAR(150) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   price REAL,
   stoke INTEGER DEFAULT 0,
   image_ref VARCHAR(150)
);

ALTER TABLE
   products
ADD
   COLUMN brand_id VARCHAR(150) NOT NULL
AFTER
   stoke;

ALTER TABLE
   products
ADD
   FOREIGN KEY (brand_id) REFERENCES brands(id);

CREATE TABLE IF NOT EXISTS orders (
   id VARCHAR(150) PRIMARY KEY,
   total REAL NOT NULL,
   client_id VARCHAR(150) NOT NULL,
   FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE ON UPDATE CASCADE,
   created_at VARCHAR(150) NOT NULL
);

ALTER TABLE
   orders
ADD
   COLUMN client_email VARCHAR(150) NOT NULL;

ALTER TABLE
   orders
ADD
   COLUMN status VARCHAR(150) NOT NULL
AFTER
   total;

ALTER TABLE
   orders
MODIFY
   COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE
   orders
MODIFY
   COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP
AFTER
   client_email;

CREATE TABLE IF NOT EXISTS order_items (
   id VARCHAR(150) PRIMARY KEY,
   unity_price REAL NOT NULL,
   quantity INTEGER NOT NULL,
   order_id VARCHAR(150) NOT NULL,
   product_id VARCHAR(150) NOT NULL,
   FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS coupons (
   code VARCHAR(150) UNIQUE PRIMARY KEY,
   percentage INTEGER NOT NULL,
   usage_limit INTEGER NOT NULL DEFAULT 1,
   is_available BOOLEAN NOT NULL DEFAULT 1,
   expired_at DATETIME NOT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE
   orders
ADD
   COLUMN coupon VARCHAR(150);

ALTER TABLE
   orders
ADD
   FOREIGN KEY (coupon) REFERENCES coupons(code);

CREATE TABLE IF NOT EXISTS users (
   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   username VARCHAR(150) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   created_at DATE DEFAULT (NOW())
);

ALTER TABLE
   clients
ADD
   FOREIGN KEY (fk_email) REFERENCES users(email);

ALTER TABLE
   users
ADD
   COLUMN roles JSON NOT NULL;

CREATE TABLE IF NOT EXISTS roles (
   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(50) UNIQUE NOT NULL
);