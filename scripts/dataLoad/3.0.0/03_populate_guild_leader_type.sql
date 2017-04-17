
Insert Into guild_leader_type
(id, version, type, description )
Select
  nextval('guild_leader_type_seq'),
  0,
  'ORGANIZER',
  'Organizer'
Where Not Exists (Select type from guild_leader_type where type = 'ORGANIZER');

Insert Into guild_leader_type
(id, version, type, description )
Select
  nextval('guild_leader_type_seq'),
  0,
  'MENTOR',
  'Mentor'
Where Not Exists (Select type from guild_leader_type where type = 'MENTOR');

Insert Into guild_leader_type
(id, version, type, description )
Select
  nextval('guild_leader_type_seq'),
  0,
  'RULES_ADVISOR',
  'Rules Advisor'
Where Not Exists (Select type from guild_leader_type where type = 'RULES_ADVISOR');


