DROP TABLE IF EXISTS user;

CREATE TABLE IF NOT EXISTS user ( 
  user_id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  email_id varchar(100) NOT NULL,
  password varchar(200) NOT NULL,
  full_name varchar(50) NOT NULL,
  mobile_number varchar(11) NOT NULL,
  entry_text varchar(100) DEFAULT NULL,
  entry_image blob,
  role varchar(10) NOT NULL,
  is_approved boolean,
  delete_flag boolean
);