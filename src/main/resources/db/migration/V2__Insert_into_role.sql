INSERT INTO `ROLE`(`ROLE_TYPE`, `ROLE_NAME`)
VALUES ('ADMIN', '管理员'),
       ('ADVANCE', '可回答问题的高级用户'),
       ('PRIMARY', '可提问的初级用户');

INSERT INTO `PERMISSION`(`PER_CODE`, `GMT_CREATE`)
VALUES ("user:delete", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("user:update", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("question:create", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("question:update", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("question:delete", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("question:retreive", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("question:quality", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("question:top", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("comment:create", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("comment:delete", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("comment:retreive", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("comment:thumb", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("profile:retrieve", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("profile:update", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("profile:retrieve", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("user:*", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("question:*", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("comment:*", UNIX_TIMESTAMP(SYSDATE()) * 1000),
       ("profile:*", UNIX_TIMESTAMP(SYSDATE()) * 1000);

INSERT INTO `TAG` (`TAG_TYPE`, `TAG_NAME`, `COUNT`)
VALUES ('language', 'Java', 0),
       ('language', 'C++', 0),
       ('language', 'C', 0),
       ('language', 'Python', 0),
       ('language', 'JavaScript', 0),
       ('framework', 'Spring', 0),
       ('framework', 'Spring Boot', 0),
       ('framework', 'MyBatis', 0),
       ('framework', 'Django', 0),
       ('framework', 'Flask', 0),
       ('database', 'MySQL', 0),
       ('database', 'SQLServer', 0),
       ('database', 'PostgreSQL', 0),
       ('IDE', 'IntelliIDEA', 0),
       ('IDE', 'Eclipse', 0),
       ('IDE', 'VisualStudio', 0),
       ('IDE', 'NetBean', 0);