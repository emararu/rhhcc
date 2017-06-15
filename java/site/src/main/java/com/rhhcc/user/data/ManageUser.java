package com.rhhcc.user.data;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhhcc.common.type.DBComplete;
import com.rhhcc.common.type.DBResult;
import com.rhhcc.user.type.DBResultCreate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс реализующий управление пользователем в системе
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Service("manageUser")
public class ManageUser implements Serializable, Manage {
    
    private static final long serialVersionUID = 2298579636238450702L;

    private final Logger log = LoggerFactory.getLogger(ManageUser.class);
    
    @Autowired
    @Qualifier("dataSource")
    private BasicDataSource ds;
        
    @Autowired
    @Qualifier("manageUserNotify")
    private ManageUserNotify notify;
        
    @Autowired
    private HttpServletRequest context;
    
    
    /**
     * Отправляет в БД запрос на создание пользователя
     * @param user Данные пользователя
     * @return Результат ответа БД на отправленный запрос
     */
    private DBResult createUser(User user) {
        
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
            prep.setInt(10, 1);

            // OUT
            // * Результат работы: >0 - ID созданной записи; <0 - Ошибка
            prep.registerOutParameter(11, java.sql.Types.INTEGER);  
            // * Секретный ключ
            prep.registerOutParameter(12, java.sql.Types.VARCHAR);  
            // * Текстовое описание результата работы        
            prep.registerOutParameter(13, java.sql.Types.VARCHAR);  

            prep.execute();

            result = new DBResultCreate(DBComplete.register, prep.getInt(11), prep.getString(12), prep.getString(13));
            
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
    
    
    @Override
    public DBResult create(User user) {
        
        log.info(user.toString());
        
        // Создание пользователя в БД            
        DBResultCreate result = (DBResultCreate)createUser(user);
        // Если пользователь успешно создан
        if (result.getId() >= 0) {
            // Отправка уведомления на почту и установка в БД флага подтверждения
            notify.sendMail(result.getId(), user.getFirstname(), user.getEmail(), result.getSecret(), result.secretConfirmURL(context, "/user/register/"));
        }
        
        log.info(result.toString());
        
        return result;
    }
        
}
