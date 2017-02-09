-- 
-- Пользователь клиентского приложения которое будет работать с БД
-- 

set @sql_create_app_user = concat('create user if not exists ', @APP_USER_NAME, '@', @DBHOST, ' identified by ''', @APP_USER_PWD, '''');
prepare stmt_create_app_user from @sql_create_app_user;
execute stmt_create_app_user;
deallocate prepare stmt_create_app_user;
