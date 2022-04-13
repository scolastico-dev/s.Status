-- apply changes
create table check_downtime (
  id                            uuid not null,
  check_name                    varchar(255) not null,
  from_time                     timestamptz not null,
  until_time                    timestamptz,
  constraint pk_check_downtime primary key (id)
);

create table check_maintenance (
  id                            uuid not null,
  check_name                    varchar(255) not null,
  from_time                     timestamptz not null,
  until_time                    timestamptz,
  message                       varchar(255) not null,
  constraint pk_check_maintenance primary key (id)
);

create table check_response (
  id                            uuid not null,
  check_name                    varchar(255) not null,
  duration                      integer not null,
  at                            timestamptz not null,
  constraint pk_check_response primary key (id)
);

