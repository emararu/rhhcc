package com.rhhcc.user.data;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhhcc.common.type.DBResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс реализующий возможность получения данных пользователя
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Service("manageUserSelect")
class ManageUserSelect {
    
    private final Logger log = LoggerFactory.getLogger(ManageUserSelect.class);
    
    @Autowired
    @Qualifier("dataSource")
    private BasicDataSource ds;    
    
    /**
     * Отправляет в БД запрос на Аутентификацию пользователя в системе
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return Результат аутентификации пользователя в БД
     */
    public DBResult login(String login, String password) {
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
                        
            // Выполняет проверку логина и пароля и возвращает ID найденного пользователя
            String sql = "{ call usr_login(?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * Логин пользователя
            prep.setString(1, login);
            // * Пароль пользователя
            prep.setString(2, password);

            // OUT
            // * Результат работы: >0 - ID пользователя; 0 - Проверка не выполнена; <0 - Ошибка
            prep.registerOutParameter(3, java.sql.Types.INTEGER);     
            // * Текстовое описание результата работы        
            prep.registerOutParameter(4, java.sql.Types.VARCHAR);  

            prep.execute();

            result = new DBResult(prep.getLong(3), prep.getString(4));
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(-600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(-600, e.toString());
        }
        
        return result;
    }
    
    public User getUser(long user_id) {
        
        User user = null;
        
        try (Connection con = ds.getConnection()) {
            
            // Запрос для выборки данных пользователя
            String sql = "select if(ug.state = 'confirm' and ugm.provider_id = 0, ugm.id, null) group_id\n"
                       + "     , a.login\n"
                       + "     , u.out_id\n"
                       + "     , u.provider_id\n"
                       + "     , u.firstname\n"
                       + "     , u.lastname\n"
                       + "     , u.gender\n"
                       + "     , u.birthday\n"
                       + "     , u.email\n"
                       + "     , u.phone\n"
                       + "     , u.logo_url\n"
                       + "from usr_user u left join usr_auth a on u.sys_status = 'on' and a.sys_status = 'on' and u.id = a.user_id\n"
                       + "   , usr_user_group ug\n"
                       + "   , usr_user       ugm\n"
                       + "where u.id = ?\n"
                       + "  and u.id = ug.user_id\n"
                       + "  and ug.sys_status = 'on'\n"
                       + "  and ug.main_id = ugm.id\n"
                       + "  and ugm.sys_status = 'on'";
          
            log.info(sql);
            // Разбор запроса
            PreparedStatement prep = con.prepareStatement(sql);
            // Связывание переменных
            prep.setLong(1, user_id);
            // Выполнение
            ResultSet rs = prep.executeQuery();
            // Выборка данных 
            if (rs.next()) {
                // Заполнение данными пользователя
                user = new UserData()
                           .setId(user_id)
                           .setGroupId(rs.getLong("group_id"))
                           .setLogin(rs.getString("login"))
                           .setOauth(rs.getString("out_id"))
                           .setProvider(rs.getShort("provider_id"))
                           .setFirstname(rs.getString("firstname"))
                           .setLastname(rs.getString("lastname"))
                           .setGender(rs.getString("gender"))
                           .setBirthdayOf(rs.getDate("birthday"))
                           .setEmail(rs.getString("email"))
                           .setPhone(rs.getString("phone"))
                           .setIcon(rs.getString("logo_url"));
            }
            // Закрытие result set
            rs.close();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
        }
        
        return user;
    }
 
    public ArrayList<String> getPrivilege(long user_id) {
        
        ArrayList<String> privilege = new ArrayList<>();
        
        try (Connection con = ds.getConnection()) {
            
            // Запрос для выборки данных пользователя
            String sql = "select role_id from usr_user_role where sys_status = 'on' and user_id = ?";
            log.info(sql);
            // Разбор запроса
            PreparedStatement prep = con.prepareStatement(sql);
            // Связывание переменных
            prep.setLong(1, user_id);
            // Выполнение
            ResultSet rs = prep.executeQuery();
            // Выборка данных 
            while (rs.next()) {
                // Заполнение данными
                privilege.add(rs.getString("role_id"));  
            }      
            log.info(privilege.toString());            
            // Закрытие result set
            rs.close();
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
        }
        
        return privilege;
    }
    
}