INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2000-01-01', 'user1@gmail.com', 'Default', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user2@gmail.com', 'Second', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user3@gmail.com', 'Third', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user4@gmail.com', 'Fourth', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user5@gmail.com', 'Fifth', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user6@gmail.com', 'Sixth', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user7@gmail.com', 'Seventh', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user8@gmail.com', 'Default', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token)
VALUES (TRUE, '2001-02-02', 'user9@gmail.com', 'Default', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL);


INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 5);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 6);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 7);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 8);


INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Christchurch', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Tauranga', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Wellington', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb)
VALUES ('Auckland', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL);


INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description)
VALUES (1, 'Default User''s first Garden', 'The first garden created.', 1, 2, TRUE, TRUE);

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description)
VALUES (2, 'Default User''s Second Garden', NULL, 1, NULL, FALSE, TRUE);

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description)
VALUES (3, 'Second User''s First Garden', 'A demonstration garden.', 2, 0.5, FALSE, TRUE);

INSERT INTO garden (location_id, name, description, owner_user_id, size, is_garden_public, verified_description)
VALUES (4, 'Second User''s Second Garden', 'This description needs to be edited first as it is not verified', 2, 0.5,
        FALSE, FALSE);


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
