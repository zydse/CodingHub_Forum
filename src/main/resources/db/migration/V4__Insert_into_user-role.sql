INSERT INTO `USER_ROLE`(`USER_ID`, `ROLE_ID`, `GMT_CREATE`)
VALUES (1, 1, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (2, 2, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (3, 3, UNIX_TIMESTAMP(SYSDATE()) * 1000);

INSERT INTO `ROLE_PERMISSION`(`ROLE_ID`, `PERMISSION_ID`, `GMT_CREATE`)
VALUES (1, 15, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (1, 16, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (1, 17, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (1, 18, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (2, 18, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (2, 3, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (2, 4, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (2, 6, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (2, 9, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (2, 11, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (2, 12, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (3, 18, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (3, 6, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (3, 9, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (3, 11, UNIX_TIMESTAMP(SYSDATE()) * 1000),
       (3, 12, UNIX_TIMESTAMP(SYSDATE()) * 1000);