CREATE TABLE VERIFICATION
(
	ID BIGINT AUTO_INCREMENT,
	PHONE_NUMBER CHAR(11),
	CODE CHAR(6),
	GMT_CREATE BIGINT,
	CONSTRAINT NOTIFICATION_PK
		PRIMARY KEY (ID)
);