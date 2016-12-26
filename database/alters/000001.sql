
CREATE TABLE sysconfig (
  id          serial primary key,
  name        varchar(64) not null,
  value       varchar(32) not null,
  description varchar(256) not null,
  created     timestamp not null default current_timestamp,
  updated     timestamp not null default current_timestamp
);

CREATE UNIQUE INDEX sysconfig_name_idx ON sysconfig (name);

Create or Replace function update_timestamp()
  returns trigger as $$
begin
  if row(new.*) is distinct from row(old.*) then
    new.updated = now();
    return new;
  else
    return old;
  end if;
end;
$$ language 'plpgsql';

Create Trigger sysconfig_update
Before Update on sysconfig
For Each Row Execute Procedure update_timestamp();

Insert Into sysconfig
(name, value, description)
Values
  ('last alter', '000001', 'The version number of the last alter that ran.'),
  ('software version', '1.0.0', 'The supported version of the software for this DB.');
