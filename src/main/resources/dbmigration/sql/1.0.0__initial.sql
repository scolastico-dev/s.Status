-- apply changes
create table incident_archive (
  id                            numeric(19) not null,
  check_id                      nvarchar(100) not null,
  created_at                    numeric(19) not null,
  ended_at                      numeric(19) not null,
  reason                        nvarchar(1000) not null,
  status                        integer not null,
  version                       numeric(19) not null,
  constraint pk_incident_archive primary key (id)
);
create sequence incident_archive_seq as bigint start with 1 increment by 50;

create table planned_maintenance (
  id                            numeric(19) not null,
  check_id                      nvarchar(100) not null,
  starting_at                   numeric(19) not null,
  ending_at                     numeric(19) not null,
  reason                        nvarchar(1000) not null,
  version                       numeric(19) not null,
  constraint pk_planned_maintenance primary key (id)
);
create sequence planned_maintenance_seq as bigint start with 1 increment by 50;

create table status_check_result (
  id                            numeric(19) not null,
  check_id                      nvarchar(100) not null,
  created_at                    numeric(19) not null,
  check_duration                integer not null,
  status                        integer not null,
  version                       numeric(19) not null,
  constraint pk_status_check_result primary key (id)
);
create sequence status_check_result_seq as bigint start with 1 increment by 50;

