-- 
-- Выполняет создание схемы БД
-- 

set @sql_create_schema = concat('create schema ', @SCHEMA_NAME, ' character set utf8 collate utf8_general_ci');
prepare stmt_create_schema from @sql_create_schema;
execute stmt_create_schema;
deallocate prepare stmt_create_schema;
