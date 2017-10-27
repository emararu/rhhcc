package com.rhhcc.user.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhhcc.common.type.DBComplete;
import com.rhhcc.common.type.DBResult;
import com.rhhcc.user.auth.SpringAuth;
import com.rhhcc.user.type.DBResultCreate;
import com.rhhcc.user.type.DBResultUpdate;
import com.rhhcc.user.type.DBResultMerge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс реализующий возможность изменения данных пользователя
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Service("manageUserModify")
class ManageUserModify {
    
    private final Logger log = LoggerFactory.getLogger(ManageUserModify.class);
    
    @Autowired
    @Qualifier("dataSource")
    private BasicDataSource ds;
    
    @Autowired
    @Qualifier("springAuth")
    private SpringAuth springAuth;
        
    /**
     * Отправляет в БД запрос на создание пользователя
     * @param user Данные пользователя
     * @return Результат ответа БД на отправленный запрос
     */
    public DBResult create(User user) {
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
            
            // Начало транзакции
            con.setAutoCommit(false);
            
            // Регистрация пользователя в системе
            String sql = "{ call usr_register(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * Логин пользователя
            prep.setString(1, user.getLogin());
            // * Пароль пользователя
            prep.setString(2, user.getPassword());
            // * Имя пользователя
            prep.setString(3, user.getFirstname());
            // * Фамилия пользователя
            prep.setString(4, user.getLastname());
            // * Аватар пользователя
            prep.setString(5, user.getIcon());
            // * Пол
            prep.setString(6, user.getGender().toString());
            // * Дата рождения
            prep.setDate(7, (user.getBirthday() != null ? java.sql.Date.valueOf(user.getBirthday()) : null));
            // * EMail пользователя
            prep.setString(8, user.getEmail());
            // * Телефон пользователя
            prep.setString(9, user.getPhone());
            // * Пользователь выполняющий действие
            prep.setLong(10, 1);

            // OUT
            // * Результат работы: >0 - ID созданной записи; <0 - Ошибка
            prep.registerOutParameter(11, java.sql.Types.INTEGER);  
            // * Секретный ключ
            prep.registerOutParameter(12, java.sql.Types.VARCHAR);  
            // * Текстовое описание результата работы        
            prep.registerOutParameter(13, java.sql.Types.VARCHAR);  

            prep.execute();

            result = new DBResultCreate(DBComplete.register, prep.getLong(11), prep.getString(12), prep.getString(13));
            
            if (result.getId() >= 0) con.commit(); else con.rollback();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(DBComplete.register, -600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(DBComplete.register, -600, e.toString());
        }
        
        return result;
    }
        
    /**
     * Отправляет в БД запрос на обновление логина и пароля пользователя
     * @param user Данные пользователя
     * @return Результат обновления данных пользователя
     */
    public DBResult updateAuth(User user) {
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
            
            // Начало транзакции
            con.setAutoCommit(false);
            
            // Обновление логина и пароля пользователя(для пользователей зарегистрированных локально в системе)
            String sql = "{ call usr_update_auth(?, ?, ?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * ID пользователя
            prep.setLong(1, user.getId());
            // * Логин пользователя
            prep.setString(2, user.getLogin());
            // * Пароль пользователя
            prep.setString(3, user.getPassword());
            // * Пользователь выполняющий действие
            prep.setLong(4, springAuth.getCurrent().getId());

            // OUT
            // * Результат работы: 1 - Успешно; <0 - Ошибка
            prep.registerOutParameter(5, java.sql.Types.INTEGER);
            // * Текстовое описание результата работы
            prep.registerOutParameter(6, java.sql.Types.VARCHAR);

            prep.execute();

            result = new DBResult(prep.getLong(5), prep.getString(6));
            
            if (result.getId() >= 0) con.commit(); else con.rollback();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(-600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(-600, e.toString());
        }
        
        return result;
    }
        
    /**
     * Отправляет в БД запрос на обновление данных пользователя
     * @param user Данные пользователя
     * @return Результат обновления данных пользователя
     */
    public DBResult update(User user) {
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
            
            // Начало транзакции
            con.setAutoCommit(false);
            
            // Обновление данных пользователя(для пользователей зарегистрированных локально в системе)
            String sql = "{ call usr_update_user(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * ID пользователя
            prep.setLong(1, user.getId());
            // * Имя пользователя
            prep.setString(2, user.getFirstname());
            // * Фамилия пользователя
            prep.setString(3, user.getLastname());
            // * Аватар пользователя
            prep.setString(4, user.getIcon());
            // * Пол
            prep.setString(5, user.getGender().toString());
            // * Дата рождения
            prep.setDate(6, (user.getBirthday() != null ? java.sql.Date.valueOf(user.getBirthday()) : null));
            // * EMail пользователя
            prep.setString(7, user.getEmail());
            // * Телефон пользователя
            prep.setString(8, user.getPhone());
            // * Пользователь выполняющий действие
            prep.setLong(9, springAuth.getCurrent().getId());

            // OUT
            // * Результат работы: >0 - ID обновленной записи; <0 - Ошибка
            prep.registerOutParameter(10, java.sql.Types.INTEGER);
            // * Секретный ключ для подтверждения email
            prep.registerOutParameter(11, java.sql.Types.VARCHAR);  
            // * Секретный ключ для подтверждения телефонного номера
            prep.registerOutParameter(12, java.sql.Types.VARCHAR);  
            // * Текстовое описание результата работы
            prep.registerOutParameter(13, java.sql.Types.VARCHAR);

            prep.execute();

            result = new DBResultUpdate(prep.getLong(10), prep.getString(11), prep.getString(12), prep.getString(13));
            
            if (result.getId() >= 0) con.commit(); else con.rollback();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(-600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(-600, e.toString());
        }
        
        return result;
    }
        
    /**
     * Отправляет в БД запрос на слияние и обновление данных пользователя
     * @param user Данные пользователя
     * @return Результат слияния и обновления данных пользователя
     */
    public DBResult merge(User user) {
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
            
            // Начало транзакции
            con.setAutoCommit(false);
            
            // Регистрация пользователя в системе
            String sql = "{ call usr_update_out(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * Идентификатор источника авторизацию
            prep.setInt(1, user.getProvider());
            // * Идентификатор пользователя в системе источника предоставляющего авторизацию
            prep.setString(2, user.getOauth());
            // * Имя пользователя
            prep.setString(3, user.getFirstname());
            // * Фамилия пользователя
            prep.setString(4, user.getLastname());
            // * Аватар пользователя
            prep.setString(5, user.getIcon());
            // * Пол
            prep.setString(6, user.getGender().toString());
            // * Дата рождения
            prep.setDate(7, (user.getBirthday() != null ? java.sql.Date.valueOf(user.getBirthday()) : null));
            // * EMail пользователя
            prep.setString(8, user.getEmail());
            // * Телефон пользователя
            prep.setString(9, user.getPhone());
            // * Пользователь выполняющий действие
            prep.setLong(10, 1);

            // OUT
            // * Результат работы: >0 - ID обновленной записи; <0 - Ошибка
            prep.registerOutParameter(11, java.sql.Types.INTEGER);
            // * ID группировки пользователя
            prep.registerOutParameter(12, java.sql.Types.INTEGER);
            // * Текстовое описание результата работы
            prep.registerOutParameter(13, java.sql.Types.VARCHAR);

            prep.execute();

            result = new DBResultMerge(prep.getLong(11), prep.getLong(12), prep.getString(13));
            
            if (result.getId() >= 0) con.commit(); else con.rollback();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(-600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(-600, e.toString());
        }
        
        return result;
    }
    
    /**
     * Отправляет в БД запрос на подтверждение регистрации пользователя
     * @param user_id ID пользователя
     * @param secret  Секретный ключ
     * @return Результат ответа БД на отправленный запрос
     */
    public DBResult confirmAuth(long user_id, String secret) {
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
            
            // Начало транзакции
            con.setAutoCommit(false);
            
            // Регистрация пользователя в системе
            String sql = "{ call usr_confirm_accept_auth(?, ?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * ID пользователя
            prep.setLong(1, user_id);
            // * Секретный ключ
            prep.setString(2, secret);
            // * Пользователь выполняющий действие
            prep.setLong(3, user_id);

            // OUT
            // * Результат работы: >0 - ID созданной записи; <0 - Ошибка
            prep.registerOutParameter(4, java.sql.Types.INTEGER);    
            // * Текстовое описание результата работы        
            prep.registerOutParameter(5, java.sql.Types.VARCHAR);  

            prep.execute();

            result = new DBResult(DBComplete.register_confirm, prep.getLong(4), prep.getString(5));
            
            if (result.getId() >= 0) con.commit(); else con.rollback();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(DBComplete.register_confirm, -600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(DBComplete.register_confirm, -600, e.toString());
        }
        
        return result;
    }
    
    /**
     * Отправляет в БД запрос на подтверждение email пользователя
     * @param user_id ID пользователя
     * @param secret  Секретный ключ
     * @return Результат ответа БД на отправленный запрос
     */
    public DBResult confirmEmail(long user_id, String secret) {
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
            
            // Начало транзакции
            con.setAutoCommit(false);
            
            // Регистрация пользователя в системе
            String sql = "{ call usr_confirm_accept_email(?, ?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * ID пользователя
            prep.setLong(1, user_id);
            // * Секретный ключ
            prep.setString(2, secret);
            // * Пользователь выполняющий действие
            prep.setLong(3, user_id);

            // OUT
            // * Результат работы: >0 - ID созданной записи; <0 - Ошибка
            prep.registerOutParameter(4, java.sql.Types.INTEGER);    
            // * Текстовое описание результата работы        
            prep.registerOutParameter(5, java.sql.Types.VARCHAR);  

            prep.execute();

            result = new DBResult(DBComplete.email_confirm, prep.getLong(4), prep.getString(5));
            
            if (result.getId() >= 0) con.commit(); else con.rollback();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(DBComplete.email_confirm, -600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(DBComplete.email_confirm, -600, e.toString());
        }
        
        return result;
    }
    
    /**
     * Отправляет в БД запрос на подтверждение телефонного номера пользователя
     * @param user_id ID пользователя
     * @param verify  Код верификации телефонного номера
     * @return Результат ответа БД на отправленный запрос
     */
    public DBResult confirmPhone(long user_id, String verify) {
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
            
            // Начало транзакции
            con.setAutoCommit(false);
            
            // Регистрация пользователя в системе
            String sql = "{ call usr_confirm_accept_phone(?, ?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * ID пользователя
            prep.setLong(1, user_id);
            // * Код верификации телефонного номера
            prep.setString(2, verify);
            // * Пользователь выполняющий действие
            prep.setLong(3, user_id);

            // OUT
            // * Результат работы: >0 - ID созданной записи; <0 - Ошибка
            prep.registerOutParameter(4, java.sql.Types.INTEGER);    
            // * Текстовое описание результата работы        
            prep.registerOutParameter(5, java.sql.Types.VARCHAR);  

            prep.execute();

            result = new DBResult(prep.getLong(4), prep.getString(5));
            
            if (result.getId() >= 0) con.commit(); else con.rollback();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(-600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(-600, e.toString());
        }
        
        return result;
    }
    
}
