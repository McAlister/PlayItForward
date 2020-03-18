
Insert Into event_type
(id, version, type, description)
Select nextval('event_type_seq'),
       0,
       'SCO',
       'Star City Open'
Where Not Exists (Select type from event_type where type = 'SCO');

Update event
Set name = right(name, -3)
Where name like 'GP %';

