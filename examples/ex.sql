/* 2025-02-01 22:19:05 [7 ms] */
CREATE DATABASE basic_sql DEFAULT CHARACTER SET = 'utf8mb4';

/* 2025-02-01 22:30:52 [23 ms] */
CREATE TABLE brends (
   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(100) NOT NULL UNIQUE,
   site VARCHAR(100),
   phone VARCHAR(15)
);

/* 2025-02-01 22:31:39 [19 ms] */
CREATE TABLE products (
   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(100) NOT NULL,
   price REAL NOT NULL,
   stoke INTEGER DEFAULT 0
);

/* 2025-02-01 22:32:41 [38 ms] */
ALTER TABLE
   products
ADD
   COLUMN id_brend INTEGER NOT NULL;

/* 2025-02-01 22:33:31 [36 ms] */
ALTER TABLE
   products
MODIFY
   COLUMN name VARCHAR(150);

/* 2025-02-01 22:35:03 [61 ms] */
ALTER TABLE
   products
ADD
   FOREIGN KEY (id_brend) REFERENCES brends(id);

/* 2025-02-01 22:36:42 [18 ms] */
CREATE TABLE control (
   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   entry_date DATE
);

/* 2025-02-01 22:37:12 [13 ms] */
DROP TABLE control;

/* 2025-02-01 22:37:55 [3 ms] */
DROP TABLE IF EXISTS control;

/* 2025-02-01 22:39:05 [14 ms] */
CREATE INDEX idx_product_name on products (name);

/* 2025-02-01 22:43:52 [5 ms] */
INSERT INTO
   brends (name, site, phone)
VALUES
   ('Apple', 'apple.com', '0800-761-0867'),
   ('Dell', 'dell.com.br', '0800-762-0867'),
   ('Shure', 'shure.com.br', '0800-753-3355');

/* 2025-02-01 22:48:33 [5 ms] */
INSERT INTO
   products
VALUES
   (
      1,
      'Iphone 16 Pro Apple (256G) - Black Steal',
      9800.99,
      100,
      1
   ),
   (
      2,
      'Notebook Dell (1T) - Black Steal',
      10000.1,
      1,
      2
   );

/* 2025-02-01 22:50:48 [5 ms] */
INSERT INTO
   products (name, price, stoke, id_brend)
VALUES
   (
      'Iphone 16 Pro Apple (256G) - Black Steal',
      9800.99,
      100,
      1
   ),
   (
      'Notebook Dell (1T) - Black Steal',
      10000.1,
      1,
      2
   );

/* 2025-02-01 22:51:53 [1 ms] */
SELECT
   *
FROM
   brends
LIMIT
   100;

/* 2025-02-01 22:52:00 [2 ms] */
SELECT
   id
FROM
   brends
LIMIT
   100;

/* 2025-02-01 22:52:10 [1 ms] */
SELECT
   id,
   name
FROM
   brends
LIMIT
   100;

/* 2025-02-01 22:52:37 [1 ms] */
SELECT
   id,
   name
FROM
   brends
WHERE
   id = 1
LIMIT
   100;

/* 2025-02-01 22:52:42 [1 ms] */
SELECT
   id,
   name
FROM
   brends
WHERE
   id = 2
LIMIT
   100;

/* 2025-02-01 22:52:53 [1 ms] */
SELECT
   id,
   name
FROM
   brends
WHERE
   id >= 2
LIMIT
   100;

/* 2025-02-01 22:54:49 [18 ms] */
CREATE TABLE apple_products (
   name VARCHAR(150) NOT NULL,
   stoke INTEGER DEFAULT 0
);

/* 2025-02-01 22:56:06 [1 ms] */
SELECT
   name,
   stoke
FROM
   products
WHERE
   id_brend = 1
LIMIT
   100;

/* 2025-02-01 22:56:33 [4 ms] */
INSERT INTO
   apple_products
SELECT
   name,
   stoke
FROM
   products
WHERE
   id_brend = 1;

/* 2025-02-01 22:56:52 [2 ms] */
SELECT
   *
FROM
   apple_products
LIMIT
   100;

/* 2025-02-01 22:57:37 [13 ms] */
DROP TABLE apple_products;

/* 2025-02-01 22:57:47 [22 ms] */
CREATE TABLE apple_products (
   name VARCHAR(150) NOT NULL,
   stoke INTEGER DEFAULT 0
);

/* 2025-02-01 22:57:50 [5 ms] */
INSERT INTO
   apple_products
SELECT
   name,
   stoke
FROM
   products
WHERE
   id_brend = 1;

/* 2025-02-01 22:57:54 [1 ms] */
SELECT
   *
FROM
   apple_products
LIMIT
   100;

/* 2025-02-01 22:58:18 [34 ms] */
TRUNCATE TABLE apple_products;

/* 2025-02-01 22:58:20 [1 ms] */
SELECT
   *
FROM
   apple_products
LIMIT
   100;

/* 2025-02-01 22:58:28 [15 ms] */
DROP TABLE apple_products;

/* 2025-02-01 23:02:24 [1 ms] */
UPDATE
   products
SET
   name = "Microfone SM7B Black"
WHERE
   name = 'Notebook Dell (1T) - Black Steal-2';

/* 2025-02-01 23:02:51 [1 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-01 23:03:26 [1 ms] */
UPDATE
   products
SET
   name = "Microfone SM7B Black"
WHERE
   name = 'Notebook Dell (1T) - Black Steal-2';

/* 2025-02-01 23:03:38 [0 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-01 23:04:27 [5 ms] */
UPDATE
   products
SET
   name = "Microfone SM7B Black"
WHERE
   name = 'Notebook Dell (1T) - Black Steal-1';

/* 2025-02-01 23:04:39 [1 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-01 23:04:43 [1 ms] */
UPDATE
   products
SET
   name = "Microfone SM7B Black"
WHERE
   name = 'Notebook Dell (1T) - Black Steal-1';

/* 2025-02-01 23:04:49 [1 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-01 23:05:05 [1 ms] */
UPDATE
   products
SET
   name = "Microfone SM7B Black"
WHERE
   id = 4;

/* 2025-02-01 23:05:13 [4 ms] */
UPDATE
   products
SET
   name = "Microfone SM7B Red"
WHERE
   id = 4;

/* 2025-02-01 23:05:15 [1 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-01 23:06:51 [5 ms] */
UPDATE
   products
SET
   stoke = stoke + 10
WHERE
   id_brend = 2;

/* 2025-02-01 23:06:57 [1 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-01 23:07:00 [5 ms] */
UPDATE
   products
SET
   stoke = stoke + 10
WHERE
   id_brend = 2;

/* 2025-02-01 23:07:02 [0 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-01 23:08:20 [3 ms] */
DELETE from
   products
WHERE
   id = 3;

/* 2025-02-01 23:08:22 [2 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-01 23:09:28 [6 ms] */
INSERT INTO
   products (name, price, stoke, id_brend)
VALUES
   -- ('Iphone 16 Pro Apple (256G) - Black Steal', 9800.99, 100, 1),
   ('Smartphone - Black Steal', 100.2, 20, 1);

/* 2025-02-01 23:09:39 [0 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-02 12:11:22 [2 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-02 12:19:35 [2 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-02 13:46:12 [2 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-02 14:52:45 [1 ms] */
SELECT
   *
FROM
   products
LIMIT
   100;

/* 2025-02-03 17:14:30 [15 ms] */
CREATE TABLE Post (
   id VARCHAR(255) NOT NULL,
   title VARCHAR(255) NOT NULL,
   slug VARCHAR(255) NOT NULL,
   date date NOT NULL,
   tags VARCHAR(255),
   PRIMARY KEY (id)
);

/* 2025-02-03 17:18:57 [1 ms] */
SELECT
   *
FROM
   post
LIMIT
   100;

/* 2025-02-04 11:45:38 [1 ms] */
SELECT
   *
FROM
   post
LIMIT
   100;

/* 2025-02-04 11:48:00 [3 ms] */
DROP TABLE IF EXISTS Post;

/* 2025-02-04 11:48:02 [9 ms] */
CREATE TABLE Post (
   id varchar(255) NOT NULL,
   title varchar(255) NOT NULL,
   slug varchar(255) NOT NULL,
   date date NOT NULL,
   time_to_read int NOT NULL,
   tags varchar(255),
   version INT,
   PRIMARY KEY (id)
);

/* 2025-02-04 11:48:05 [4 ms] */
INSERT INTO
   Post (
      id,
      title,
      slug,
      date,
      time_to_read,
      tags,
      version
   )
VALUES
   (
      1,
      'Hello, World!',
      'hello-world',
      CURRENT_DATE,
      5,
      'Spring Boot, Java',
      null
   );

/* 2025-02-04 13:07:21 [3 ms] */
DELETE FROM
   "post"
WHERE
   "id" = '1';

/* 2025-02-04 13:07:46 [0 ms] */
SELECT
   *
FROM
   post
LIMIT
   100;

/* 2025-02-04 21:33:37 [2 ms] */
SELECT
   *
FROM
   products
WHERE
   stoke < 80
   AND price < 3000
LIMIT
   100;

/* 2025-02-04 21:34:55 [1 ms] */
SELECT
   *
FROM
   products
WHERE
   name LIKE 'IPhone%'
LIMIT
   100;

/* 2025-02-04 21:35:18 [2 ms] */
SELECT
   *
FROM
   products
WHERE
   name LIKE 'Apple%'
LIMIT
   100;

/* 2025-02-04 21:35:22 [1 ms] */
SELECT
   *
FROM
   products
WHERE
   name LIKE '%Apple%'
LIMIT
   100;

/* 2025-02-04 21:37:12 [1 ms] */
SELECT
   *
FROM
   products
ORDER BY
   stoke ASC
LIMIT
   100;

/* 2025-02-04 21:37:28 [1 ms] */
SELECT
   *
FROM
   products
ORDER BY
   stoke DESC
LIMIT
   100;

/* 2025-02-04 21:37:40 [1 ms] */
SELECT
   *
FROM
   products
ORDER BY
   price DESC
LIMIT
   100;

/* 2025-02-04 21:38:03 [1 ms] */
SELECT
   *
FROM
   products
ORDER BY
   price DESC
LIMIT
   3;

/* 2025-02-04 21:57:25 [6 ms] */
SELECT
   *
FROM
   products
ORDER BY
   price DESC
LIMIT
   3;

CREATE TABLE clients (
   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   city VARCHAR(200),
   birthdate DATE
);

CREATE TABLE orders (
   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   order_date DATE,
   id_client INTEGER,
   FOREIGN KEY (id_client) REFERENCES clients(id)
);

CREATE TABLE order_items (
   order_id INTEGER,
   product_id INTEGER,
   quantity INTEGER,
   unity_price REAL,
   FOREIGN KEY (order_id) REFERENCES orders(id),
   FOREIGN KEY (product_id) REFERENCES products(id),
   PRIMARY KEY (order_id, product_id)
);

ALTER TABLE
   orders
MODIFY
   COLUMN order_date DATE DEFAULT (NOW());

INSERT INTO
   clients (name, email, city)
VALUES
   (
      'João Pereira',
      'joao@exemplo.com.br',
      'Rio de Janeiro'
   ),
   ('Ana Costa', 'ana@costa.com', 'São Paulo'),
   (
      'Carlos Souza',
      'carlos@gmail.com',
      'Belo Horizonte'
   ),
   (
      'Vanessa Weber',
      'vanessa@codigofonte.tv',
      'São José dos Campos'
   ),
   (
      'Gabriel Fróes',
      'gabriel@codigofonte.tv',
      'São José dos Campos'
   );

INSERT INTO
   orders (id_client, total)
VALUES
   (4, 5500.00),
   (5, 2000.00);

INSERT INTO
   order_items (order_id, product_id, quantity, unity_price)
VALUES
   (3, 1, 1, 3500.00),
   (3, 2, 1, 2000.00),
   (4, 2, 1, 2000.00);

-- SUB QUERYs
/* <- get all brands ids than get all products which brand_id is equal to those ids */
/* IN OPERATOR IS LIKE AN ARRAY WHICH YOU CAN SEND MULTIPLES VALUES LIKE IDS IN (1, 2, 3, 4) */
SELECT
   name,
   price
FROM
   products
WHERE
   id_brend IN (
      SELECT
         id
      FROM
         brends
      WHERE
         name = 'Apple'
   );

-- JOINS
SELECT
   clients.name,
   orders.total
FROM
   clients
   INNER JOIN orders ON clients.id = orders.id_client;

-- INNER JOIN
SELECT
   clients.name,
   orders.total AS "order_total"
   /*  BRING JUST THE ONES THAT ONE WHO HAS ORDERS */
FROM
   clients
   INNER JOIN orders ON clients.id = orders.id;

-- LEFT JOIN
SELECT
   clients.name,
   orders.total AS "order_total"
   /* BRING ALL CLIENTS AND THEY ORDERS, EVEN IF SOME CLIENTS HANS'T PLACED ANY ORDERS */
FROM
   clients
   LEFT JOIN orders ON clients.id = orders.id;

SELECT
   clients.name,
   orders.total AS "order_total"
   /* BRING ALL CLIENTS AND THEY ORDERS, BUT JUST IF THE ONES WHO HAS PLACED ANY ORDERS */
FROM
   clients
   RIGHT JOIN orders ON clients.id = orders.id;

-- AGGREGATION 
SELECT
   city
FROM
   clients;

SELECT
   city,
   COUNT(*) as client_amount
FROM
   clients
GROUP BY
   city;

SELECT
   DATE_FORMAT(order_date, '%Y-%m') as month,
   AVG(total) as total_avg
FROM
   orders
GROUP BY
   month;

SELECT
   sum(total) / COUNT(*) as avg
FROM
   orders;

SELECT
   MAX(total) AS biggest_order,
   MIN(total) as smallest_order
from
   orders;

/* all products below average */
SELECT
   name,
   stoke
FROM
   products
WHERE
   stoke < (
      SELECT
         AVG(stoke)
      from
         products
   );

SELECT
   c.city,
   SUM(o.total) AS "raw_total",
   COUNT(*) amount_sells
FROM
   clients AS c
   INNER JOIN orders AS o ON c.id = o.id_client
WHERE
   c.city IN ('Sao Jose dos Campos', 'Rio de janeiro')
GROUP BY
   c.city
HAVING
   raw_total < 7000;

