CREATE TABLE ROLE
(
    ID        INT AUTO_INCREMENT,
    ROLE_TYPE VARCHAR(10) NOT NULL,
    ROLE_NAME VARCHAR(15) NOT NULL,
    CONSTRAINT ROLE_PK PRIMARY KEY (ID)
);
INSERT INTO ROLE(ROLE_TYPE, ROLE_NAME)
VALUES ('ADMIN', '管理员'),
       ('PUBLISH', '可发表问题用户'),
       ('ANSWER', '可回答问题用户')
;
CREATE TABLE TAG
(
    ID       INT AUTO_INCREMENT,
    TAG_TYPE VARCHAR(10) NOT NULL,
    TAG_NAME VARCHAR(15) NOT NULL,
    COUNT    BIGINT DEFAULT 0,
    CONSTRAINT TAG_PK PRIMARY KEY (ID)
);
CREATE TABLE USER
(
    ID           BIGINT AUTO_INCREMENT,
    ROLE_ID      INT,
    NAME         VARCHAR(50) NOT NULL UNIQUE,
    PASSWORD     VARCHAR(35),
    PHONE_NUMBER CHAR(11),
    TOKEN        CHAR(36),
    CREDIT       INT DEFAULT 0,
    BIO          VARCHAR(256),
    ACCOUNT_ID   VARCHAR(100),
    GMT_CREATE   BIGINT      NOT NULL,
    GMT_MODIFIED BIGINT      NOT NULL,
    AVATAR_URL   VARCHAR(100),
    CONSTRAINT FOREIGN KEY (ROLE_ID) REFERENCES ROLE (ID),
    CONSTRAINT USER_PK PRIMARY KEY (ID)
);
CREATE TABLE QUESTION
(
    ID            BIGINT AUTO_INCREMENT,
    TITLE         VARCHAR(50) NOT NULL,
    DESCRIPTION   TEXT        NOT NULL,
    GMT_CREATE    BIGINT      NOT NULL,
    GMT_MODIFIED  BIGINT      NOT NULL,
    CREATOR       BIGINT,
    COMMENT_COUNT INT DEFAULT 0,
    VIEW_COUNT    INT DEFAULT 0,
    CONSTRAINT FOREIGN KEY (CREATOR) REFERENCES USER (ID),
    CONSTRAINT QUESTION_PK PRIMARY KEY (ID)
);
CREATE TABLE QUESTION_TAG
(
    ID          BIGINT AUTO_INCREMENT,
    QUESTION_ID BIGINT,
    TAG_ID      INT,
    GMT_CREATE  BIGINT NOT NULL,
    CONSTRAINT FOREIGN KEY (QUESTION_ID) REFERENCES QUESTION (ID),
    CONSTRAINT FOREIGN KEY (TAG_ID) REFERENCES TAG (ID),
    CONSTRAINT QUESTION_TAG_PK PRIMARY KEY (ID)
);
CREATE TABLE COMMENT
(
    ID                BIGINT AUTO_INCREMENT,
    QUESTION_ID       BIGINT REFERENCES QUESTION (ID),
    REVIEWER          BIGINT REFERENCES USER (ID),
    CONTENT           VARCHAR(1024) NOT NULL,
    GMT_CREATE        BIGINT        NOT NULL,
    GMT_MODIFIED      BIGINT        NOT NULL,
    THUMB_COUNT       INT DEFAULT 0,
    SUB_COMMENT_COUNT INT DEFAULT 0,
    CONSTRAINT FOREIGN KEY (QUESTION_ID) REFERENCES QUESTION (ID),
    CONSTRAINT FOREIGN KEY (REVIEWER) REFERENCES USER (ID),
    CONSTRAINT COMMENT_PK PRIMARY KEY (ID)
);
CREATE TABLE SUB_COMMENT
(
    ID           BIGINT AUTO_INCREMENT,
    QUESTION_ID  BIGINT,
    COMMENT_ID   BIGINT,
    REVIEWER     BIGINT,
    CONTENT      VARCHAR(1024) NOT NULL,
    GMT_CREATE   BIGINT        NOT NULL,
    GMT_MODIFIED BIGINT        NOT NULL,
    CONSTRAINT FOREIGN KEY (QUESTION_ID) REFERENCES QUESTION (ID),
    CONSTRAINT FOREIGN KEY (COMMENT_ID) REFERENCES COMMENT (ID),
    CONSTRAINT FOREIGN KEY (REVIEWER) REFERENCES USER (ID),
    CONSTRAINT SUB_COMMENT_PK PRIMARY KEY (ID)
);
CREATE TABLE NOTIFICATION
(
    ID            BIGINT AUTO_INCREMENT,
    NOTIFIER      BIGINT,
    RECEIVER      BIGINT,
    STATUS        INT DEFAULT 0,
    TYPE          INT    NOT NULL,
    OUTER_ID      BIGINT,
    NOTIFIER_NAME VARCHAR(50),
    OUTER_TITLE   VARCHAR(256),
    GMT_CREATE    BIGINT NOT NULL,
    CONSTRAINT FOREIGN KEY (NOTIFIER) REFERENCES USER (ID),
    CONSTRAINT FOREIGN KEY (RECEIVER) REFERENCES USER (ID),
    CONSTRAINT FOREIGN KEY (OUTER_ID) REFERENCES QUESTION (ID),
    CONSTRAINT NOTIFICATION_PK PRIMARY KEY (ID)
);
CREATE TABLE VIEW_HISTORY
(
    ID           BIGINT AUTO_INCREMENT,
    VIEWER       BIGINT,
    QUESTION_ID  BIGINT,
    GMT_CREATE   BIGINT NOT NULL,
    GMT_MODIFIED BIGINT NOT NULL,
    CONSTRAINT FOREIGN KEY (VIEWER) REFERENCES USER (ID),
    CONSTRAINT FOREIGN KEY (QUESTION_ID) REFERENCES QUESTION (ID),
    CONSTRAINT VIEW_HISTORY_PK PRIMARY KEY (ID)
);
CREATE TABLE THUMB_HISTORY
(
    ID         BIGINT AUTO_INCREMENT,
    THUMB_USER BIGINT,
    COMMENT_ID BIGINT,
    GMT_CREATE BIGINT NOT NULL,
    CONSTRAINT FOREIGN KEY (THUMB_USER) REFERENCES USER (ID),
    CONSTRAINT FOREIGN KEY (COMMENT_ID) REFERENCES COMMENT (ID),
    CONSTRAINT THUMB_HISTORY_PK PRIMARY KEY (ID)
)