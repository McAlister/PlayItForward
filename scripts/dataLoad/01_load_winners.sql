
Insert Into event_winner
(id, version, event_id,  name, ranking, record, blurb )
    Select nextval('hibernate_sequence'), 
        0,
        id,
        'Tatsuumi Kobayashi',
        20,
        '12-3-0',
        'Tatsuumi was our first and most mysterious winner.  A Japanese national she traveled all the way to Louisville to compete in the Legacy GP.  We are fairly certain she is not an english speaker as she made no effort to collect her prize and has not responded to emails.  If you know Tatsuumi and speak Japanese please let her know that we are holding her playmat for her.  With or without her playmat, her 36 points are a great way to kick off the year.  May we see many more records like it.'
From event where name = 'GP Louisville';


Insert Into event_winner
(id, version, event_id,  name, ranking, record, blurb )
    Select nextval('hibernate_sequence'), 
        0,
        id,
        'Courtney Rudigar',
        47,
        '11-3-1',
        'Of the 9 women who made day 2 in San Jose Courtney was solidly in the middle of the pack at the end of day 1.  While 5 women had 21 points going into day 2, Courtney had 19.  So she had an uphill battle ahead of her to claim the playmat.  But by the end of the first draft she had taken the lead and she held it till the bitter end.'
From event where name = 'GP San Jose';

Insert Into event_winner
(id, version, event_id,  name, ranking, record, blurb )
    Select nextval('hibernate_sequence'), 
        0,
        id,
        'Veronica Page',
        29,
        '12-3-0',
        'In GP Pittsburgh Veronica Page had a rough start with losses in rounds 3 and 5 after her byes.  At the beginning of round 6 day two was looking like a shakey proposition.  But she soldiered on going X-2 in day one and delivering a sublime string of 5 match wins in day two to finish the GP at 36 points.  Seven other women made day two and four left the day with pro points so it was a good showing all around.'
From event where name = 'GP Pittsburgh';

Insert Into event_winner
(id, version, event_id,  name, ranking, record, blurb )
    Select nextval('hibernate_sequence'),
        0,
        id,
        'Chantelle Campbell',
        24,
        '12-3-0',
        'Vancouver was a nail biter in more ways than one.  The field of women was small in Canada and at the end of day one the bulk of the female competitors were sitting at 15 points, just shy of making day two.  Only 5 women were representing the Lady Planeswalkers that Sunday which is rather daunting odds. Varience being what it was I did not expect to see another Tatsuumi or Veronica this day.  But I was wrong.  Not only did a woman go 12-3 in Vancouver.  Two of them did.  Chantelle Campbell and Jennifer Crotts were both in it to win it.  They gave no ground and asked no quarter.  For the first time we had to wait for final standings to crown our winner because it came down to tiebreakers.  At 24th place Chantelle narrowly beat Jennifer''s 28th place finish. '
From event where name = 'GP Vancouver';

