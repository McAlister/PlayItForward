
Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
SELECT
  nextval('artist_seq'),
  0,
  'Malwina Kwiatkowska',
  'Hi! I''m Merkerinn, a freelance artist from Poland. I''ve been drawing things since I ' ||
    'learned how to hold the crayons and doing digital art since I saved money for my very ' ||
    'first tablet about 5 years ago.',
  'https://merkerinn.deviantart.com/',
  'https://www.facebook.com/merkerinn',
  'https://www.patreon.com/merkerinn',
  'https://merkerinn.artstation.com/'
Where not exists (select name from artist where name = 'Malwina Kwiatkowska');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'R.K. Post',
    'R.K. Post is a long time Magic artist and regular on the GP circuit. You can probably ' ||
      'find him on Artists Row if you want to get your mat signed.',
    null,
    'https://www.facebook.com/RkPostArt',
    'https://www.patreon.com/rkpost',
    'http://www.rkpost.net/'
  Where not exists (select name from artist where name = 'R.K. Post');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'Max Collins',
    'Max is an artist and animator at Cartoon Network and Studio Yotta.',
    null,
    null,
    null,
    'http://maxcollins.tumblr.com/'
  Where not exists (select name from artist where name = 'Max Collins');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'The Secret Of Kells',
    'An iconic image of Aisling from The Secret Of Kells used with permission of GKIDS.',
    null,
    null,
    null,
    'http://www.gkids.com/films/the-secret-of-kells/'
  Where not exists (select name from artist where name = 'The Secret Of Kells');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'Anna Lakisova',
    'Anna is a professional freelance digital artist that specializes in fantasy and gaming.',
    'https://anna-lakisova.deviantart.com/',
    'https://www.facebook.com/anna.lakisova.art',
    null,
    'https://www.anna-lakisova.com/'
  Where not exists (select name from artist where name = 'Anna Lakisova');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'Freyja Sig',
    'Freyja is a talented hobbyist from the United Kingdom.  Her art is evocative and fanciful.',
    'https://freyjasig.deviantart.com/gallery/',
    null,
    null,
    null
  Where not exists (select name from artist where name = 'Freyja Sig');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'Inna Vjuzhanina',
    'Inna is a professional graphic artist from Ukraine who really likes to draw Lara Croft.  ' ||
      'No, more than that.  A little more ...',
    'https://inna-vjuzhanina.deviantart.com/gallery/',
    'https://www.facebook.com/innavjuzhanina',
    null,
    'http://www.innavjuzhanina.com/p/home.html'
  Where not exists (select name from artist where name = 'Inna Vjuzhanina');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'Sandara',
    'Sandara is a professional digital artist who created my all time favorite playmat, ' ||
      '''Houston We Have a Problem''.  It was given out in GP Houston 2013.  She is also a ' ||
      'mentor of young artists winning Deviant Art''s Deviousness Award for 2017.',
    'https://sandara.deviantart.com/gallery/',
    'https://www.facebook.com/ArtofSandara3/',
    null,
    'https://society6.com/sandara'
  Where not exists (select name from artist where name = 'Sandara');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'Cyril Rolando',
    'Cyril is another talented hobbyist, this time from France.  He cultivates an ' ||
      'otherworldly style in his art.',
    'https://aquasixio.deviantart.com/gallery/',
    'https://www.facebook.com/Aquasixio/',
    null,
    'http://cyrilrolando.tumblr.com/'
  Where not exists (select name from artist where name = 'Cyril Rolando');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'Veronika Firsova',
    'Nieris is an illustrator from Siberia who specializes in game art.',
    'https://nieris.deviantart.com/gallery/',
    'https://www.facebook.com/Nieris1',
    null,
    'https://www.instagram.com/nieris_art/'
  Where not exists (select name from artist where name = 'Veronika Firsova');

Insert Into artist
(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
  SELECT
    nextval('artist_seq'),
    0,
    'Bob Kehl',
    'His name is Bob and he likes to paint amazing pirates almost as much as Inna ' ||
      'likes to draw Tomb Raider.  Almost.',
    'https://bobkehl.deviantart.com/gallery',
    'https://www.facebook.com/bobkehlart',
    null,
    'http://www.bobkehl.com/'
  Where not exists (select name from artist where name = 'Bob Kehl');
