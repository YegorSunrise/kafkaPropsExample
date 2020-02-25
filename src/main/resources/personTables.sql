drop table person_b;
drop table person_a;

create table person_b
(
    id bigint not null
        constraint person_b_pkey
            primary key,
    name text,
    age int,
    consumer_meta text
);

create table person_a
(
    id bigint not null
        constraint person_a_pkey
            primary key,
    name text,
    age int,
    consumer_meta text
);


