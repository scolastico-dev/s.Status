-- apply changes
create table incident_archive (
  id                            integer not null,
  check_id                      varchar(100) not null,
  created_at                    integer not null,
  ended_at                      integer not null,
  reason                        varchar(1000) not null,
  status                        integer(1) not null,
  version                       integer not null,
  constraint pk_incident_archive primary key (id)
);

create table planned_maintenance (
  id                            integer not null,
  check_id                      varchar(100) not null,
  starting_at                   integer not null,
  ending_at                     integer not null,
  reason                        varchar(1000) not null,
  version                       integer not null,
  constraint pk_planned_maintenance primary key (id)
);

create table status_check_result (
  id                            integer not null,
  check_id                      varchar(100) not null,
  created_at                    integer not null,
  check_duration                integer not null,
  status                        integer(1) not null,
  version                       integer not null,
  constraint pk_status_check_result primary key (id)
);

