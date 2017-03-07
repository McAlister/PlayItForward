
Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Louisville',
    'gplou17',
    (Select id from event_organizer where name = 'Star City Games'),
    '2017-01-07',
    '2017-01-08'
  Where Not Exists (Select name from event where name = 'GP Louisville');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP San Jose',
    'gpsj17',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2017-01-28',
    '2017-01-29'
  Where Not Exists (Select name from event where name = 'GP San Jose');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Pittsburgh',
    'gppit17',
    (Select id from event_organizer where name = 'Pastimes'),
    '2017-02-11',
    '2017-02-12'
  Where Not Exists (Select name from event where name = 'GP Pittsburgh');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Vancouver',
    'gpvan17',
    (Select id from event_organizer where name = 'Game Keeper'),
    '2017-02-18',
    '2017-02-19'
  Where Not Exists (Select name from event where name = 'GP Vancouver');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP New Jersey Standard',
    'gpnja17',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2017-03-11',
    '2017-03-12'
  Where Not Exists (Select name from event where name = 'GP New Jersey Standard');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Orlando',
    'gporl17',
    (Select id from event_organizer where name = 'Star City Games'),
    '2017-03-25',
    '2017-03-26'
  Where Not Exists (Select name from event where name = 'GP Orlando');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Richmond',
    null,
    (Select id from event_organizer where name = 'Star City Games'),
    '2017-05-06',
    '2017-05-07'
  Where Not Exists (Select name from event where name = 'GP Richmond');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Montreal',
    null,
    (Select id from event_organizer where name = 'Game Keeper'),
    '2017-05-20',
    '2017-05-21'
  Where Not Exists (Select name from event where name = 'GP Montreal');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Omaha',
    null,
    (Select id from event_organizer where name = 'Cascade Games'),
    '2017-06-03',
    '2017-06-04'
  Where Not Exists (Select name from event where name = 'GP Omaha');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Las Vegas Legacy',
    null,
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2017-06-15',
    '2017-06-16'
  Where Not Exists (Select name from event where name = 'GP Las Vegas Legacy');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Las Vegas Limited',
    null,
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2017-06-16',
    '2017-06-17'
  Where Not Exists (Select name from event where name = 'GP Las Vegas Limited');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
Select nextval('hibernate_sequence'),
  0,
  (Select id from event_type where type = 'GP'),
  'GP Las Vegas Modern',
  null,
  (Select id from event_organizer where name = 'Channel Fireball'),
  '2017-06-17',
  '2017-06-18'
  Where Not Exists (Select name from event where name = 'GP Las Vegas Modern');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Toronto',
    null,
    (Select id from event_organizer where name = 'Face to Face Games'),
    '2017-07-22',
    '2017-07-23'
  Where Not Exists (Select name from event where name = 'GP Toronto');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Minneapolis',
    null,
    (Select id from event_organizer where name = 'Pastimes'),
    '2017-08-05',
    '2017-08-06'
  Where Not Exists (Select name from event where name = 'GP Minneapolis');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Denver',
    null,
    (Select id from event_organizer where name = 'Cascade Games'),
    '2017-08-19',
    '2017-08-20'
  Where Not Exists (Select name from event where name = 'GP Denver');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Indianapolis',
    null,
    (Select id from event_organizer where name = 'Pastimes'),
    '2017-08-26',
    '2017-08-27'
  Where Not Exists (Select name from event where name = 'GP Indianapolis');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Washington',
    null,
    (Select id from event_organizer where name = 'Star City Games'),
    '2017-09-02',
    '2017-09-03'
  Where Not Exists (Select name from event where name = 'GP Washington');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Phoenix',
    null,
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2017-10-28',
    '2017-10-29'
  Where Not Exists (Select name from event where name = 'GP Phoenix');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Atlanta',
    null,
    (Select id from event_organizer where name = 'Star City Games'),
    '2017-11-11',
    '2017-11-12'
  Where Not Exists (Select name from event where name = 'GP Atlanta');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Portland',
    null,
    (Select id from event_organizer where name = 'Cascade Games'),
    '2017-11-18',
    '2017-11-19'
  Where Not Exists (Select name from event where name = 'GP Portland');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Oklahoma City',
    null,
    (Select id from event_organizer where name = 'Cascade Games'),
    '2017-12-09',
    '2017-12-10'
  Where Not Exists (Select name from event where name = 'GP Oklahoma City');

Insert Into event
(id, version, type_id, name, event_code, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP New Jersey Limited',
    null,
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2017-12-16',
    '2017-12-17'
  Where Not Exists (Select name from event where name = 'GP New Jersey Limited');


