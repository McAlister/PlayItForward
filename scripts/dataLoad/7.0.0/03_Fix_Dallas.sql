
Update raw_standings
Set name = 'Garcia Rosas, Jose Daniel [MX]'
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And name like 'Garcia Rosas, Jos%';

Update raw_standings
Set name = 'Cervantes Martínez, Jesús [MX]'
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
     And name like 'Cervan%';

Update raw_standings
Set name = 'Orrmons, Kale [US]'
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
     And name like '%Orrm%';

Update raw_standings
Set name = 'Myers, Caleb [US]'
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And name like 'Myers, Caeb';

Update raw_standings r1
    Set name = coalesce((Select max(name) From raw_standings r2 where r2.event_id = r1.event_id and r2.round = 15 and substring(r2.name, 0, length(r2.name) - 4 ) = r1.name), name)
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And round = 14;

Update raw_standings r1
Set name = coalesce((Select max(name) From raw_standings r2 where r2.event_id = r1.event_id and r2.round = 15 and substring(r2.name, 0, length(r2.name) - 4 ) = r1.name), name)
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And round = 2;

Update raw_standings r1
Set name = coalesce((Select max(name) From raw_standings r2 where r2.event_id = r1.event_id and r2.round = 15 and substring(r2.name, 0, length(r2.name) - 4 ) = r1.name), name)
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And round = 3;

Update raw_standings r1
Set name = coalesce((Select max(name) From raw_standings r2 where r2.event_id = r1.event_id and r2.round = 15 and substring(r2.name, 0, length(r2.name) - 4 ) = r1.name), name)
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
                                             And round = 4;

Update raw_standings r1
Set name = coalesce((Select max(name) From raw_standings r2 where r2.event_id = r1.event_id and r2.round = 15 and substring(r2.name, 0, length(r2.name) - 4 ) = r1.name), name)
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And round = 5;

Update raw_standings r1
Set name = coalesce((Select max(name) From raw_standings r2 where r2.event_id = r1.event_id and r2.round = 15 and substring(r2.name, 0, length(r2.name) - 4 ) = r1.name), name)
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And round = 6;

Update raw_standings r1
Set name = coalesce((Select max(name) From raw_standings r2 where r2.event_id = r1.event_id and r2.round = 15 and substring(r2.name, 0, length(r2.name) - 4 ) = r1.name), name)
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And round = 7;

Update raw_standings r1
Set name = coalesce((Select max(name) From raw_standings r2 where r2.event_id = r1.event_id and r2.round = 15 and substring(r2.name, 0, length(r2.name) - 4 ) = r1.name), name)
Where event_id = (Select id From event where name = 'Dallas-Fort Worth' and event_code = 'gpdfw18')
    And round = 8;