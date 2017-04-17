
Insert Into guild_leader
(id, version, name, image_name, type_id, user_id, guild_id)
Select
  nextval('guild_leader_seq'),
  0,
  'Simone Aiken',
  null,
  1,
  id,
  1
From app_user
Where username = 'saiken@ulfheim.net'
  And Not Exists (Select name from guild_leader where name = 'Simone Aiken');



