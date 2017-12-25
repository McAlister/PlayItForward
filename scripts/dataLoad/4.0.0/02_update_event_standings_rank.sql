

UPDATE event_standing e
SET rank = ( SELECT rank
             FROM raw_standings r, player p
             WHERE r.name = p.name
              AND r.event_id = e.event_id
              AND r.round = e.round
              AND p.id = e.player_id
);
