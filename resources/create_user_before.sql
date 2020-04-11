delete from messages;
delete from user_role;
delete from usr;

insert into usr (id, active, username, password) 
	values 
		(1, true, 'qwe', '$2a$07$4zIrWCDYjnOwNEVxLX0eFePYf2rjDuWOBx3QHcuHxsD0QY.1qKgBK'),
		(2, true, 'ewq', '$2a$07$4zIrWCDYjnOwNEVxLX0eFePYf2rjDuWOBx3QHcuHxsD0QY.1qKgBK');
		
insert into user_role (user_id, roles)
	values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');