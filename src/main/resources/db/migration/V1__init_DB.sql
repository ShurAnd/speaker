create sequence hibernate_sequence start 1 increment 1;
create table messages 
					(id int8 not null,
					filename varchar(255),
					message varchar(2048) not null,
					tag varchar(255),
					user_id int8,
					primary key (id));
create table user_role 
					(user_id int8 not null,
					roles varchar(255));
create table usr 
					(id int8 not null,
					activation_code varchar(255),
					active boolean not null,
					mail varchar(255),
					password varchar(255) not null,
					username varchar(255) not null,
					primary key (id));
alter table if exists messages
			add constraint message_usr_fk foreign key (user_id) references usr;
alter table if exists user_role
			add constraint user_roles_usr_fk foreign key (user_id) references usr;