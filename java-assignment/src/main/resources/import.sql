INSERT INTO store(id, name, quantityProductsInStock) VALUES (1, 'TONSTAD', 10);
INSERT INTO store(id, name, quantityProductsInStock) VALUES (2, 'KALLAX', 5);
INSERT INTO store(id, name, quantityProductsInStock) VALUES (3, 'BESTÅ', 3);
ALTER SEQUENCE store_seq RESTART WITH 4;

INSERT INTO product(id, name, stock) VALUES (1, 'TONSTAD', 10);
INSERT INTO product(id, name, stock) VALUES (2, 'KALLAX', 5);
INSERT INTO product(id, name, stock) VALUES (3, 'BESTÅ', 3);
ALTER SEQUENCE product_seq RESTART WITH 4;

INSERT INTO warehouse(id, businessUnitCode, location, capacity, stock, createdAt, archivedAt)
VALUES (1, 'MWH.001', 'ZWOLLE-001', 100, 10, '2024-07-01', null);
INSERT INTO warehouse(id, businessUnitCode, location, capacity, stock, createdAt, archivedAt)
VALUES (2, 'MWH.012', 'AMSTERDAM-001', 50, 5, '2023-07-01', null);
INSERT INTO warehouse(id, businessUnitCode, location, capacity, stock, createdAt, archivedAt)
VALUES (3, 'MWH.023', 'TILBURG-001', 30, 27, '2021-02-01', null);
ALTER SEQUENCE warehouse_seq RESTART WITH 4;

INSERT INTO warehouse_fulfilment(id, product, store, warehouse, createdAt, updatedAt) VALUES
-- ProductA in Store1, two warehouses (max reached for product-store)
(1, 'ProductA', 'Store1', 'Warehouse1', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),
(2, 'ProductA', 'Store1', 'Warehouse2', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),

-- ProductB in Store1, one warehouse
(3, 'ProductB', 'Store1', 'Warehouse2', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),

-- ProductC in Store1, one warehouse
(4, 'ProductC', 'Store1', 'Warehouse3', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),

-- Warehouse1 already storing 5 products across stores (max reached)
(5, 'ProductD', 'Store2', 'Warehouse1', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),
(6, 'ProductE', 'Store3', 'Warehouse1', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),
(7, 'ProductF', 'Store4', 'Warehouse1', '2024-07-01T00:00:00', '2024-07-01T00:00:00'),
(8, 'ProductG', 'Store5', 'Warehouse1', '2024-07-01T00:00:00', '2024-07-01T00:00:00');
ALTER SEQUENCE warehouse_fulfilment_seq RESTART WITH 9;
