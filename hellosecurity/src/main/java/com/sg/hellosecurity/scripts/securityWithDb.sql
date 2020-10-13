drop database if exists securitywithdb;
create database securitywithdb;

use securitywithdb;

create table `user`(
    `id` int primary key auto_increment,
    `username` varchar(30) not null unique,
    `password` varchar(100) not null,
    `enabled` boolean not null
);

create table `role`(
    `id` int primary key auto_increment,
    `role` varchar(30) not null
);

create table `user_role`(
    `user_id` int not null,
    `role_id` int not null,
    primary key(`user_id`,`role_id`),
    foreign key (`user_id`) 
        references `user`(`id`),
    foreign key (`role_id`) 
        references `role`(`id`)
);

insert into `user`(`id`,`username`,`password`,`enabled`)
    values
        (1,"admin", "password", true),
        (2,"user","password", true);

insert into `role`(`id`,`role`)
    values
        (1,"ROLE_ADMIN"), 
        (2,"ROLE_USER");
    
insert into `user_role`(`user_id`,`role_id`)
    values
        (1,1),
        (1,2),
        (2,2);

-- Use from your own machine
UPDATE user SET 
    password = '$2a$10$2rFvUu2vo50WTZnRWhMHw.kqn0Du7GLBw3ghOPMDZ9qWvfDWZkyn.' 
    WHERE id = 1;

UPDATE user SET 
    password = '$2a$10$2rFvUu2vo50WTZnRWhMHw.kqn0Du7GLBw3ghOPMDZ9qWvfDWZkyn.' 
    WHERE id = 2;