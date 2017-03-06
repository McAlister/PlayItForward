
Insert Into event_organizer
(id, version, name, url)
  Select nextval('hibernate_sequence'),
    0,
    'Channel Fireball',
    'http://store.channelfireball.com/store'
  Where Not Exists (Select name from event_organizer where name = 'Channel Fireball');

Insert Into event_organizer
(id, version, name, url)
  Select nextval('hibernate_sequence'),
    0,
    'Star City Games',
    'http://www.starcitygames.com/'
  Where Not Exists (Select name from event_organizer where name = 'Star City Games');

Insert Into event_organizer
(id, version, name, url)
  Select nextval('hibernate_sequence'),
    0,
    'Pastimes',
    'http://www.pastimes.net/'
  Where Not Exists (Select name from event_organizer where name = 'Pastimes');

Insert Into event_organizer
(id, version, name, url)
  Select nextval('hibernate_sequence'),
    0,
    'Game Keeper',
    'http://www.gamekeeper.ca/'
  Where Not Exists (Select name from event_organizer where name = 'Game Keeper');

Insert Into event_organizer
(id, version, name, url)
  Select nextval('hibernate_sequence'),
    0,
    'Cascade Games',
    'http://www.cascadegames.com/'
  Where Not Exists (Select name from event_organizer where name = 'Cascade Games');

Insert Into event_organizer
(id, version, name, url)
  Select nextval('hibernate_sequence'),
    0,
    'Face to Face Games',
    'http://www.facetofacegames.com/'
  Where Not Exists (Select name from event_organizer where name = 'Face to Face Games');


