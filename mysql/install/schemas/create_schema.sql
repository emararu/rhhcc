-- 
-- Выполняет создание схемы БД
-- 

set @sql_create_schema = concat('create schema ', @SCHEMA_NAME, ' character set utf8mb4 collate utf8mb4_unicode_ci');
prepare stmt_create_schema from @sql_create_schema;
execute stmt_create_schema;
deallocate prepare stmt_create_schema;
