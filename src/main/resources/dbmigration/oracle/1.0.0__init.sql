-- apply changes
create table check_response (
  id                            varchar2(40) not null,
  at                            timestamp not null,
  duration                      number(10) not null,
  reachable                     number(1) default 0 not null,
  constraint pk_check_response primary key (id)
);

