USE blogCms;

-- test
INSERT INTO `role` (`role`)
    VALUES ("ROLE_ADMIN");

INSERT INTO `role` (`role`)
    VALUES ("ROLE_CREATOR");

INSERT INTO category (category)
    VALUES ("New Product");

-- admin manual add
INSERT INTO `user` (username, password, isEnabled)
    VALUES ("Narish", "password", 1);

INSERT INTO userRole (userId, roleId)
    VALUES (1,1);

-- INSERT INTO post(title, body, isApproved, isStaticPage, createdOn, postOn, expireOn, userId)
--     values ("title", "body", 1, 1, now(), now(), null, 1);


UPDATE `user` SET 
    password = "$2a$10$OVxfKgo/KlnxfuZvTqhs8ejKFrQs9.0URnoNSLlW7fiSR/OCvmNry"
WHERE userId = 1;

SELECT * FROM blogCms.`user`;
SELECT * FROM `role`;
SELECT * FROM userRole;
SELECT * FROM blogCms.category;
SELECT * FROM blogCms.postCategory;

SELECT * FROM blogCms.post;
SELECT * FROM blogCms.post 
    WHERE (2020-10-25 15:47:00.000 <= NOW() AND 2020-10-25 15:50:00.000 >= NOW()) 
        AND isApproved != 0;