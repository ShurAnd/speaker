create table subscribers(
	channel_id int8 not null,
	sub_id int8 not null,
	primary key(channel_id, sub_id)
);
