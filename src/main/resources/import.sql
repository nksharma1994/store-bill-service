insert into users (id, name, type, joining_date) values (1001, 'EMPLOYEE User', 'EMPLOYEE', '2017-11-15');
insert into users (id, name, type, joining_date) values (1002, 'AFFILIATE User', 'AFFILIATE', '2017-11-15');
insert into users (id, name, type, joining_date) values (1003, 'CUSTOMER User greater than 2years', 'CUSTOMER', '2017-11-15');
insert into users (id, name, type, joining_date) values (1004, 'CUSTOMER User less than 2years', 'CUSTOMER', '2020-11-15');

insert into categories (id, name) values (1, 'Grocery');
insert into categories (id, name) values (2, 'Other');

insert into products (id, name, price, category_id) values (1, 'Grocery Product 50', 50.0, 1);
insert into products (id, name, price, category_id) values (2, 'Other Product 80', 80.0, 2);
insert into products (id, name, price, category_id) values (3, 'Other Product 100', 100.0, 2);
