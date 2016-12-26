
CREATE TABLE event_organizer (
  id          serial primary key,
  name        varchar(64) not null,
  created     timestamp not null default current_timestamp,
  updated     timestamp not null default current_timestamp
);

Create Trigger event_organizer_update
Before Update on event_organizer
For Each Row Execute Procedure update_timestamp();

