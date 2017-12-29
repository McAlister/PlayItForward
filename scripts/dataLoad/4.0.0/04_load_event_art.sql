
Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Untitled',
    (Select id from artist where name = 'Max Collins'),
    (Select id from event where start_date = '2017-01-07')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-01-07' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Untitled',
    (Select id from artist where name = 'Max Collins'),
    (Select id from event where start_date = '2017-01-28')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-01-28' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Untitled',
    (Select id from artist where name = 'Max Collins'),
    (Select id from event where start_date = '2017-07-22')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-07-22' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Halloween Witch',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-02-11')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-02-11' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Halloween Witch',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-02-18')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-02-18' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Halloween Witch',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-10-28')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-10-28' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Snowblind',
    (Select id from artist where name = 'R.K. Post'),
    (Select id from event where start_date = '2017-03-11')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-03-11' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Snowblind',
    (Select id from artist where name = 'R.K. Post'),
    (Select id from event where start_date = '2017-03-25')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-03-25' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Snowblind',
    (Select id from artist where name = 'R.K. Post'),
    (Select id from event where start_date = '2017-09-02')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-09-02' ));


Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Dreaming Through Wonderland',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-05-06')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-05-06' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Dreaming Through Wonderland',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-05-20')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-05-20' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Alice: Madness Returns',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-06-03')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-06-03' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Alice: Madness Returns',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-06-15')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-06-15' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Alice: Madness Returns',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-06-16')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-06-16' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Alice: Madness Returns',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-06-17')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-06-17' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Angel Of Revival',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-08-05')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-08-05' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Angel Of Revival',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-08-19')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-08-19' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Angel Of Revival',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-08-26')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-08-26' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Pangur Ban Song',
    (Select id from artist where name = 'The Secret Of Kells'),
    (Select id from event where start_date = '2017-11-11')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-11-11' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Pangur Ban Song',
    (Select id from artist where name = 'The Secret Of Kells'),
    (Select id from event where start_date = '2017-11-18')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-11-18' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Reindee-cats Party',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-12-09')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-12-09' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Reindee-cats Party',
    (Select id from artist where name = 'Malwina Kwiatkowska'),
    (Select id from event where start_date = '2017-12-16')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2017-12-16' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Meditation',
    (Select id from artist where name = 'Anna Lakisova'),
    (Select id from event where start_date = '2018-01-27')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-01-27' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Meditation',
    (Select id from artist where name = 'Anna Lakisova'),
    (Select id from event where start_date = '2018-02-10')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-02-10' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Meditation',
    (Select id from artist where name = 'Anna Lakisova'),
    (Select id from event where start_date = '2018-02-24')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-02-24' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'A Young Samurai Warrior',
    (Select id from artist where name = 'Freyja Sig'),
    (Select id from event where start_date = '2018-03-17')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-03-17' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'A Young Samurai Warrior',
    (Select id from artist where name = 'Freyja Sig'),
    (Select id from event where start_date = '2018-12-08')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-12-08' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Bow-girl',
    (Select id from artist where name = 'Anna Lakisova'),
    (Select id from event where start_date = '2018-05-05')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-05-05' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Bow-girl',
    (Select id from artist where name = 'Anna Lakisova'),
    (Select id from event where start_date = '2018-04-14')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-04-14' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Bow-girl',
    (Select id from artist where name = 'Anna Lakisova'),
    (Select id from event where start_date = '2018-04-07' and name = 'GP Seattle Standard')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-04-07' and name = 'GP Seattle Standard'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Bow-girl',
    (Select id from artist where name = 'Anna Lakisova'),
    (Select id from event where start_date = '2018-04-07' and name = 'GP Seattle Legacy')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-04-07' and name = 'GP Seattle Legacy'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Landfall',
    (Select id from artist where name = 'Bob Kehl'),
    (Select id from event where start_date = '2018-06-23')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-06-23' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Landfall',
    (Select id from artist where name = 'Bob Kehl'),
    (Select id from event where start_date = '2018-06-16' and name = 'GP Las Vegas Modern')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-06-16' and name = 'GP Las Vegas Modern'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Landfall',
    (Select id from artist where name = 'Bob Kehl'),
    (Select id from event where start_date = '2018-06-16' and name = 'GP Las Vegas Limited')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-06-16' and name = 'GP Las Vegas Limited'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Spell Thief Lux',
    (Select id from artist where name = 'Veronika Firsova'),
    (Select id from event where start_date = '2018-07-07')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-07-07' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Spell Thief Lux',
    (Select id from artist where name = 'Veronika Firsova'),
    (Select id from event where start_date = '2018-07-21')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-07-21' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Spell Thief Lux',
    (Select id from artist where name = 'Veronika Firsova'),
    (Select id from event where start_date = '2018-07-28')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-07-28' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Spell Thief Lux',
    (Select id from artist where name = 'Veronika Firsova'),
    (Select id from event where start_date = '2018-08-18' and name = 'GP Providence')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-08-18' and name = 'GP Providence'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Dragon Army',
    (Select id from artist where name = 'Inna Vjuzhanina'),
    (Select id from event where start_date = '2018-08-11')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-08-11' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Dragon Army',
    (Select id from artist where name = 'Inna Vjuzhanina'),
    (Select id from event where start_date = '2018-08-18' and name = 'GP Los Angeles')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-08-18' and name = 'GP Los Angeles'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Dragon Army',
    (Select id from artist where name = 'Inna Vjuzhanina'),
    (Select id from event where start_date = '2018-09-01' and name = 'GP Richmond Legacy')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-09-01' and name = 'GP Richmond Legacy'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Dragon Army',
    (Select id from artist where name = 'Inna Vjuzhanina'),
    (Select id from event where start_date = '2018-09-01' and name = 'GP Richmond Standard')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-09-01' and name = 'GP Richmond Standard'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Zombie Archer',
    (Select id from artist where name = 'Sandara'),
    (Select id from event where start_date = '2018-10-06' and name = 'GP Mexico City')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-10-06' and name = 'GP Mexico City'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Zombie Archer',
    (Select id from artist where name = 'Sandara'),
    (Select id from event where start_date = '2018-10-06' and name = 'GP Montreal')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event
                                    Where start_date = '2018-10-06' and name = 'GP Montreal'));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'Zombie Archer',
    (Select id from artist where name = 'Sandara'),
    (Select id from event where start_date = '2018-10-27')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-10-27' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'SAGITTARIUS from the Dancing Zodiac',
    (Select id from artist where name = 'Cyril Rolando'),
    (Select id from event where start_date = '2018-11-03')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-11-03' ));

Insert Into event_art
(id, version, title, artist_id, event_id)
Select
    nextval('event_art_seq'),
    0,
    'SAGITTARIUS from the Dancing Zodiac',
    (Select id from artist where name = 'Cyril Rolando'),
    (Select id from event where start_date = '2018-11-17')
Where Not Exists (Select id
                  From event_art
                  Where event_id = (Select id From event Where start_date = '2018-11-17' ));

