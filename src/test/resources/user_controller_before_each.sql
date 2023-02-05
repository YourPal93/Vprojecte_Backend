delete from roles;
delete from posts;
delete from users;
delete from user_plates;


insert into users(id, name, password, login, email, birthdate) values
(1,
'TEST_NAME',
'$2a$10$yRVxhm9w1YU/PvcUrW057uyaiLJQNqeComA5kF26b.M24BcOZzING',
'TEST_LOGIN_UNIQUE',
'TEST_EMAIL_UNIQUE',
'2000-01-01');

insert into roles(id, name, user_id, role_type) values
(1, 'ROLE_USER', 1, 0);
