-- CREATE database
CREATE DATABASE `guestbook`;

-- CREATE table
CREATE TABLE guestbook.`user` (
  `user_id` int(11) NOT NULL,
  `email_id` varchar(100) NOT NULL,
  `password` varchar(200) NOT NULL,
  `full_name` varchar(50) NOT NULL,
  `mobile_number` varchar(11) NOT NULL,
  `entry_text` varchar(100) DEFAULT NULL,
  `entry_image` blob,
  `role` varchar(10) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_id_UNIQUE` (`email_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- INSERT data
INSERT INTO guestbook.`user` (`user_id`, `email_id`, `password`, `full_name`, `mobile_number`, `entry_text`, `role`) VALUES (1, 'john@test.com', '$2a$12$.36i0tr/CudnMmCwVcZ18.rLlumLDCn5Iyfi10nHch2TMiVNi/a4W', 'John P', '01231231231', 'Entry text 1', 'USER');
INSERT INTO guestbook.`user` (`user_id`, `email_id`, `password`, `full_name`, `mobile_number`, `entry_text`, `role`) VALUES (2, 'alex@test.com', '$2a$12$v3c14F9Jlefoy7fpBJfRYOqo0MrZ.FUhSeL84HLWnlU4nrZnmP5du', 'Alex N', '03213213213', 'Entry text 2', 'USER');
INSERT INTO guestbook.`user` (`user_id`, `email_id`, `password`, `full_name`, `mobile_number`, `role`) VALUES (3, 'admin@test.com', '$2a$12$fwXnsJtKR2Y97oZNYkwLQuVSW7URNAqIVRyIiTe5kLJXLcXkPzMDW', 'Admin', '01111111111', 'ADMIN');