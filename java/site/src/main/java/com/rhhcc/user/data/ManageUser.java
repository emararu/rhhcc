package com.rhhcc.user.data;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhhcc.common.type.DBComplete;
import com.rhhcc.common.type.DBResult;
import com.rhhcc.user.auth.SpringAuth;
import com.rhhcc.user.type.DBResultCreate;
import com.rhhcc.user.type.DBResultMerge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс реализующий управление пользователем в системе
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Service("manageUser")
public class ManageUser implements Manage {
    
    private final Logger log = LoggerFactory.getLogger(ManageUser.class);
    
    @Autowired
    @Qualifier("dataSource")
    private BasicDataSource ds;
    
    @Autowired
    @Qualifier("manageUserNotify")
    private ManageUserNotify notify;
    
    @Autowired
    @Qualifier("springAuthService")
    private SpringAuth springAuth;
    
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
     * Отправляет в БД запрос на слияние и обновление данных пользователя
     * @param user Данные пользователя
     * @return Результат слияния и обновления данных пользователя
     */
    private DBResult mergeUser(User user) {
        
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
            result = new DBResult(DBComplete.register, -600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(DBComplete.register, -600, e.toString());
        }
        
        return result;
    }
    
    /**
     * Отправляет в БД запрос на подтверждение регистрации пользователя
     * @param user_id ID пользователя
     * @param secret  Секретный ключ
     * @return Результат ответа БД на отправленный запрос
     */
    private DBResult confirmUser(long user_id, String secret) {
        
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
     * Отправляет в БД запрос на Аутентификацию пользователя в системе
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return Результат аутентификации пользователя в БД
     */
    private DBResult loginUser(String login, String password) {
        
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
    
    @Override
    public DBResult create(User user) {
        
        log.info(user.toString());
        
        // Создание пользователя в БД
        DBResultCreate result = (DBResultCreate)createUser(user);
        // Если пользователь успешно создан
        if (result.getId() >= 0) {
            // Отправка уведомления о регистрации пользователя на почту и установка в БД флага подтверждения
            notify.create(result.getId(), user.getFirstname(), user.getEmail(), result.getSecret(), result.secretConfirmURL(context, "/user/confirm/"));
        }
        
        log.info(result.toString());
        
        return result;
    }
    
    @Override
    public DBResult merge(User user) {        
        
        log.info(user.toString());
        
        // Слияние и обновление данных пользователя
        DBResult result = mergeUser(user);
        log.info(result.toString());

        return result;
    }
       
    @Override
    public DBResult confirm(long user_id, String secret) {
        
        log.info("user_id="+user_id+", secret="+secret);
        
        // Подтверждение регистрации пользователя в системе
        DBResult result = confirmUser(user_id, secret);        
        log.info(result.toString());

        // Если подтверждение данных выполнено успешно
        if (result.getId() >= 0) {
            // Старт сессии указанного пользоваетя для работы в системе
            this.startSession(user_id);
        }
        
        return result;
    }
    
    @Override
    public User get(long user_id) {
        
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
 
    @Override
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
        
    @Override
    public DBResult login(String login, String password) {
       
        log.info("login="+login+", password="+password);
        
        // Аутентификация пользователя в БД            
        DBResult result = loginUser(login, password);
        // Если пользователь аутентифицирован
        if (result.getId() >= 0) {
            // Старт сессии указанного пользоваетя для работы в системе
            this.startSession(result.getId());
        }
        
        log.info(result.toString());
        
        return result;
    }
    
    @Override
    public void startSession(User user) {        
        log.info(user.toString());
        // Привилегии пользователя
        ArrayList<String> privilege = this.getPrivilege(user.getId());
        // Аутентификация пользователя в spring security
        springAuth.process(user, privilege); 
    }
    
    @Override
    public void startSession(long user_id) {
        // Если ID пользователя указан
        if (user_id > 0) {
            // Данные пользователя
            User user = this.get(user_id);
            // Старт сессии
            if (user != null) startSession(user);
        }    
    }
        
}
