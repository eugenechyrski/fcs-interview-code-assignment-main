INSERT INTO store(id, name, quantityProductsInStock) VALUES
(1, 'TONSTAD', 10),
(2, 'KALLAX', 5),
(3, 'BESTÅ', 3),
(4, 'Store4', 3),
(5, 'Store5', 3),
(6, 'Store6', 3);
ALTER SEQUENCE store_seq RESTART WITH 7;

INSERT INTO product(id, name, stock) VALUES
(1, 'TONSTAD', 10),
(2, 'KALLAX', 5),
(3, 'BESTÅ', 3),
(4, 'Product4', 3),
(5, 'Product5', 3),
(6, 'Product6', 3),
(7, 'Product7', 3),
(8, 'Product8', 3),
(9, 'Product9', 3);
ALTER SEQUENCE product_seq RESTART WITH 10;

INSERT INTO warehouse(id, businessUnitCode, location, capacity, stock, createdAt, archivedAt) VALUES
 (1, 'MWH.001', 'ZWOLLE-001', 100, 10, '2024-07-01', null),
 (2, 'MWH.012', 'AMSTERDAM-001', 50, 5, '2023-07-01', null),
 (3, 'MWH.023', 'TILBURG-001', 30, 27, '2021-02-01', null),
 (4, 'MWH.024', 'TILBURG-001', 1, 1, '2021-02-01', null);
ALTER SEQUENCE warehouse_seq RESTART WITH 5;

INSERT INTO warehouse_fulfilment(id, productId, storeId, warehouse, createdAt, updatedAt) VALUES
-- ProductA in Store1, two warehouses (max reached for product-store)
(1, 1, 1, 'MWH.001', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),
(2, 1, 1, 'MWH.012', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),

-- ProductB in Store1, one warehouse
(3, 2, 1, 'MWH.012', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),

-- ProductC in Store1, one warehouse
(4, 3, 1, 'MWH.023', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),

-- Warehouse1 already storing 5 products across stores (max reached)
(5, 4, 2, 'MWH.001', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),
(6, 5, 3, 'MWH.001', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),
(7, 6, 4, 'MWH.001', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),
(8, 7, 5, 'MWH.001', '2024-07-01T00:00:00', '2024-07-01T00:00:00');
ALTER SEQUENCE warehouse_fulfilment_seq RESTART WITH 9;
