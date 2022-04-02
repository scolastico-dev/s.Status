-- apply changes
create table check_response (
  id                            varchar(40) not null,
  at                            datetime(6) not null,
  duration                      integer not null,
  reachable                     tinyint(1) default 0 not null,
  constraint pk_check_response primary key (id)
);

