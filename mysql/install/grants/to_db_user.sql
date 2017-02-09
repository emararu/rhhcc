-- 
-- Привилегии пользователю от имени которого выполняется работа с БД
-- 

set @sql_grant_db_user = concat('grant alter, alter routine, create, create routine, create view, delete, drop, event, execute, index, insert, lock tables, references, select, trigger, update on ', @SCHEMA_NAME, '.', '* to ', @DB_USER_NAME, '@', @DBHOST);
prepare stmt_grant_db_user from @sql_grant_db_user;
execute stmt_grant_db_user;
deallocate prepare stmt_grant_db_user;