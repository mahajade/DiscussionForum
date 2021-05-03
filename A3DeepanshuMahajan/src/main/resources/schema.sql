drop table if exists users; 
drop table if exists authorities;
CREATE TABLE users(
	username varchar(50) not null primary key ,
	password varchar(100) not null,
	enabled boolean not null
);

CREATE TABLE authorities(
	authority varchar(50) not null,
	username varchar(50) not null, 
	foreign key (username) references users(username)
);

CREATE TABLE posts(
	id int primary key auto_increment not null,
	username varchar(50) not null,
	message varchar(500) not null,
	date date not null,
	time time not null
);

CREATE TABLE replies(
	id int not null,
	username varchar(50) not null,
	reply varchar(500) not null,
	date date not null,
	time time not null
);


