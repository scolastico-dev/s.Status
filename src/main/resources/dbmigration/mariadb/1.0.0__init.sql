-- apply changes
create table check_downtime (
  id                            varchar(40) not null,
  check_name                    varchar(255) not null,
  from_time                     datetime(6) not null,
  until_time                    datetime(6),
  yellow                        tinyint(1) default 0 not null,
  messages                      varchar(1000) not null,
  constraint pk_check_downtime primary key (id)
);

create table check_maintenance (
  id                            varchar(40) not null,
  check_name                    varchar(255) not null,
  from_time                     datetime(6) not null,
  until_time                    datetime(6),
  message                       varchar(255) not null,
  constraint pk_check_maintenance primary key (id)
);

create table check_response (
  id                            varchar(40) not null,
  check_name                    varchar(255) not null,
  duration                      integer not null,
  at                            datetime(6) not null,
  constraint pk_check_response primary key (id)
);

create table started_actions (
  id                            varchar(40) not null,
  check_name                    varchar(255) not null,
  action                        varchar(255) not null,
  at                            datetime(6) not null,
  constraint pk_started_actions primary key (id)
);

