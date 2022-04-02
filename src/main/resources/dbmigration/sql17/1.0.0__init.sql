-- apply changes
create table check_response (
  id                            uniqueidentifier not null,
  at                            datetime2 not null,
  duration                      integer not null,
  reachable                     bit default 0 not null,
  constraint pk_check_response primary key (id)
);

