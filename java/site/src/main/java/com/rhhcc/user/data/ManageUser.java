package com.rhhcc.user.data;

import java.io.Serializable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

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
        // String sql = "{ call usr_register(:p_login, :p_password, :p_firstname, :p_lastname, :p_logo_url, :p_gender, :p_birthday, :p_email, :p_phone, :p_actor_id, :p_result, :p_result_secret, :p_result_text) }";
        String sql = "{ call usr_register(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        CallableStatement prep = con.prepareCall(sql);
        
        // IN
        prep.setString(1, user.getLogin());               // Логин пользователя
        prep.setString(2, user.getPassword());            // Пароль пользователя
        prep.setString(3, user.getFirstname());           // Имя пользователя
        prep.setString(4, user.getLastname());            // Фамилия пользователя
        prep.setString(5, user.getIcon());                // Аватар пользователя
        prep.setString(6, user.getGender().toString());   // Пол
        prep.setDate(7, new java.sql.Date(user.getBirthday().getMillis()));  // Дата рождения
        prep.setString(8, user.getEmail());               // EMail пользователя
        prep.setString(9, user.getPhone());               // Телефон пользователя
        prep.setInt(10, 1);                               // Пользователь выполняющий действие
        // OUT
        prep.registerOutParameter(11, java.sql.Types.INTEGER);  // Результат работы: >0 - ID созданной записи; <0 - Ошибка
        prep.registerOutParameter(12, java.sql.Types.VARCHAR);  // Секретный ключ
        prep.registerOutParameter(13, java.sql.Types.VARCHAR);  // Текстовое описание результата работы        
        
        prep.execute();
        
        log.info("*p_result=" + prep.getInt(11));
        log.info("*p_result_secret=" + prep.getString(12));
        log.info("*p_result_text=" + prep.getString(13));
        
        con.close();
        /*
create procedure usr_register
( in p_login           varchar(30)
, in p_password        varchar(32)
, in p_firstname       varchar(30)
, in p_lastname        varchar(30)
, in p_logo_url        varchar(128)
, in p_gender          varchar(6)
, in p_birthday        date
, in p_email           varchar(50)
, in p_phone           varchar(25)
, in p_actor_id        int
, out p_result         int
, out p_result_secret  varchar(32)
, out p_result_text    varchar(400)
)        
        */
        
        return 1;
    }
    
    
}
