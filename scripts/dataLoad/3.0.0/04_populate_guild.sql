
Insert Into guild
(id, version, name, description, url, twitter, address, email, phone)
Select
  nextval('guild_seq'),
  0,
  'The Brute Squad',
  'A guild for women magic players in the front range',
  null,
  null,
  '123 Fake St.',
  'test@fake.com',
  '555-555-5555'
Where Not Exists (Select name from guild where name = 'The Brute Squad');



