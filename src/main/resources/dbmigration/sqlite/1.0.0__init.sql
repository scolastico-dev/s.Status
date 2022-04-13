-- apply changes
create table check_downtime (
  id                            varchar(40) not null,
  check_name                    varchar(255) not null,
  from_time                     timestamp not null,
  until_time                    timestamp,
  constraint pk_check_downtime primary key (id)
);

create table check_maintenance (
  id                            varchar(40) not null,
  check_name                    varchar(255) not null,
  from_time                     timestamp not null,
  until_time                    timestamp,
  message                       varchar(255) not null,
  constraint pk_check_maintenance primary key (id)
);

create table check_response (
  id                            varchar(40) not null,
  check_name                    varchar(255) not null,
  duration                      integer not null,
  at                            timestamp not null,
  constraint pk_check_response primary key (id)
);

