create table PLAIN_TEXT(
   id int auto_increment NOT NULL,
   text varchar(50),
   insert_ts timestamp default current_timestamp,
   update_ts timestamp default current_timestamp on update current_timestamp,
   primary key(id)
);

create table RESULT_TEXT(
    id int auto_increment NOT NULL,
    text varchar(50),
    insert_ts timestamp default current_timestamp,
    primary key(`id`)
);