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

INSERT INTO
   orders (
      id,
      total,
      status,
      client_id,
      client_email,
      created_at
   )
VALUES
   (
      '1395a4f6-11b9-439e-a59e-7a03f7750834',
      100,
      'WAITING_PAYMENT',
      '1fef5e47-5ab0-4391-b0a0-49592e977578',
      'joao@exemplo.com.br',
      '2025-02-07T13:38:06.399843833'
   );


INSERT INTO 
   brands (id, name)
VALUES
('291e753d-1861-4dd5-b1dd-acdab2945e66', 'Apple')

INSERT INTO
   products (id, name, price, stoke, brand_id, image_ref)
VALUES
   (
      '284791a5-5a40-4a31-a60c-d2df68997569',
      'Iphone 16 Pro Apple (256G) - Black Steal',
      9800.99,
      20,
      '291e753d-1861-4dd5-b1dd-acdab2945e66',
      'http://img.com/2324'
   );


INSERT INTO coupons
   (code, percentage, usage_limit, is_available, expired_at)
VALUES
   ('SAVE10', 10, 2, 1, '2025-02-14');