create table event (
  id              varchar(255) not null,
  revision        integer not null,
  name            varchar(255),
  eventdate       date,
  tags            long varchar,
  notes           long varchar,
  primary key(id)
)
