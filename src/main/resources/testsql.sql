SELECT
    c.id,
    c.name,
    c.email,
    o.id,
    o.status
FROM
    clients AS c
    INNER JOIN orders AS o ON (c.id = o.client_id);


