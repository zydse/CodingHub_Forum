create table comment
(
	id BIGINT auto_increment,
	parent_id BIGINT not null,
	type INT,
	reviewer BIGINT,
	gmt_create BIGINT,
	gmt_modified BIGINT,
	thumb_count INT default 0,
	content VARCHAR(1024),
	constraint comment_pk
		primary key (id)
);