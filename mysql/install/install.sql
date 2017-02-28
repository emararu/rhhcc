--  
--  СКРИПТ УСТАНОВКИ
--
--  * @GP_AUTHOR - Разработчик
--  * @GP_NUMBER - Порядковый номер изменения, в формате - число от 0 до XXXXX.YY, где:
--                 XXXXX - номер задачи с лидирующими нулями
--                 YY    - порядковый номер изменения в данной задаче
--  * @GP_DESC   - Сообщение описывающее изменение в БД
--
--  ---------------------------------------------------------------------------------------------  --
--  Все пути указываются относительно текущего расположения install.sql - GIT:rhhcc/mysql/install
--  ---------------------------------------------------------------------------------------------  --
--
--  УСТАНОВКА
--
--    Установка выполняется в командной строке:
--    1. Изменить значения по умолчанию переменных в конфигурационном файле GIT:rhhcc/mysql/settings/define.sql
--    2. Перейти в каталог с установочными скриптами, в котором находится файл GIT:/rhhcc/mysql/install/install.sql
-- 
--       cd ??/rhhcc/mysql/install
--       Вместо знаков ?? необходимо указать место располежение репозитория GIT
--
--    3. После установки переменных, для запуска установки - выполнить в командной строке:
--
--       mysql -u root -p < ../settings/_gen_define.sql -s -r > ../settings/login.cnf
--       mysql -u root -p < ../settings/_gen_usedb.sql -s -r > schemas/use_schema.sql
--       mysql -u root -p < install.sql -v > ../logs/install.log
--       В процессе установки будет сформирован лог в директории ../logs
--

-- ГЛОБАЛЬНЫЕ ПЕРЕМЕННЫЕ
set @GP_AUTHOR = 'EMararu';
set @GP_NUMBER = '00000.00';
set @GP_DESC   = 'Installation';

--  ---------------------------------------------------------------------------------------------  --

source ../settings/define.sql
source ../settings/global.sql

--  ---------------------------------------------------------------------------------------------  --

source uninstall.sql
source schemas/create_schema.sql
source users/create_user.sql
source grants/create_grant.sql
source schemas/use_schema.sql

--  ---------------------------------------------------------------------------------------------  --

-- Регистрация установки в sys_dc

--  ---------------------------------------------------------------------------------------------  --

-- source ../settings/undef.sql

--  ---------------------------------------------------------------------------------------------  --

