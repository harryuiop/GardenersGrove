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
VALUES ('Auckland', 'New Zealand', TRUE, -36.848525309230695, 174.76215186484197, NULL, NULL, NULL),
       ('Christchurch', 'New Zealand', TRUE, -43.52873828575177, 172.61946504809166, NULL, NULL, NULL),
       ('Wellington', 'New Zealand', TRUE, -41.29049377331487, 174.78195358349248, NULL, NULL, NULL),
       ('Hamilton', 'New Zealand', TRUE, -37.80495392682121, 175.30419405185643, NULL, NULL, NULL),
       ('Tauranga', 'New Zealand', TRUE, -37.685088014099115, 176.15242173151168, NULL, NULL, NULL),
       ('Lower Hutt', 'New Zealand', TRUE, -41.20975215386865, 174.90693128595632, NULL, NULL, NULL),
       ('Dunedin', 'New Zealand', TRUE, -45.861762374090375, 170.62692539587306, NULL, NULL, NULL),
       ('Palmerston North', 'New Zealand', TRUE, -40.35923355848256, 175.61172298733018, NULL, NULL, NULL),
       ('Napier', 'New Zealand', TRUE, -39.490042756979484, 176.9010129748827, NULL, NULL, NULL),
       ('Hibiscus Coast', 'New Zealand', TRUE, -36.62323188694552, 174.7481756867455, NULL, NULL, NULL),
       ('Porirua', 'New Zealand', TRUE, -41.13298345658692, 174.8390620093526, NULL, NULL, NULL),
       ('New Plymouth', 'New Zealand', TRUE, -39.05595846439934, 174.07198473718512, NULL, NULL, NULL),
       ('Rotorua', 'New Zealand', TRUE, -38.1346831506834, 176.2457272735295, NULL, NULL, NULL),
       ('Whangarei', 'New Zealand', TRUE, -35.717369943573864, 174.31597608871215, NULL, NULL, NULL),
       ('Nelson', 'New Zealand', TRUE, -41.28989234093661, 173.23169141275727, NULL, NULL, NULL),
       ('Hastings', 'New Zealand', TRUE, -39.62225544729829, 176.83070367890804, NULL, NULL, NULL),
       ('Invercargill', 'New Zealand', TRUE, -46.40235066043681, 168.3553202439977, NULL, NULL, NULL),
       ('Upper Hutt', 'New Zealand', TRUE, -41.1301740364658, 175.06959301444988, NULL, NULL, NULL),
       ('Whanganui', 'New Zealand', TRUE, -39.92997713130781, 175.0299819752915, NULL, NULL, NULL),
       ('Gisborne', 'New Zealand', TRUE, -38.59225981646112, 177.965351187787, NULL, NULL, NULL),
       ('Paraparaumu', 'New Zealand', TRUE, -40.895352076043984, 175.00463113547062, NULL, NULL, NULL),
       ('Blenheim', 'New Zealand', TRUE, -41.51818491944056, 173.96340405138307, NULL, NULL, NULL),
       ('Rolleston', 'New Zealand', TRUE, -43.60642342871527, 172.3874486445921, NULL, NULL, NULL),
       ('Queenstown', 'New Zealand', TRUE, -45.038398530572536, 168.65877721435268, NULL, NULL, NULL),
       ('Timaru', 'New Zealand', TRUE, -44.38249564729987, 171.24216028483687, NULL, NULL, NULL),
       ('Pukekohe', 'New Zealand', TRUE, -37.205835901829026, 174.90188537696935, NULL, NULL, NULL),
       ('Taupō', 'New Zealand', TRUE, -38.69362276756423, 176.0757259922453, NULL, NULL, NULL),
       ('Masterton', 'New Zealand', TRUE, -40.949130595162714, 175.66867598929898, NULL, NULL, NULL),
       ('Sydney', 'Australia', TRUE, -33.868820, 151.209290, NULL, NULL, NULL),
       ('Melbourne', 'Australia', TRUE, -37.813629, 144.963058, NULL, NULL, NULL),
       ('London', 'United Kingdom', TRUE, 51.507351, -0.127758, NULL, NULL, NULL),
       ('New York', 'United States', TRUE, 40.712776, -74.005974, NULL, NULL, NULL),
       ('Tokyo', 'Japan', TRUE, 35.676192, 139.650311, NULL, NULL, NULL),
       ('Paris', 'France', TRUE, 48.856613, 2.352222, NULL, NULL, NULL),
       ('Berlin', 'Germany', TRUE, 52.520008, 13.404954, NULL, NULL, NULL),
       ('Toronto', 'Canada', TRUE, 43.651070, -79.347015, NULL, NULL, NULL),
       ('Cape Town', 'South Africa', TRUE, -33.924870, 18.424055, NULL, NULL, NULL),
       ('Rio de Janeiro', 'Brazil', TRUE, -22.906847, -43.172897, NULL, NULL, NULL);


INSERT INTO advice_ranges(min_temperature, max_temperature, min_moisture, max_moisture, min_pressure, max_pressure,
    min_humidity, max_humidity, light_level)
VALUES (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0),
       (20.0, 25.0, 30.0, 70.0, 0.9, 1.1, 20.0, 80.0, 2.0);

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created, advice_ranges)
VALUES (1, 'Default User''s first Garden', 'The first garden created.', 1, 2, TRUE, TRUE, NOW(), 1),
       (2, 'Default User''s Second Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 2),
       (3, 'Default User''s Third Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 3),
       (4, 'Default User''s Fourth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 4),
       (5, 'Default User''s Fifth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 5),
       (6, 'Default User''s Sixth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 6),
       (7, 'Default User''s Seventh Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 7),
       (8, 'Default User''s Eighth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 8),
       (9, 'Default User''s Ninth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 9),
       (10, 'Default User''s Tenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 10),
       (11, 'Default User''s Eleventh Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 11),
       (12, 'Default User''s Twelfth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 12),
       (13, 'Default User''s Thirteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 13),
       (14, 'Default User''s Fourteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 14),
       (15, 'Default User''s Fifteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW(), 15),
       (16, 'Second User''s Third Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 16),
       (17, 'Second User''s Fourth Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 17),
       (18, 'Second User''s Fifth Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 18),
       (19, 'Second User''s Sixth Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 19),
       (20, 'Second User''s Seventh Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 20),
       (21, 'Second User''s Eighth Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 21),
       (22, 'Second User''s Ninth Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 22),
       (23, 'Second User''s Tenth Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 23),
       (24, 'Second User''s Eleventh Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 24),
       (25, 'Second User''s Twelfth Garden', NULL, 2, NULL, TRUE, TRUE, NOW(), 25),
       (26, 'Third User''s First Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 26),
       (27, 'Third User''s Second Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 27),
       (28, 'Third User''s Third Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 28),
       (29, 'Third User''s Fourth Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 29),
       (30, 'Third User''s Fifth Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 30),
       (31, 'Third User''s Sixth Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 31),
       (32, 'Third User''s Seventh Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 32),
       (33, 'Third User''s Eighth Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 33),
       (34, 'Third User''s Ninth Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 34),
       (35, 'Third User''s Tenth Garden', NULL, 3, NULL, TRUE, TRUE, NOW(), 35),
       (36, 'Third User''s Eleventh Garden', 'A demonstration garden.', 3, 0.5, TRUE, TRUE, NOW(), 36),
       (37, 'Third User''s Twelfth Garden', 'This description needs to be edited first as it is not verified', 3, 0.5,
        FALSE, FALSE, NOW(), 37),
       (38, 'Third User''s Thirteenth Garden', 'A demonstration garden.', 3, 0.5, TRUE, TRUE, NOW(), 38);


UPDATE garden
SET arduino_id = '127.0.0.1'
WHERE id = 1;
UPDATE garden
SET arduino_id = '127.0.0.2'
WHERE id = 2;

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


INSERT INTO arduino_data_point (atmosphere_atm, humidity_percent, light_percent, moisture_percent, temp_celsius, garden_id, time)
VALUES (1.083, 45.2, 65.5, 25.0, 20.0, 1, '2024-01-10 11:20:00'),
       (1.157, 70.0, 40.5, 55.0, 20.0, 1, '2024-01-02 11:20:00'),
       (1.091, 33.4, 78.9, 45.3, 20.0, 1, '2024-01-03 11:20:00'),
       (1.121, 29.8, 60.7, 71.2, 20.0, 1, '2024-01-04 11:20:00'),
       (1.067, 50.5, 23.6, 67.3, 20.0, 1, '2024-01-05 11:20:00'),
       (1.105, 72.3, 48.1, 43.8, 20.0, 1, '2024-01-06 11:20:00'),
       (1.142, 79.4, 67.9, 35.0, 20.0, 1, '2024-01-07 11:20:00'),
       (1.098, 58.7, 9.0, 61.3, 30.0, 1, '2024-08-25 23:53:00'),
       (1.024, 62.5, 72.2, 20.0, 25.0, 1, '2024-08-25 08:20:00'),
       (1.177, 39.2, 43.3, 28.7, 20.0, 1, '2024-08-27 11:20:00'),
       (1.020, 53.9, 44.1, 30.5, 20.0, 1, '2024-08-30 15:20:00'),
       (1.082, 77.8, 56.0, 58.0, 20.0, 1, '2024-09-05 10:15:00'),
       (1.110, 61.0, 35.7, 64.4, 10.0, 1, '2024-09-05 19:15:00'),
       (1.065, 55.3, 33.5, 27.6, 8.0, 1, '2024-09-07 18:53:00'),
       (1.124, 75.5, 39.4, 20.1, 15.0, 1, '2024-09-07 14:53:00'),
       (1.095, 69.2, 45.8, 31.2, 0.0, 1, '2024-09-09 14:53:00'),
       (1.041, 50.0, 11.3, 72.1, 7.3, 1, '2024-09-09 19:53:00'),
       (1.089, 74.7, 34.0, 41.2, 40.0, 1, '2024-09-10 13:43:00'),
       (1.070, 68.3, 53.2, 24.6, 30.0, 1, '2024-09-10 13:13:00'),
       (1.025, 78.0, 63.9, 21.9, 11.0, 1, '2024-09-11 05:01:00'),
       (1.138, 49.1, 41.4, 30.3, 12.0, 1, '2024-09-11 11:01:00'),
       (1.078, 70.9, 54.8, 60.4, 12.0, 1, '2024-09-10 11:01:00'),
       (1.034, 63.5, 47.6, 20.0, 12.0, 1, '2024-09-09 11:01:00'),
       (1.099, 65.8, 36.9, 74.2, 11.5, 1, '2024-09-08 11:01:00'),
       (1.080, 42.3, 49.3, 52.5, 9.0, 1, '2024-09-07 11:01:00'),
       (1.054, 30.9, 62.1, 45.7, 15.0, 1, '2024-09-06 11:01:00'),
       (1.112, 78.4, 71.6, 38.4, 18.0, 1, '2024-09-05 11:01:00'),
       (1.093, 35.8, 55.5, 48.3, 12.0, 1, '2024-09-11 15:01:00'),
       (1.076, 57.0, 39.2, 63.7, 12.0, 1, '2024-09-10 15:01:00'),
       (1.043, 64.2, 52.8, 37.5, 12.0, 1, '2024-09-09 15:01:00'),
       (1.051, 75.1, 48.3, 29.9, 11.5, 1, '2024-09-08 15:01:00'),
       (1.071, 47.2, 15.4, 55.0, 7.5, 1, '2024-09-08 20:01:00'),
       (1.029, 34.8, 64.1, 26.6, 15.0, 1, '2024-09-26 10:01:00'),
       (1.095, 66.4, 75.0, 58.2, 18.0, 1, '2024-09-25 15:01:00'),
       (1.062, 48.5, 41.7, 28.1, 11.0, 1, '2024-09-26 06:53:00'),
       (1.044, 55.9, 22.0, 30.0, 9.0, 1, '2024-09-25 23:53:00'),
       (1.132, 71.2, 58.0, 34.0, 10.0, 1, '2024-09-25 10:02:00'),
       (1.132, 71.2, 58.0, 34.0, 10.0, 1, '2024-09-27 15:02:00'),
       (1.132, 15.3, 58.0, 34.0, 10.0, 1, '2024-09-27 10:02:00');

INSERT INTO arduino_data_point (atmosphere_atm, humidity_percent, light_percent, moisture_percent, temp_celsius, garden_id, time)
VALUES
    (1.075, 52.8, 58.3, 31.2, 22.5, 2, '2024-08-26 11:20:00'),
    (1.102, 65.7, 45.9, 48.6, 19.8, 2, '2024-08-27 14:30:00'),
    (1.088, 41.3, 72.1, 39.7, 21.2, 2, '2024-08-28 09:45:00'),
    (1.115, 34.6, 63.8, 67.9, 20.7, 2, '2024-08-29 16:15:00'),
    (1.059, 47.2, 28.4, 59.1, 19.5, 2, '2024-08-30 12:00:00'),
    (1.098, 68.9, 52.7, 41.5, 20.3, 2, '2024-08-31 08:30:00'),
    (1.135, 75.6, 61.4, 37.8, 21.8, 2, '2024-09-01 17:45:00'),
    (1.092, 54.3, 12.7, 57.9, 29.2, 2, '2024-09-02 23:53:00'),
    (1.031, 59.8, 68.5, 24.3, 26.7, 2, '2024-09-03 08:20:00'),
    (1.168, 44.7, 39.6, 32.1, 19.6, 2, '2024-09-04 11:20:00'),
    (1.027, 50.4, 47.8, 33.9, 20.9, 2, '2024-09-05 15:20:00'),
    (1.079, 73.2, 51.6, 53.4, 21.5, 2, '2024-09-06 10:15:00'),
    (1.105, 57.6, 31.9, 61.8, 11.3, 2, '2024-09-07 19:15:00'),
    (1.071, 51.9, 36.2, 29.4, 9.2, 2, '2024-09-08 18:53:00'),
    (1.119, 71.8, 42.7, 23.5, 16.4, 2, '2024-09-09 14:53:00'),
    (1.087, 64.5, 49.2, 34.8, 15.5, 2, '2024-09-10 14:53:00'),
    (1.038, 46.7, 14.9, 68.3, 18.1, 2, '2024-09-11 19:53:00'),
    (1.084, 70.3, 37.5, 38.9, 23.6, 2, '2024-09-12 13:43:00'),
    (1.065, 63.9, 56.8, 27.2, 21.4, 2, '2024-09-13 13:13:00'),
    (1.029, 74.6, 59.3, 25.7, 12.7, 2, '2024-09-14 05:01:00'),
    (1.132, 45.8, 44.9, 33.1, 13.5, 2, '2024-09-15 11:01:00'),
    (1.073, 67.2, 58.4, 56.9, 11.8, 2, '2024-09-16 11:01:00'),
    (1.041, 60.1, 51.2, 22.8, 12.9, 2, '2024-09-17 15:01:00'),
    (1.095, 62.4, 40.3, 70.6, 10.9, 2, '2024-09-18 09:30:00'),
    (1.076, 39.7, 52.9, 49.1, 20.2, 2, '2024-09-19 12:45:00'),
    (1.058, 35.3, 65.7, 42.3, 16.2, 2, '2024-09-20 16:30:00'),
    (1.108, 74.9, 67.2, 41.7, 17.3, 2, '2024-09-21 10:15:00'),
    (1.089, 40.2, 59.1, 45.6, 13.1, 2, '2024-09-22 14:20:00'),
    (1.082, 53.5, 43.8, 60.2, 11.5, 2, '2024-09-23 18:00:00'),
    (1.047, 61.8, 55.4, 34.9, 12.6, 2, '2024-09-24 07:45:00'),
    (1.055, 71.3, 51.9, 32.6, 10.8, 2, '2024-09-24 11:30:00'),
    (1.068, 43.9, 18.7, 51.6, 18.3, 2, '2024-09-24 20:01:00'),
    (1.035, 38.4, 67.8, 29.2, 14.7, 2, '2024-09-24 22:15:00'),
    (1.091, 62.9, 70.6, 54.7, 18.9, 2, '2024-09-25 02:30:00'),
    (1.057, 45.1, 45.3, 30.8, 11.7, 2, '2024-09-25 06:53:00'),
    (1.049, 52.4, 25.6, 32.7, 9.8, 2, '2024-09-25 10:15:00'),
    (1.127, 68.7, 54.2, 36.5, 10.5, 2, '2024-09-25 14:02:00'),
    (1.083, 72.5, 0.5, 35.8, 8.2, 2, '2024-09-25 00:00:00'),
    (1.079, 74.1, 0.3, 36.2, 7.8, 2, '2024-09-25 01:00:00'),
    (1.075, 75.3, 0.2, 36.5, 7.5, 2, '2024-09-25 02:00:00'),
    (1.072, 76.0, 0.2, 36.7, 7.3, 2, '2024-09-25 03:00:00'),
    (1.070, 76.5, 1.0, 36.9, 7.1, 2, '2024-09-25 04:00:00'),
    (1.069, 76.2, 5.5, 37.0, 7.2, 2, '2024-09-25 05:00:00'),
    (1.071, 75.0, 15.8, 36.8, 7.9, 2, '2024-09-25 06:00:00'),
    (1.076, 72.3, 32.7, 36.3, 9.5, 2, '2024-09-25 07:00:00'),
    (1.082, 68.9, 48.6, 35.7, 11.8, 2, '2024-09-25 08:00:00'),
    (1.089, 64.2, 62.3, 34.9, 14.2, 2, '2024-09-25 09:00:00'),
    (1.095, 59.5, 74.8, 34.1, 16.5, 2, '2024-09-25 10:00:00'),
    (1.099, 55.1, 83.5, 33.4, 18.3, 2, '2024-09-25 11:00:00'),
    (1.102, 51.8, 88.2, 32.9, 19.6, 2, '2024-09-25 12:00:00'),
    (1.104, 49.5, 90.1, 32.5, 20.4, 2, '2024-09-25 13:00:00'),
    (1.105, 48.2, 89.7, 32.3, 20.8, 2, '2024-09-25 14:00:00'),
    (1.104, 48.0, 86.4, 32.2, 20.9, 2, '2024-09-25 15:00:00'),
    (1.102, 49.3, 79.5, 32.4, 20.5, 2, '2024-09-25 16:00:00'),
    (1.099, 51.6, 68.2, 32.8, 19.7, 2, '2024-09-25 17:00:00'),
    (1.095, 54.8, 52.3, 33.3, 18.4, 2, '2024-09-25 18:00:00'),
    (1.090, 58.7, 33.6, 33.9, 16.9, 2, '2024-09-25 19:00:00'),
    (1.086, 62.9, 15.2, 34.5, 15.1, 2, '2024-09-25 20:00:00'),
    (1.082, 66.8, 3.7, 35.1, 13.5, 2, '2024-09-25 21:00:00'),
    (1.079, 70.0, 0.8, 35.6, 12.2, 2, '2024-09-25 22:00:00'),
    (1.076, 72.4, 0.4, 36.0, 11.1, 2, '2024-09-25 23:00:00');

INSERT INTO arduino_data_point (atmosphere_atm, humidity_percent, light_percent, moisture_percent, temp_celsius, garden_id, time)
VALUES
    (1.068, 55.3, 56.8, 33.5, 23.1, 3, '2024-08-26 11:20:00'),
    (1.109, 63.2, 47.5, 46.9, 20.5, 3, '2024-08-27 14:30:00'),
    (1.095, 43.7, 70.9, 41.2, 22.0, 3, '2024-08-28 09:45:00'),
    (1.122, 36.9, 62.4, 65.3, 21.3, 3, '2024-08-29 16:15:00'),
    (1.063, 49.8, 30.1, 57.8, 20.1, 3, '2024-08-30 12:00:00'),
    (1.105, 66.5, 54.3, 43.7, 21.0, 3, '2024-08-31 08:30:00'),
    (1.142, 73.1, 59.8, 39.5, 22.5, 3, '2024-09-01 17:45:00'),
    (1.088, 56.7, 14.3, 55.6, 29.8, 3, '2024-09-02 23:53:00'),
    (1.037, 62.1, 66.9, 26.1, 27.3, 3, '2024-09-03 08:20:00'),
    (1.175, 46.9, 41.2, 34.3, 20.3, 3, '2024-09-04 11:20:00'),
    (1.033, 52.8, 49.4, 35.7, 21.6, 3, '2024-09-05 15:20:00'),
    (1.085, 70.8, 53.2, 51.9, 22.2, 3, '2024-09-06 10:15:00'),
    (1.112, 59.9, 33.5, 59.5, 12.1, 3, '2024-09-07 19:15:00'),
    (1.077, 54.3, 37.8, 31.6, 10.0, 3, '2024-09-08 18:53:00'),
    (1.125, 69.4, 44.3, 25.7, 17.1, 3, '2024-09-09 14:53:00'),
    (1.093, 62.1, 50.8, 36.9, 16.2, 3, '2024-09-10 14:53:00'),
    (1.044, 49.1, 16.5, 66.1, 18.8, 3, '2024-09-11 19:53:00'),
    (1.090, 67.9, 39.1, 41.0, 24.3, 3, '2024-09-12 13:43:00'),
    (1.071, 61.5, 58.4, 29.4, 22.1, 3, '2024-09-13 13:13:00'),
    (1.035, 72.2, 57.7, 27.9, 13.4, 3, '2024-09-14 05:01:00'),
    (1.138, 48.2, 46.5, 35.2, 14.2, 3, '2024-09-15 11:01:00'),
    (1.079, 64.8, 60.0, 54.5, 12.5, 3, '2024-09-16 11:01:00'),
    (1.047, 57.7, 52.8, 24.9, 13.6, 3, '2024-09-17 15:01:00'),
    (1.101, 60.0, 41.9, 68.2, 11.6, 3, '2024-09-18 09:30:00'),
    (1.082, 42.1, 54.5, 47.3, 20.9, 3, '2024-09-19 12:45:00'),
    (1.064, 37.7, 67.3, 44.5, 16.9, 3, '2024-09-20 16:30:00'),
    (1.114, 72.5, 65.6, 43.9, 18.0, 3, '2024-09-21 10:15:00'),
    (1.095, 42.6, 60.7, 47.8, 13.8, 3, '2024-09-22 14:20:00'),
    (1.088, 55.9, 45.4, 58.4, 12.2, 3, '2024-09-23 18:00:00'),
    (1.053, 59.4, 57.0, 37.1, 13.3, 3, '2024-09-24 07:45:00'),
    (1.061, 68.9, 53.5, 34.8, 11.5, 3, '2024-09-24 11:30:00'),
    (1.074, 46.3, 20.3, 49.4, 19.0, 3, '2024-09-24 20:01:00'),
    (1.041, 40.8, 69.4, 31.4, 15.4, 3, '2024-09-24 22:15:00'),
    (1.097, 60.5, 72.2, 52.3, 19.6, 3, '2024-09-25 02:30:00'),
    (1.063, 47.5, 46.9, 33.0, 12.4, 3, '2024-09-25 06:53:00'),
    (1.055, 54.8, 27.2, 34.9, 10.5, 3, '2024-09-25 10:15:00'),
    (1.133, 66.3, 55.8, 38.7, 11.2, 3, '2024-09-25 14:02:00'),
    (1.089, 70.1, 0.7, 38.0, 8.9, 3, '2024-09-25 00:00:00'),
    (1.085, 71.7, 0.5, 38.4, 8.5, 3, '2024-09-25 01:00:00'),
    (1.081, 72.9, 0.4, 38.7, 8.2, 3, '2024-09-25 02:00:00'),
    (1.078, 73.6, 0.4, 38.9, 8.0, 3, '2024-09-25 03:00:00'),
    (1.076, 74.1, 1.2, 39.1, 7.8, 3, '2024-09-25 04:00:00'),
    (1.075, 73.8, 5.7, 39.2, 7.9, 3, '2024-09-25 05:00:00'),
    (1.077, 72.6, 16.0, 39.0, 8.6, 3, '2024-09-25 06:00:00'),
    (1.082, 69.9, 33.1, 38.5, 10.2, 3, '2024-09-25 07:00:00'),
    (1.088, 66.5, 49.2, 37.9, 12.5, 3, '2024-09-25 08:00:00'),
    (1.095, 61.8, 63.1, 37.1, 14.9, 3, '2024-09-25 09:00:00'),
    (1.101, 57.1, 75.8, 36.3, 17.2, 3, '2024-09-25 10:00:00'),
    (1.105, 52.7, 84.7, 35.6, 19.0, 3, '2024-09-25 11:00:00'),
    (1.108, 49.4, 89.6, 35.1, 20.3, 3, '2024-09-25 12:00:00'),
    (1.110, 47.1, 91.7, 34.7, 21.1, 3, '2024-09-25 13:00:00'),
    (1.111, 45.8, 91.3, 34.5, 21.5, 3, '2024-09-25 14:00:00'),
    (1.110, 45.6, 88.0, 34.4, 21.6, 3, '2024-09-25 15:00:00'),
    (1.108, 46.9, 81.1, 34.6, 21.2, 3, '2024-09-25 16:00:00'),
    (1.105, 49.2, 69.8, 35.0, 20.4, 3, '2024-09-25 17:00:00'),
    (1.101, 52.4, 53.9, 35.5, 19.1, 3, '2024-09-25 18:00:00'),
    (1.096, 56.3, 35.2, 36.1, 17.6, 3, '2024-09-25 19:00:00'),
    (1.092, 60.5, 16.8, 36.7, 15.8, 3, '2024-09-25 20:00:00'),
    (1.088, 64.4, 5.3, 37.3, 14.2, 3, '2024-09-25 21:00:00'),
    (1.085, 67.6, 2.4, 37.8, 12.9, 3, '2024-09-25 22:00:00'),
    (1.082, 70.0, 2.0, 38.2, 11.8, 3, '2024-09-25 23:00:00');