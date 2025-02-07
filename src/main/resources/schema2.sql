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

INSERT INTO
   clients (id, name, email, city, birthday)
VALUES
   (
      '1fef5e47-5ab0-4391-b0a0-49592e977578',
      'João Pereira',
      'joao@exemplo.com.br',
      'Rio de Janeiro',
      '1999-02-06'
   );

CREATE TABLE brands (
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
   COLUMN brand_id VARCHAR(150) NOT NULL;

ALTER TABLE
   products
ADD
   FOREIGN KEY (brand_id) REFERENCES brands(id);

INSERT INTO
   orders (id, total, status, client_id, client_email, created_at)
VALUES
   ('1395a4f6-11b9-439e-a59e-7a03f7750834', 100, 'WAITING_PAYMENT', '1fef5e47-5ab0-4391-b0a0-49592e977578', 'joao@exemplo.com.br', '2025-02-07T13:38:06.399843833')

INSERT INTO
   products (id, name, price, stoke, image_ref)
VALUES
   (
      '284791a5-5a40-4a31-a60c-d2df68997569',
      'Iphone 16 Pro Apple (256G) - Black Steal',
      9800.99,
      20,
      "http://img.com/2324"
   );

SELECT
   name
FROM
   products
WHERE
   id = "284791a5-5a40-4a31-a60c-d2df68997569";

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

SHOW CREATE TABLE orders;

CREATE TABLE IF NOT EXISTS order_items (
   id VARCHAR(150) PRIMARY KEY,
   unity_price REAL NOT NULL,
   quantity INTEGER NOT NULL,
   order_id VARCHAR(150) NOT NULL,
   product_id VARCHAR(150) NOT NULL,
   FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE
);