-- 
-- Пользователя от имени которого будет выполняться работа с БД
-- 

set @sql_create_db_user = concat('create user if not exists ', @DB_USER_NAME, '@', @DBHOST, ' identified by ''', @DB_USER_PWD, '''');
prepare stmt_create_db_user from @sql_create_db_user;
execute stmt_create_db_user;
deallocate prepare stmt_create_db_user;