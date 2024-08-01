INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2000-01-01', 'user1@gmail.com', 'Default', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user2@gmail.com', 'Harry', 'Ellis',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user3@gmail.com', 'Sam', 'Willems',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user4@gmail.com', 'HanByeol', 'Yang',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user5@gmail.com', 'Harrison', 'Parkes',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user6@gmail.com', 'Josh', 'Winters',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user7@gmail.com', 'Vincent', 'Chen',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user8@gmail.com', 'Alex', 'Belcher',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user9@gmail.com', 'Morgan', 'English',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user10@gmail.com', 'Ella', 'Calder',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user11@gmail.com', 'Marina', 'Filipovic',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user12@gmail.com', 'Tim', 'Bell',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user13@gmail.com', 'Walter', 'Guttmann',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO friendship (friend1_user_id, friend2_user_id, id) VALUES (4, 1, 100);

INSERT INTO friendship (friend1_user_id, friend2_user_id, id) VALUES (3, 1, 101);

INSERT INTO friendship (friend1_user_id, friend2_user_id, id) VALUES (5, 1, 102);

INSERT INTO friendship (friend1_user_id, friend2_user_id, id) VALUES (6, 1, 103);

INSERT INTO friendship (friend1_user_id, friend2_user_id, id) VALUES (7, 1, 104);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 8);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 12);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 13, 1);


INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Auckland', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Christchurch', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Wellington', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Hamilton', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Tauranga', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Lower Hutt', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Dunedin', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Palmerston North', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Napier', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Hibiscus Coast', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Porirua', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('New Plymouth', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Rotorua', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Whangarei', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Nelson', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Hastings', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Invercargill', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Upper Hutt', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Whanganui', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Gisborne', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Paraparaumu', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Blenheim', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Rolleston', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Queenstown', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);


INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (1, 'Default User''s first Garden', 'The first garden created.', 1, 2, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (2, 'Default User''s Second Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (3, 'Default User''s Third Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (4, 'Default User''s Fourth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (5, 'Default User''s Fifth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (6, 'Default User''s Sixth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (7, 'Default User''s Seventh Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (8, 'Default User''s Eighth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (9, 'Default User''s Ninth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (10, 'Default User''s Tenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (11, 'Default User''s Eleventh Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (12, 'Default User''s Twelfth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (13, 'Default User''s Thirteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (14, 'Default User''s Fourteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (15, 'Default User''s Fifteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (16, 'Default User''s Sixteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (17, 'Default User''s Seventeenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (18, 'Default User''s Eighteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (19, 'Default User''s Nineteenth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (20, 'Default User''s Twentieth Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (21, 'Default User''s Twenty First Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (22, 'Default User''s Twenty Second Garden', NULL, 1, NULL, TRUE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (23, 'Second User''s First Garden', 'A demonstration garden.', 2, 0.5, FALSE, TRUE, NOW());

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description,
                    time_created)
VALUES (24, 'Second User''s Second Garden', 'This description needs to be edited first as it is not verified', 2, 0.5,
        FALSE, FALSE, NOW());


INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (1, 'First plant of Default User''s first Garden', 1, NULL, 'Plant One', '2024-01-01');

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (2, 'Second plant of First User''s first Garden', 1, NULL, 'Plant Two', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (3, 'Third plant of First User''s first Garden', 1, NULL, 'Plant Three', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (4, 'Fourth plant of First User''s first Garden', 1, NULL, 'Plant Four', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (5, 'Fifth plant of First User''s first Garden', 1, NULL, 'Plant Five', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (6, 'Sixth plant of First User''s first Garden', 1, NULL, 'Plant Six', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (7, 'Seventh plant of First User''s first Garden', 1, NULL, 'Plant Seven', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (8, 'Eighth plant of First User''s first Garden', 1, NULL, 'Plant Eight', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (9, 'Ninth plant of First User''s first Garden', 1, NULL, 'Plant Nine', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (10, 'Tenth plant of First User''s first Garden', 1, NULL, 'Plant Ten', NULL);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on)
VALUES (NULL, 'First plant of Second User''s first Garden', 2, NULL, 'Star Fruit', NULL);
