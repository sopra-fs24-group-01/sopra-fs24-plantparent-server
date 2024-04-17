INSERT INTO "PUBLIC"."USER"
VALUES (1, 'user1@mail.com', 'asdf', '5c6f1b47-7461-4637-9881-039692044f69', 'user1'),
       (2, 'user2@mail.com', 'asdf', '11fb6270-ae06-458b-a398-de9507ddf110', 'user2'),
       (3, 'user3@mail.com', 'asdf', '59ae3fa7-da1c-4db7-a0ae-1bfdbf37e867', 'user3');

INSERT INTO "PUBLIC"."PLANT"
VALUES (1, 'Specifically only water on the 1st of April.', NULL, NULL, 'plastic plant', 'ikea', 365, 3),
       (2, 'Specifically only water on the 1st of April.', TIMESTAMP '2020-04-01 02:00:00',
        TIMESTAMP '2025-04-01 02:00:00', 'plastic plant', 'ikea', 365, 3),
       (3, 'Specifically only water on the 1st of April.', TIMESTAMP '2024-04-01 02:00:00',
        TIMESTAMP '2025-04-01 02:00:00', 'plastic plant', 'ikea', 365, 3),
       (4, 'Needs 50L Water.', NULL, NULL, 'favorite-tree', 'apple tree', NULL, 1),
       (5, 'Make sure the water doesn''t hit the flowers.', NULL, NULL, 'rose bush', 'black-rose', NULL, 1);

INSERT INTO "PUBLIC"."PLANT_CARETAKERS"
VALUES (4, 2),
       (4, 3),
       (5, 2);