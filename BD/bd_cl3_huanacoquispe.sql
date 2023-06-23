CREATE DATABASE bd_cl3_dawi_huanacoquispe;
USE bd_cl3_dawi_huanacoquispe;

create table Product (
id int primary key auto_increment,
name varchar(50) not null,
description varchar(350) not null,
date_reg date default(current_date()) not null
);

select * from Product;