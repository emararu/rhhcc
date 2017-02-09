-- 
--  На основании определенных переменных в define.sql выполняется создание файла для автоматического соединения с БД
-- 
--  Для запуска создания файла автоматического соединения с БД:
--    1. Изменить значения по умолчанию переменных в конфигурационном файле GIT:rhhcc/mysql/settings/define.sql
--    2. Перейти в каталог с установочными скриптами, в котором находится файл GIT:/rhhcc/mysql/install/install.sql
--    3. После установки переменных, выполнить в командной строке:
-- 
--       mysql -u root -p < ../settings/_gen_define.sql -s -r > ../settings/login.cnf
--       mysql -u root -p < ../settings/_gen_usedb.sql -s -r > schemas/use_schema.sql
-- 
--       В результате работы скрипта, будет создан конфигурационных файл.
--       Установив переменную окружения MYSQL_TEST_LOGIN_FILE=GIT:/rhhcc/mysql/settings/login.cnf коннект к БД 
--       можно будет запускать mysql с прямым указанием места размещения кофигурационного файла:
-- 
--          mysql --defaults-extra-file=GIT:/rhhcc/mysql/settings/login.cnf
-- 
--  ------------------------------------------------------------------------------------------------------------------  --
--       Вместо GIT: необходимо указать место расположения репозитория GIT
--  ------------------------------------------------------------------------------------------------------------------  --
-- 

source ../settings/define.sql
select '[client]' as '';
select concat('host=', @DBHOST) as '';
select concat('user=', @DB_USER_NAME) as '';
select concat('password=', @DB_USER_PWD) as '';
select concat('database=', @SCHEMA_NAME) as '';
