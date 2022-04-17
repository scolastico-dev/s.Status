-- apply changes
create table average_cache (
  id                            uniqueidentifier not null,
  check_name                    nvarchar(255) not null,
  uptime                        integer not null,
  duration                      integer not null,
  at                            datetime2 not null,
  timezone                      integer not null,
  constraint pk_average_cache primary key (id)
);

create table check_downtime (
  id                            uniqueidentifier not null,
  check_name                    nvarchar(255) not null,
  from_time                     datetime2 not null,
  until_time                    datetime2,
  yellow                        bit default 0 not null,
  messages                      varchar(1000) not null,
  constraint pk_check_downtime primary key (id)
);

create table check_maintenance (
  id                            uniqueidentifier not null,
  check_name                    nvarchar(255) not null,
  from_time                     datetime2 not null,
  until_time                    datetime2,
  message                       nvarchar(255) not null,
  constraint pk_check_maintenance primary key (id)
);

create table check_response (
  id                            uniqueidentifier not null,
  check_name                    nvarchar(255) not null,
  duration                      integer not null,
  at                            datetime2 not null,
  constraint pk_check_response primary key (id)
);

create table started_actions (
  id                            uniqueidentifier not null,
  check_name                    nvarchar(255) not null,
  action                        nvarchar(255) not null,
  at                            datetime2 not null,
  constraint pk_started_actions primary key (id)
);

