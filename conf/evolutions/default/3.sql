-- Association schema

-- !Ups

create table `user_association` (
                                    `user_id` BIGINT NOT NULL,
                                    `association_id` BIGINT NOT NULL,
                                    PRIMARY KEY(user_id, association_id),
                                    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                                    FOREIGN KEY (association_id) REFERENCES association(id) ON DELETE CASCADE)

-- !Downs
drop table `user_association`