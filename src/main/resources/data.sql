INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2000-01-01', 'user1@gmail.com', 'Default', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 1);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2001-02-02', 'user2@gmail.com', 'Second', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 2);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2001-02-02', 'user3@gmail.com', 'Third', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 3);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2001-02-02', 'user4@gmail.com', 'Fourth', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 4);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2001-02-02', 'user5@gmail.com', 'Fifth', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 5);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2001-02-02', 'user6@gmail.com', 'Sixth', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 6);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2001-02-02', 'user7@gmail.com', 'Seventh', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 7);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2001-02-02', 'user8@gmail.com', 'Eighth', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 8);

INSERT INTO users (confirmation, dob, email, first_name, last_name, password, profile_picture_file_name,
                   token, user_id)
VALUES (TRUE, '2001-02-02', 'user9@gmail.com', 'Ninth', 'User',
        '$2a$08$/1k6IbJBx9wkbHzPHOnUr.7TePtLBFSfn4dST0XXnDNvZCnG6vHH2', NULL, NULL, 9);


INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 5);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 6);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 7);

INSERT INTO friend_request (status, receiver_user_id, sender_user_id)
VALUES (1, 1, 8);


INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb, id)
VALUES ('Christchurch', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL, 1);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb, id)
VALUES ('Tauranga', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL, 2);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb, id)
VALUES ('Wellington', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL, 3);

INSERT INTO location (city, country, is_coordinates_set, lat, lng, postcode, street_address, suburb, id)
VALUES ('Auckland', 'New Zealand', FALSE, 0.0, 0.0, NULL, NULL, NULL, 4);


INSERT INTO garden (location_id, name, owner_user_id, size, id)
VALUES (1, 'Default User''s first Garden', 1, 2, 1);

INSERT INTO garden (location_id, name, owner_user_id, size, id)
VALUES (2, 'Default User''s Second Garden', 1, NULL, 2);

INSERT INTO garden (location_id, name, owner_user_id, size, id)
VALUES (3, 'Second User''s First Garden', 2, 0.5, 3);

INSERT INTO garden (location_id, name, owner_user_id, size, id)
VALUES (4, 'Second User''s Second Garden', 2, 0.5, 4);


INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (1, 'First plant of Default User''s first Garden', 1, NULL, 'Plant One', '2024-01-01', 1);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (2, 'Second plant of First User''s first Garden', 1, NULL, 'Plant Two', NULL, 2);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (3, 'Third plant of First User''s first Garden', 1, NULL, 'Plant Three', NULL, 3);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (4, 'Fourth plant of First User''s first Garden', 1, NULL, 'Plant Four', NULL, 4);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (5, 'Fifth plant of First User''s first Garden', 1, NULL, 'Plant Five', NULL, 5);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (6, 'Sixth plant of First User''s first Garden', 1, NULL, 'Plant Six', NULL, 6);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (7, 'Seventh plant of First User''s first Garden', 1, NULL, 'Plant Seven', NULL, 7);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (8, 'Eighth plant of First User''s first Garden', 1, NULL, 'Plant Eight', NULL, 8);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (9, 'Ninth plant of First User''s first Garden', 1, NULL, 'Plant Nine', NULL, 9);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (10, 'Tenth plant of First User''s first Garden', 1, NULL, 'Plant Ten', NULL, 10);

INSERT INTO plant (count, description, garden_id, image_file_name, name, planted_on, id)
VALUES (NULL, 'First plant of Second User''s first Garden', 2, NULL, 'Star Fruit', NULL, 11);
