USE blogCms;

-- test
INSERT INTO `role` (`role`)
    VALUES ("admin");

INSERT INTO category (category)
    VALUES ("New Product");

INSERT INTO `user` (username, password, isEnabled, firstName, lastName, email)
    VALUES ("name", "password", 1, null, null, null);

INSERT INTO post(title, body, isApproved, isStaticPage, createdOn, postOn, expireOn, userId)
    values ("title", "body", 1, 1, now(), now(), null, 1);

SELECT * FROM `user`;
SELECT * FROM post;
SELECT * FROM post
    WHERE postId = 1;