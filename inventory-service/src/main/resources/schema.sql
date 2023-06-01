create table PRODUCT_INVENTORY(
  id int not null AUTO_INCREMENT,
  product_id int not null ,
  product_name varchar(100) not null,
  available_products int,
  PRIMARY KEY ( id )
);