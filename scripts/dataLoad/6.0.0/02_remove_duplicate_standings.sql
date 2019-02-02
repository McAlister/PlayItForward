
Delete From raw_standings
Where id in ( Select min(id)
    From raw_standings
    Group by event_id, name, round
    Having count( name ) > 1
    Order By name );
