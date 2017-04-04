

delete from user_role where user_id = (select id from app_user where username = 'nobody');

delete from role where authority = 'ROLE_ANYONE';

delete from app_user where username = 'nobody';

