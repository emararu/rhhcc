-- 
-- Привилегии пользователю от имени которого работает клиентское приложение
-- 

set @sql_grant_app_user = concat('grant execute, select on ', @SCHEMA_NAME, '.', '* to ', @APP_USER_NAME, '@', @DBHOST);
prepare stmt_grant_app_user from @sql_grant_app_user;
execute stmt_grant_app_user;
deallocate prepare stmt_grant_app_user;
