package com.rhhcc.user.data;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.*;

import org.apache.commons.dbcp.BasicDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    BasicDataSource ds;
        
    @Override
    public long create(User user) throws SQLException {
        
        Connection con = ds.getConnection();   

        // Регистрация пользователя в системе
        String sql = "{ call usr_register(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
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
        prep.setDate(7, java.sql.Date.valueOf(user.getBirthday()));
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
        
        log.info("*p_result=" + prep.getInt(11));
        log.info("*p_result_secret=" + prep.getString(12));
        log.info("*p_result_text=" + prep.getString(13));
        
        con.close();
        
        return 1;
    }
    
    
}
