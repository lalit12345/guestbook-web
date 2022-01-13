-- CREATE database
CREATE DATABASE `guestbook`;

-- CREATE table
CREATE TABLE guestbook.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_id` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_email_id` (`email_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `delete_flag` tinyint(1) NOT NULL,
  `entry_image` varchar(255) DEFAULT NULL,
  `entry_text` varchar(255) DEFAULT NULL,
  `is_approved` tinyint(1) NOT NULL,
  `email_id` varchar(255) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_email_id` (`email_id`),
  CONSTRAINT `FK_email_id` FOREIGN KEY (`email_id`) REFERENCES `user` (`email_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;



-- INSERT data
--12345
INSERT INTO guestbook.user(`id`, `email_id`, `full_name`, `mobile_number`, `password`, `role`) VALUES ('1', 'test1@test1.com', 'Test1 Test1', '1111111111', '$2a$10$fCTy.4DoyDIno3QyvwEXIOtzuesZGVnRri8WdVZtCC6z9YDNVBOj6', 'USER');

--12345
INSERT INTO guestbook.user(`id`, `email_id`, `full_name`, `mobile_number`, `password`, `role`) VALUES ('2', 'test2@test2.com', 'Test2 Test2', '2222222222', '$2a$10$AcaO3qawvFYpTkOiwd3pW.owY7vjWarz6rxF26mzl7/cZ4Zl7RXEK', 'USER');

--admin
INSERT INTO guestbook.user(`id`, `email_id`, `full_name`, `mobile_number`, `password`, `role`) VALUES ('7', 'admin@admin.com', 'admin admin', '9999999999', '$2a$10$KH54Mf3nTsTLEKOWXfJE/eGdsNJGiE2C3hUORlhOufwmGuJa4t6lC', 'ADMIN');