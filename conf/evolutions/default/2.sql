-- Association schema

-- !Ups

create table `association` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `name` TEXT NOT NULL,
                        `address1` TEXT NOT NULL,
                        `address2` TEXT NULL,
                        `city` TEXT NOT NULL,
                        `state` TEXT NOT NULL,
                        `zip` TEXT NOT NULL,
                        `contact_no` BIGINT NOT NULL,
                        `email` TEXT NOT NULL
)

-- !Downs
drop table `association`