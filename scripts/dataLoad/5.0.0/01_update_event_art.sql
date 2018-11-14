--Insert Into artist
--(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
--  SELECT
--    nextval('artist_seq'),
--    0,
--    'Janice Duke',
--    'Janice Duke is an illustrator based in Scotland. She specialises in digital illustration
--     and design – with a focus on book illustration and book covers – for indie authors and
--     publishers, as well as article illustration.',
--    'https://www.deviantart.com/janiceduke',
--    'https://www.facebook.com/janice.duke.art',
--    null,
--    'https://janiceduke.com/'
--  Where not exists (select name from artist where name = 'Janice Duke');
--
--Insert Into artist
--(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
--  SELECT
--    nextval('artist_seq'),
--    0,
--    'Nalak-Bel',
--    'Nalak-Bel is an extremely talented and very mysterious french illustration student.',
--    'https://www.deviantart.com/nalak-bel',
--    null,
--    null,
--    null
--  Where not exists (select name from artist where name = 'Nalak-Bel');
--
--Insert Into artist
--(id, version, name, blurb, deviant_art_url, facebook_url, patreon_url, website_url)
--  SELECT
--    nextval('artist_seq'),
--    0,
--    'Alan Campos',
--    'Alan Campos is an extremely talented and very mysterious french illustration student.',
--    'https://www.deviantart.com/alanscampos',
--    'https://www.facebook.com/artistacampos',
--    'https://www.patreon.com/alancampos',
--    null
--  Where not exists (select name from artist where name = 'Alan Campos');


Update event_art
Set purchase_url = 'https://www.rkpost.net/product-page/pul117-snowblind'
Where title = 'Snowblind';

Update event_art
Set purchase_url = 'https://www.deviantart.com/aquasixio/art/SAGITTARIUS-from-the-Dancing-Zodiac-542159262'
Where title = 'SAGITTARIUS from the Dancing Zodiac';

