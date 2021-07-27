-- apply changes
create table incident_archive (
  id                            bigint auto_increment not null,
  check_id                      varchar(100) not null,
  created_at                    bigint not null,
  ended_at                      bigint not null,
  reason                        varchar(1000) not null,
  status                        integer(1) not null,
  version                       bigint not null,
  constraint pk_incident_archive primary key (id)
);

create table planned_maintenance (
  id                            bigint auto_increment not null,
  check_id                      varchar(100) not null,
  starting_at                   bigint not null,
  ending_at                     bigint not null,
  reason                        varchar(1000) not null,
  version                       bigint not null,
  constraint pk_planned_maintenance primary key (id)
);

create table status_check_result (
  id                            bigint auto_increment not null,
  check_id                      varchar(100) not null,
  created_at                    bigint not null,
  check_duration                integer not null,
  status                        integer(1) not null,
  version                       bigint not null,
  constraint pk_status_check_result primary key (id)
);

