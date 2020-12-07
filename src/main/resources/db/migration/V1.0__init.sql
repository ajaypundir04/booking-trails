CREATE TABLE `users` (
   `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `name` varchar(255) NOT NULL,
   `email` varchar(255) NOT NULL,
   `dob` DATE NOT NULL,
   `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
   `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
   UNIQUE (`email`)
 );

CREATE TABLE `trails` (
   `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `name` varchar(255) NOT NULL,
   `start_time` TIME NOT NULL ,
   `end_time` TIME NOT NULL ,
   `lower_age` INT NOT NULL,
   `upper_age` INT NOT NULL,
   `price` DOUBLE NOT NULL,
   `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
   `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
   UNIQUE (`name`)
 );

CREATE TABLE `bookings` (
   `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `trail_id` bigint NOT NULL,
   `user_id` bigint NOT NULL,
   `start_time` TIME NOT NULL ,
   `end_time` TIME NOT NULL,
   `booking_date` DATE NOT NULL,
   `booking_status` varchar(50),
   `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
   `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (`user_id`,`booking_date`,`start_time`,`end_time`,`trail_id`),
   CONSTRAINT `FK_booking_trails` FOREIGN KEY (`trail_id`) REFERENCES trails(`id`) ON UPDATE CASCADE,
   CONSTRAINT `FK_booking_users` FOREIGN KEY (`user_id`) REFERENCES users(`id`) ON UPDATE CASCADE
 );

 CREATE TABLE `bookings_users_mapping`(
   `booking_id` bigint NOT NULL,
   `user_id` bigint NOT NULL,
   `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
   `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `FK_bookings` FOREIGN KEY (`booking_id`) REFERENCES bookings(`id`) ON UPDATE CASCADE,
    CONSTRAINT `FK_users` FOREIGN KEY (`user_id`) REFERENCES users(`id`) ON UPDATE CASCADE
 );
