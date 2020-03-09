create table question
(
	id INT auto_increment,
	title VARCHAR(50),
	description TEXT,
	gmt_create BIGINT,
	gmt_modified BIGINT,
	creator INT,
	comment_count INT default 0,
	view_count INT default 0,
	thumb_count INT default 0,
	tags VARCHAR(256),
	constraint question_pk
		primary key (id)
);

comment on table question is '问题表';