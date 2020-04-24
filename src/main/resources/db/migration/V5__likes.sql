create table likes(
	user_id bigint not null references usr,
	message_id bigint not null references messages,
	primary key(user_id, message_id)
);