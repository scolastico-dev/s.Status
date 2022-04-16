-- apply changes
create table check_downtime (
  id                            uuid not null,
  check_name                    varchar(255) not null,
  from_time                     timestamptz not null,
  until_time                    timestamptz,
  yellow                        boolean default false not null,
  messages                      varchar[] not null,
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

create table started_actions (
  id                            uuid not null,
  check_name                    varchar(255) not null,
  action                        varchar(255) not null,
  at                            timestamptz not null,
  constraint pk_started_actions primary key (id)
);

