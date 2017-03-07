
Insert Into event_type
(id, version, type, description)
  Select nextval('hibernate_sequence'),
    0,
    'GP',
    'Grand Prix'
  Where Not Exists (Select type from event_type where type = 'GP');

Insert Into event_type
(id, version, type, description)
  Select nextval('hibernate_sequence'),
    0,
    'OTHER',
    'Unknown Type'
  Where Not Exists (Select type from event_type where type = 'OTHER');


