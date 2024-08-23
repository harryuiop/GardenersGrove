INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name, token)
VALUES (TRUE, '2000-01-01', 'user1@gmail.com', 'Default', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user2@gmail.com', 'Harry', 'Ellis',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user3@gmail.com', 'Sam', 'Willems',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user4@gmail.com', 'HanByeol', 'Yang',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user5@gmail.com', 'Harrison', 'Parkes',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user6@gmail.com', 'Josh', 'Winters',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user7@gmail.com', 'Vincent', 'Chen',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user8@gmail.com', 'Alex', 'Belcher',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user9@gmail.com', 'Morgan', 'English',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user10@gmail.com', 'Ella', 'Calder',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user11@gmail.com', 'Marina', 'Filipovic',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user12@gmail.com', 'Tim', 'Bell',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'user13@gmail.com', 'Walter', 'Guttmann',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-02', 'dupuser1@outlook.com', 'Duplicate', 'Danny',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-03', 'dupuser2@outlook.com', 'Duplicate', 'Danny',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL),
       (TRUE, '2001-02-04', 'dupuser3@outlook.com', 'Duplicate', 'Danny',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);


INSERT INTO friendship (friend1_user_id, friend2_user_id, id)
VALUES (4, 1, 100),
       (3, 1, 101),
       (5, 1, 102),
       (6, 1, 103),
       (7, 1, 104),
       (1, 16, 105);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 8),
       (1, 1, 12),
       (1, 13, 1),
       (1, 1, 14),
       (2, 1, 15);


INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Auckland', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Christchurch', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Wellington', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Hamilton', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Tauranga', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Lower Hutt', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Dunedin', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Palmerston North', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Napier', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Hibiscus Coast', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Porirua', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('New Plymouth', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Rotorua', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Whangarei', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Nelson', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Hastings', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Invercargill', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Upper Hutt', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Whanganui', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Gisborne', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Paraparaumu', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Blenheim', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Rolleston', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL),
       ('Queenstown', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);


INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (1, 'Default User''s first Garden', 'The first garden created.', 1, 2, TRUE, TRUE, NOW()),
       (2, 'Default User''s Second Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (3, 'Default User''s Third Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (4, 'Default User''s Fourth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (5, 'Default User''s Fifth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (6, 'Default User''s Sixth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (7, 'Default User''s Seventh Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (8, 'Default User''s Eighth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (9, 'Default User''s Ninth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (10, 'Default User''s Tenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (11, 'Default User''s Eleventh Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (12, 'Default User''s Twelfth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (13, 'Default User''s Thirteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (14, 'Default User''s Fourteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (15, 'Default User''s Fifteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (16, 'Default User''s Sixteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (17, 'Default User''s Seventeenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (18, 'Default User''s Eighteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (19, 'Default User''s Nineteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (20, 'Default User''s Twentieth Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (21, 'Default User''s Twenty First Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (22, 'Default User''s Twenty Second Garden', NULL, 1, NULL, TRUE, TRUE, NOW()),
       (23, 'Second User''s First Garden', 'A demonstration garden.', 2, 0.5, FALSE, TRUE, NOW()),
       (24, 'Second User''s Second Garden', 'This description needs to be edited first as it is not verified', 2, 0.5,
        FALSE, FALSE, NOW());


INSERT INTO tag (name)
VALUES ('first_tag'),
       ('second_tag'),
       ('third_tag'),
       ('fourth_tag'),
       ('fifth_tag');


INSERT INTO tag_gardens (tags_id, gardens_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 2);


INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (1, 'First plant of Default User''s first Garden', 1, NULL, 'Plant One', '2024-01-01'),
       (2, 'Second plant of First User''s first Garden', 1, NULL, 'Plant Two', NULL),
       (3, 'Third plant of First User''s first Garden', 1, NULL, 'Plant Three', NULL),
       (4, 'Fourth plant of First User''s first Garden', 1, NULL, 'Plant Four', NULL),
       (5, 'Fifth plant of First User''s first Garden', 1, NULL, 'Plant Five', NULL),
       (6, 'Sixth plant of First User''s first Garden', 1, NULL, 'Plant Six', NULL),
       (7, 'Seventh plant of First User''s first Garden', 1, NULL, 'Plant Seven', NULL),
       (8, 'Eighth plant of First User''s first Garden', 1, NULL, 'Plant Eight', NULL),
       (9, 'Ninth plant of First User''s first Garden', 1, NULL, 'Plant Nine', NULL),
       (10, 'Tenth plant of First User''s first Garden', 1, NULL, 'Plant Ten', NULL),
       (NULL, 'First plant of Second User''s first Garden', 2, NULL, 'Star Fruit', NULL);

INSERT INTO arduino_data_point (ATMOSPHERE_ATM, HUMIDITY_PERCENT, LIGHT_PERCENT, MOISTURE_PERCENT, TEMP_CELSIUS, GARDEN_ID, ID, TIME)
VALUES (1.0, 60.0, 90.0, 35.0, 20.0, 1, 2, '2024-01-02 11:20:00');

INSERT INTO arduino_data_point (ATMOSPHERE_ATM, HUMIDITY_PERCENT, LIGHT_PERCENT, MOISTURE_PERCENT, TEMP_CELSIUS, GARDEN_ID, ID, TIME)
VALUES (100.0, 50.0, 05.0, 30.0, 20.0, 1, 1, '2024-01-10 11:20:00');

INSERT INTO arduino_data_point (ATMOSPHERE_ATM, HUMIDITY_PERCENT, LIGHT_PERCENT, MOISTURE_PERCENT, TEMP_CELSIUS, GARDEN_ID, ID, TIME)
VALUES (1.0, 30.0, 80.0, 30.0, 20.0, 1, 3, '2024-01-03 11:20:00');

INSERT INTO arduino_data_point (ATMOSPHERE_ATM, HUMIDITY_PERCENT, LIGHT_PERCENT, MOISTURE_PERCENT, TEMP_CELSIUS, GARDEN_ID, ID, TIME)
VALUES (1.0, 10.0, 70.0, 40.0, 20.0, 1, 4, '2024-01-04 11:20:00');

INSERT INTO arduino_data_point (ATMOSPHERE_ATM, HUMIDITY_PERCENT, LIGHT_PERCENT, MOISTURE_PERCENT, TEMP_CELSIUS, GARDEN_ID, ID, TIME)
VALUES (1.0, 55.0, 20.0, 30.0, 20.0, 1, 5, '2024-01-05 11:20:00');

INSERT INTO arduino_data_point (ATMOSPHERE_ATM, HUMIDITY_PERCENT, LIGHT_PERCENT, MOISTURE_PERCENT, TEMP_CELSIUS, GARDEN_ID, ID, TIME)
VALUES (1.0, 59.0, 47.0, 70.0, 20.0, 1, 6, '2024-01-06 11:20:00');

INSERT INTO arduino_data_point (ATMOSPHERE_ATM, HUMIDITY_PERCENT, LIGHT_PERCENT, MOISTURE_PERCENT, TEMP_CELSIUS, GARDEN_ID, ID, TIME)
VALUES (1.0, 90.0, 43.0, 30.0, 20.0, 1, 7, '2024-01-07 11:20:00');
