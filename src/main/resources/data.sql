-- INSERT data
--12345
INSERT INTO guestbook.user(`id`, `email_id`, `full_name`, `mobile_number`, `password`, `role`) VALUES ('1', 'test1@test1.com', 'Test1 Test1', '1111111111', '$2a$10$fCTy.4DoyDIno3QyvwEXIOtzuesZGVnRri8WdVZtCC6z9YDNVBOj6', 'USER');

--12345
INSERT INTO guestbook.user(`id`, `email_id`, `full_name`, `mobile_number`, `password`, `role`) VALUES ('2', 'test2@test2.com', 'Test2 Test2', '2222222222', '$2a$10$AcaO3qawvFYpTkOiwd3pW.owY7vjWarz6rxF26mzl7/cZ4Zl7RXEK', 'USER');

--admin
INSERT INTO guestbook.user(`id`, `email_id`, `full_name`, `mobile_number`, `password`, `role`) VALUES ('7', 'admin@admin.com', 'admin admin', '9999999999', '$2a$10$KH54Mf3nTsTLEKOWXfJE/eGdsNJGiE2C3hUORlhOufwmGuJa4t6lC', 'ADMIN');