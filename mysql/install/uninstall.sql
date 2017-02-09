-- 
-- Выполняет удаление БД, Пользователей
-- 

set @sql_drop_schema = concat('drop schema if exists ', @SCHEMA_NAME);
prepare stmt_drop_schema from @sql_drop_schema;
execute stmt_drop_schema;
deallocate prepare stmt_drop_schema;

set @sql_drop_db_user = concat('drop user if exists ', @DB_USER_NAME, '@', @DBHOST);
prepare stmt_drop_db_user from @sql_drop_db_user;
execute stmt_drop_db_user;
deallocate prepare stmt_drop_db_user;

set @sql_drop_app_user = concat('drop user if exists ', @APP_USER_NAME, '@', @DBHOST);
prepare stmt_drop_app_user from @sql_drop_app_user;
execute stmt_drop_app_user;
deallocate prepare stmt_drop_app_user;