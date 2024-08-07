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
       ('Queenstown', 'New Zealand', TRUE, -45.038398530572536, 168.65877721435268, NULL, NULL, NULL);


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
