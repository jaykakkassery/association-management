-- user_performance schema

-- !Ups

create table `user_performance` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    `user_id` BIGINT NOT NULL,
                                    `association_id` BIGINT NOT NULL,
                                    `event_id` BIGINT NOT NULL,
                                    `performance_name` VARCHAR(200) NOT NULL,
                                    `performance_order` INT NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                                    FOREIGN KEY (association_id) REFERENCES association(id) ON DELETE CASCADE,
                                    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE)


-- !Downs
drop table `user_performance`