USE moviedb;
drop table if exists employees;
CREATE TABLE employees(
	email varchar(50) primary key,
	password varchar(20) not null,
	fullname varchar(100)
);
insert INTO employees VALUES('classta@email.edu', 'classta','TA CS122B');