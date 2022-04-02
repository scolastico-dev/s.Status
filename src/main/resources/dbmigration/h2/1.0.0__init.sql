-- apply changes
create table check_response (
  id                            uuid not null,
  at                            timestamp not null,
  duration                      integer not null,
  reachable                     boolean default false not null,
  constraint pk_check_response primary key (id)
);

