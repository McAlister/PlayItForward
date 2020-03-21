
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

Update event
Set name = 'New Jersey'
Where event_code like 'gpnj17%';

Update event
Set name = 'Washington D.C.'
Where event_code = 'gpdc';

Update event
Set start_date = '2017-05-07'
Where event_code = 'gpric17';

Update event
Set name = 'Seattle'
Where event_code like 'gpsea18%';

Update event
Set start_date = '2018-04-13'
Where event_code = 'gphar18';

Update event
Set start_date = '2018-05-04',
       name = 'Dallas-Fort Worth'
Where event_code = 'gpdfw18';

Update event
Set name = 'Las Vegas (Modern)'
Where event_code = 'gplv18-modern';

Update event
Set name = 'Las Vegas (Limited)'
Where event_code = 'gplv18-limited';

Update event
Set name = 'São Paulo'
Where name = 'Sao Paulo';

Update event
Set name = 'Richmond'
Where event_code like 'gpric18%';

Update event
set start_date = '2018-07-06'
Where event_code = 'gpsao18';

Update event
set start_date = '2018-11-03'
Where event_code = 'gpmil18';

update event
set event_url = 'https://magic.wizards.com/en/events/coverage/gpvan18'
Where event_code = 'gpvan18';

update event
set start_date = '2019-02-23'
Where name = 'Cleveland' And start_date = '2019-02-22';

update event
set name = 'São Paulo (April)'
Where event_code = 'gpsao19';

update event
set name = 'Las Vegas (Modern)',
       start_date = '2019-08-24'
Where name = 'Las Vegas' And start_date = '2019-08-23';

update event
set name = 'Las Vegas (Limited)'
Where name = 'Las Vegas' And playmat_file_name = '2019-Vegas-Limited.jpg';

Update event
set name = 'Montreal'
Where name = 'Montréal';

Update event
set start_date = '2019-10-26'
Where name = 'Phoenix' And start_date = '2019-10-25';

Update event
Set start_date = '2019-12-21'
Where name = 'Portland' And start_date = '2019-12-20';

Update event
Set start_date = '2019-12-14'
Where name = 'Oklahoma City' And start_date = '2019-12-13';

update event
Set name = 'São Paulo (November)'
Where name = 'São Paulo' And start_date = '2019-11-16';

update event
set start_date = '2019-11-23'
Where name = 'Columbus' and start_date = '2019-11-15';
