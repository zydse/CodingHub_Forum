create table USER
(
	ID BIGINT auto_increment,
	ACCOUNT_ID VARCHAR(100),
	NAME VARCHAR(50),
	TOKEN CHAR(36),
	GMT_CREATE BIGINT,
	GMT_MODIFIED BIGINT,
	BIO VARCHAR(256),
	constraint USER_PK
		primary key (ID)
);

comment on table USER is '用户表';