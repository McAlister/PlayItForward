

UPDATE event set event_code = 'gpokc17' where name = 'GP Oklahoma City';

UPDATE event set event_code = 'gpnj17-2' where name = 'GP New Jersey Limited';

UPDATE event set event_code = 'gplou17' where name = 'GP Louisville';

UPDATE event set event_code = 'gpsj17' where name = 'GP San Jose';

UPDATE event set event_code = 'gppit17' where name = 'GP Pittsburgh';

UPDATE event set event_code = 'gpvan17' where name = 'GP Vancouver';

UPDATE event set event_code = 'gpnja17' where name = 'GP New Jersey Standard';

UPDATE event set event_code = 'gporl17' where name = 'GP Orlando';

UPDATE event set event_code = 'gpric17' where name = 'GP Richmond';

UPDATE event set event_code = 'gpmon17' where name = 'GP Montreal';

UPDATE event set event_code = 'gpoma17' where name = 'GP Omaha';

UPDATE event set event_code = 'gplv17-legacy' where name = 'GP Las Vegas Legacy';

UPDATE event set event_code = 'gplv17-limited' where name = 'GP Las Vegas Limited';

UPDATE event set event_code = 'gplv17-modern' where name = 'GP Las Vegas Modern';

UPDATE event set event_code = 'gptor17' where name = 'GP Toronto';

UPDATE event set event_code = 'gpmin17' where name = 'GP Minneapolis';

UPDATE event set event_code = 'gpden17' where name = 'GP Denver';

UPDATE event set event_code = 'gpind17' where name = 'GP Indianapolis';

UPDATE event set event_code = 'gpdc' where name = 'GP Washington';

UPDATE event set event_code = 'gppho17' where name = 'GP Phoenix';

UPDATE event set event_code = 'gpatl17' where name = 'GP Atlanta';

UPDATE event set event_code = 'gppor17' where name = 'GP Portland';

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Houston',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-01-27',
    '2018-01-28'
  Where Not Exists (Select name from event where name = 'GP Houston');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Toronto',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-02-10',
    '2018-02-11'
  Where Not Exists (Select name from event where name = 'GP Toronto');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Memphis',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-02-24',
    '2018-02-25'
  Where Not Exists (Select name from event where name = 'GP Memphis');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Phoenix',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-03-17',
    '2018-03-18'
  Where Not Exists (Select name from event where name = 'GP Phoenix');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Seattle Legacy',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-04-07',
    '2018-04-08'
  Where Not Exists (Select name from event where name = 'GP Seattle Legacy');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Seattle Standard',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-04-07',
    '2018-04-08'
  Where Not Exists (Select name from event where name = 'GP Seattle Standard');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Hartford',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-04-14',
    '2018-04-15'
  Where Not Exists (Select name from event where name = 'GP Hartford');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Dallas',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-05-05',
    '2018-05-06'
  Where Not Exists (Select name from event where name = 'GP Dallas');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Las Vegas Limited',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-06-16',
    '2018-06-17'
  Where Not Exists (Select name from event where name = 'GP Las Vegas Limited');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Las Vegas Modern',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-06-16',
    '2018-06-17'
  Where Not Exists (Select name from event where name = 'GP Las Vegas Modern');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Pittsburgh',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-06-23',
    '2018-06-24'
  Where Not Exists (Select name from event where name = 'GP Pittsburgh');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Sao Paulo',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-07-07',
    '2018-07-08'
  Where Not Exists (Select name from event where name = 'GP Sao Paulo');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Sacramento',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-07-21',
    '2018-07-22'
  Where Not Exists (Select name from event where name = 'GP Sacramento');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Minneapolis',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-07-28',
    '2018-07-29'
  Where Not Exists (Select name from event where name = 'GP Minneapolis');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Orlando',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-08-11',
    '2018-08-12'
  Where Not Exists (Select name from event where name = 'GP Orlando');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Providence',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-08-18',
    '2018-08-19'
  Where Not Exists (Select name from event where name = 'GP Providence');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Los Angeles',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-08-18',
    '2018-08-19'
  Where Not Exists (Select name from event where name = 'GP Los Angeles');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Richmond Legacy',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-09-01',
    '2018-09-02'
  Where Not Exists (Select name from event where name = 'GP Richmond Legacy');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Richmond Standard',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-09-01',
    '2018-09-02'
  Where Not Exists (Select name from event where name = 'GP Richmond Standard');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Montreal',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-10-06',
    '2018-10-07'
  Where Not Exists (Select name from event where name = 'GP Montreal');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Mexico City',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-10-06',
    '2018-10-07'
  Where Not Exists (Select name from event where name = 'GP Mexico City');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP New Jersey',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-10-27',
    '2018-10-28'
  Where Not Exists (Select name from event where name = 'GP New Jersey');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Atlanta',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-11-03',
    '2018-11-04'
  Where Not Exists (Select name from event where name = 'GP Atlanta');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Milwaukee',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-11-17',
    '2018-11-18'
  Where Not Exists (Select name from event where name = 'GP Milwaukee');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Portland',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-12-08',
    '2018-12-09'
  Where Not Exists (Select name from event where name = 'GP Portland');
