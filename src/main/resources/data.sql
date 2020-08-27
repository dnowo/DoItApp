--INSERT INTO ROLES(name) VALUES ('ROLE_ADMIN');
--INSERT INTO ROLES(name) VALUES ('ROLE_USER');
--INSERT INTO USERS(email, password, username) VALUES ('danielnowo@gmail.com', '$2y$12$zczfI7ni0EceeuOW9s9CLOvPmIiiYyMbI7Rv2x6.3gOkuXTGBaZyW', 'danielnowoadmin');
--INSERT INTO USERS(email, password, username) VALUES ('danielnowo@gmaile.com', '$2y$12$zczfI7ni0EceeuOW9s9CLOvPmIiiYyMbI7Rv2x6.3gOkuXTGBaZyW', 'danielnowouser');
--INSERT INTO USER_ROLES(user_id, role_id) VALUES (1,1);
--INSERT INTO USER_ROLES(user_id, role_id) VALUES (2,2);

insert into jobs(
    priority, title, description, deadline, notification
) values (
    1,
    'A very important work to do!',
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit,
    sed do eiusmod tempor incididunt ut labore et dolore magna
    aliqua. Ut enim ad minim veniam, quis nostrud exercitation
    ullamco laboris nisi ut aliquip ex ea commodo consequat.
    Duis aute irure dolor in reprehenderit in voluptate velit
    esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
    occaecat cupidatat non proident, sunt in culpa qui officia
    deserunt mollit anim id est laborum.',
    '2020-08-09 22:29',
    0
);

insert into jobs(
    priority, title, description, deadline, notification
) values (
    1,
    'Learn Java!',
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit,
    sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
    '2020-12-03 12:19',
    0
);

insert into jobs(
    priority, title, description, deadline, notification
) values (
    2,
    'Learn SpringBoot!',
    'Excepteur sint
    occaecat cupidatat non proident, sunt in culpa qui officia
    deserunt mollit anim id est laborum.',
    '2019-04-25 03:29',
    0
);

insert into jobs(
    priority, title, description, deadline, notification
) values (
    1,
    'Learn SpringBoot!',
    'Excepteur sint
    occaecat cupidatat non proident, sunt in culpa qui officia
    deserunt mollit anim id est laborum.',
    '2020-08-09 23:42',
    0
);
insert into jobs(
    priority, title, description, deadline, notification
) values (
    1,
    'Learn Java1!',
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit,
    sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
    '2020-11-03 12:19',
    0
);

insert into jobs(
    priority, title, description, deadline, notification
) values (
    1,
    'Learn Java2!',
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit,
    sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
    '2020-10-03 12:19',
    0
);

insert into jobs(
    priority, title, description, deadline, notification, ended
) values (
    1,
    'Learn Java3!',
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit,
    sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
    '2020-09-03 12:19',
    0,
    0
);

insert into jobs(
    priority, title, description, deadline, notification, ended
) values (
    1,
    'Learn Java4!',
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit,
    sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
    '2020-08-20 12:19',
    0,
    0
);