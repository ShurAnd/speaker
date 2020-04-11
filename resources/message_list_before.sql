delete from messages;

insert into messages (id, text, tag, user_id) values
(1, 'msg1', 'tag1', 1),
(2, 'msg2', 'tag2', 1),
(3, 'msg3', 'tag3', 1),
(4, 'msg4', 'tag2', 2);

alter sequence hibernate_sequence restart with 10;
