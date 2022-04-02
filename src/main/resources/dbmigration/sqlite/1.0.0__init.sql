-- apply changes
create table check_response (
  id                            varchar(40) not null,
  at                            timestamp not null,
  duration                      integer not null,
  reachable                     int default 0 not null,
  constraint pk_check_response primary key (id)
);

