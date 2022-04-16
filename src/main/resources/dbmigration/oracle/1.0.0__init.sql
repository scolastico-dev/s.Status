-- apply changes
create table check_downtime (
  id                            varchar2(40) not null,
  check_name                    varchar2(255) not null,
  from_time                     timestamp not null,
  until_time                    timestamp,
  yellow                        number(1) default 0 not null,
  messages                      varchar(1000) not null,
  constraint pk_check_downtime primary key (id)
);

create table check_maintenance (
  id                            varchar2(40) not null,
  check_name                    varchar2(255) not null,
  from_time                     timestamp not null,
  until_time                    timestamp,
  message                       varchar2(255) not null,
  constraint pk_check_maintenance primary key (id)
);

create table check_response (
  id                            varchar2(40) not null,
  check_name                    varchar2(255) not null,
  duration                      number(10) not null,
  at                            timestamp not null,
  constraint pk_check_response primary key (id)
);

create table started_actions (
  id                            varchar2(40) not null,
  check_name                    varchar2(255) not null,
  action                        varchar2(255) not null,
  at                            timestamp not null,
  constraint pk_started_actions primary key (id)
);

