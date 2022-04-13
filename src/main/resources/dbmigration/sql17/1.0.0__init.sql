-- apply changes
create table check_downtime (
  id                            uniqueidentifier not null,
  check_name                    nvarchar(255) not null,
  from_time                     datetime2 not null,
  until_time                    datetime2,
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

