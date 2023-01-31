create table users (id int primary key,
user_name varchar(50) not null,
primary_contact_number varchar(10) not null);

create table orders (id int primary key,
user_id int not null,
item_name varchar(50) not null,
constraint fk_users_orders
foreign key (user_id)
references users(id));

insert into public.users
values (1, 'John', '1234567'),
(2, 'Jenny', '5697891'),
(3, 'Vikram', '8963578'),
(4, 'Tim', '7895631');

insert into public.orders
values (10, 1, 'Phone Charger'),
(11, 1, 'Headphones'),
(12, 2, 'OLED TV'),
(13, 3, 'Gaming Laptop');