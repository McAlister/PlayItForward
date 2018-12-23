

Update event set format = 'TBD' where format is null;

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Vancouver',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2018-12-29',
    '2018-12-30',
    'Limited'
  Where Not Exists (Select name from event where name = 'GP Vancouver' And start_date > '2018-12-29');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Oakland',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-01-05',
    '2019-01-06',
    'Modern'
  Where Not Exists (Select name from event where name = 'GP Oakland' And start_date > '2019-01-05');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Prague',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-01-12',
    '2019-01-13',
    'Limited'
  Where Not Exists (Select name from event where name = 'GP Prague' And start_date > '2019-01-12');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP New Jersey',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-01-26',
    '2019-01-27',
    'Limited'
  Where Not Exists (Select name from event where name = 'GP New Jersey' And start_date > '2019-01-26');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Toronto',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-02-09',
    '2019-02-10',
    'Modern'
  Where Not Exists (Select name from event where name = 'GP Toronto' And start_date > '2019-02-09');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Memphis',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-02-16',
    '2019-02-17',
    'Standard'
  Where Not Exists (Select name from event where name = 'GP Memphis' And start_date > '2019-02-16');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Cleveland',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-02-23',
    '2019-02-24',
    'Standard'
  Where Not Exists (Select name from event where name = 'GP Cleveland' And start_date > '2019-02-23');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Cleveland',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-02-23',
    '2019-02-24',
    'Limited'
  Where Not Exists (Select name from event where name = 'GP Cleveland' And start_date > '2019-02-23');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Los Angeles',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-03-02',
    '2019-03-03',
    'Modern'
  Where Not Exists (Select name from event where name = 'GP Los Angeles' And start_date > '2019-03-02');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Tampa Bay',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-03-16',
    '2019-03-17',
    'Modern'
  Where Not Exists (Select name from event where name = 'GP Tampa Bay' And start_date > '2019-03-16');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Calgary',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-03-30',
    '2019-04-01',
    'Modern'
  Where Not Exists (Select name from event where name = 'GP Calgary' And start_date > '2019-03-30');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Niagara Falls',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-04-20',
    '2019-04-21',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Niagara Falls' And start_date > '2019-04-20');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP London',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-04-27',
    '2019-04-28',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP London' And start_date > '2019-04-27');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Madison',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-05-11',
    '2019-05-12',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Madison' And start_date > '2019-05-11');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Providence',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-05-25',
    '2019-05-26',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Providence' And start_date > '2019-05-25');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Kansas City',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-06-01',
    '2019-06-02',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Kansas City' And start_date > '2019-06-01');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Washington DC',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-06-15',
    '2019-06-16',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Washington DC' And start_date > '2019-06-15');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Seattle',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-06-22',
    '2019-06-23',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Seattle' And start_date > '2019-06-22');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Dallas-Fort Worth',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-06-29',
    '2019-06-30',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Dallas-Fort Worth' And start_date > '2019-06-29');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Detroit',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-07-13',
    '2019-07-14',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Detroit' And start_date > '2019-07-13');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Denver',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-07-20',
    '2019-07-21',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Detroit' And start_date > '2019-07-20');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'GP Minneapolis',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-08-10',
    '2019-08-11',
    'TBD'
  Where Not Exists (Select name from event where name = 'GP Minneapolis' And start_date > '2019-08-10');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Las Vegas',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-08-23',
    '2019-08-24',
    'TBD'
  Where Not Exists (Select name from event where name = 'Las Vegas' And start_date > '2019-08-23');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Las Vegas',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-08-24',
    '2019-08-25',
    'TBD'
  Where Not Exists (Select name from event where name = 'Las Vegas' And start_date > '2019-08-24');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Indianapolis',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-09-07',
    '2019-09-08',
    'TBD'
  Where Not Exists (Select name from event where name = 'Indianapolis' And start_date > '2019-09-07');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Ghent',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-09-14',
    '2019-09-15',
    'TBD'
  Where Not Exists (Select name from event where name = 'Ghent' And start_date > '2019-09-14');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Atlanta',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-09-21',
    '2019-09-22',
    'TBD'
  Where Not Exists (Select name from event where name = 'Atlanta' And start_date > '2019-09-21');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Montréal',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-10-05',
    '2019-10-06',
    'TBD'
  Where Not Exists (Select name from event where name = 'Montréal' And start_date > '2019-10-05');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Utrecht',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-10-12',
    '2019-10-13',
    'TBD'
  Where Not Exists (Select name from event where name = 'Utrecht' And start_date > '2019-10-12');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Phoenix',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-10-25',
    '2019-10-26',
    'TBD'
  Where Not Exists (Select name from event where name = 'Phoenix' And start_date > '2019-10-25');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Richmond',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-11-09',
    '2019-11-10',
    'TBD'
  Where Not Exists (Select name from event where name = 'Richmond' And start_date > '2019-11-09');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Columbus',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-11-16',
    '2019-11-17',
    'TBD'
  Where Not Exists (Select name from event where name = 'Columbus' And start_date > '2019-11-16');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Oklahoma City',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-12-14',
    '2019-12-15',
    'TBD'
  Where Not Exists (Select name from event where name = 'Oklahoma City' And start_date > '2019-12-14');

Insert Into event
(id, version, type_id, name, organizer_id, start_date, end_date, format)
  Select nextval('hibernate_sequence'),
    0,
    (Select id from event_type where type = 'GP'),
    'Portland',
    (Select id from event_organizer where name = 'Channel Fireball'),
    '2019-12-21',
    '2019-12-22',
    'TBD'
  Where Not Exists (Select name from event where name = 'Portland' And start_date > '2019-12-21');



