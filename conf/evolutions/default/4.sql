-- user_performance schema

-- !Ups

create table `event` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `name` VARCHAR(200) NOT NULL)

-- !Downs
drop table `event`