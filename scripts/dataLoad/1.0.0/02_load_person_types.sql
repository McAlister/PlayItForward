
Insert Into person_type
(id, version, type, description)
  Select nextval('hibernate_sequence'),
    0,
    'WOMAN',
    'A future woman pro'
  Where Not Exists (Select type from person_type where type = 'WOMAN');

Insert Into person_type
(id, version, type, description)
  Select nextval('hibernate_sequence'),
    0,
    'EVENT_ORGANIZER',
    'An event organizer'
  Where Not Exists (Select type from person_type where type = 'EVENT_ORGANIZER');

Insert Into person_type
(id, version, type, description)
  Select nextval('hibernate_sequence'),
    0,
    'WIZARD',
    'Affiliated with WoTC'
  Where Not Exists (Select type from person_type where type = 'WIZARD');

Insert Into person_type
(id, version, type, description)
  Select nextval('hibernate_sequence'),
    0,
    'ARTIST',
    'Interested Artist'
  Where Not Exists (Select type from person_type where type = 'ARTIST');

Insert Into person_type
(id, version, type, description)
  Select nextval('hibernate_sequence'),
    0,
    'OTHER',
    'Supporter'
  Where Not Exists (Select type from person_type where type = 'OTHER');


