package com.rhhcc.user.data;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhhcc.common.type.DBResult;
import com.rhhcc.user.auth.SpringAuth;
import com.rhhcc.user.type.DBResultCreate;
import com.rhhcc.user.type.DBResultUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс реализующий управление пользователем в системе
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Service("manageUser")
public class ManageUser {
    
    private final Logger log = LoggerFactory.getLogger(ManageUser.class);
    
    @Autowired
    @Qualifier("manageUserSelect")
    private ManageUserSelect select;
    
    @Autowired
    @Qualifier("manageUserModify")
    private ManageUserModify modify;
    
    @Autowired
    @Qualifier("manageUserNotify")
    private ManageUserNotify notify;
    
    @Autowired
    @Qualifier("springAuth")
    private SpringAuth springAuth;
    
    @Autowired
    private HttpServletRequest context;
    
    /**
     * Регистрация пользователя
     * @param user Данные пользователя
     * @return Результат создания пользователя DBResultCreate
     */
    public DBResult create(User user) {
        
        log.info(user.toString());
        
        // Создание пользователя в БД
        DBResultCreate result = (DBResultCreate)modify.create(user);
        // Если пользователь успешно создан
        if (result.getId() >= 0) {
            // Отправка уведомления о регистрации пользователя на почту и установка в БД флага подтверждения
            notify.create(result.getId(), user.getFirstname(), user.getEmail(), result.getSecret(), result.secretConfirmURL(context, "/user/confirm/"));
        }
        
        log.info(result.toString());
        
        return result;
    }
    
    /**
     * Обновление логина и пароля пользователя
     * @param user Данные пользователя
     * @return Результат обновления данных пользователя
     */
    public DBResult updateAuth(User user) {
        return modify.updateAuth(user);
    }

    /**
     * Обновление данных пользователя в БД
     * @param user Данные пользователя
     * @return Результат обновления данных пользователя
     */
    public DBResult update(User user) {
        
        log.info(user.toString());
        
        // Обновление данных пользователя в БД
        DBResultUpdate result = (DBResultUpdate)modify.update(user);
        // Если данные пользователь успешно обновлены
        if (result.getId() >= 0) {
            // Если необходимо подтвердить EMail
            if (!result.getSecret().isEmpty()) {
                // Отправка уведомления о подтверждении электронного андреса и установка в БД флага подтверждения
                notify.confirmEmail(result.getId(), user.getFirstname(), user.getEmail(), result.getSecret(), result.secretConfirmURL(context, "/user/confirm_email/"));
            }
            // Если необходимо подтвердить телефонный номер
            if (!result.getVerify().isEmpty()) {
                // Отправка кода верификации для подтверждения телефонного номера
            }
        }
        
        log.info(result.toString());
        
        return result;
    }
        
    /**
     * Слияние и обновление данных пользователя в БД и из системы внешней 
     * аутентификации
     * @param user Данные пользователя
     * @return Результат слияния и обновления данных пользователя
     */
    public DBResult merge(User user) {        
        
        log.info(user.toString());
        
        // Слияние и обновление данных пользователя
        DBResult result = modify.merge(user);
        log.info(result.toString());

        return result;
    }
       
    /**
     * Подтверждение регистрации пользователя в системе
     * @param user_id ID пользователя
     * @param secret  Секретный ключ
     * @return Результат создания пользователя DBResultCreate
     */
    public DBResult confirmAuth(long user_id, String secret) {
        
        log.info("user_id="+user_id+", secret="+secret);
        
        // Подтверждение регистрации пользователя в системе
        DBResult result = modify.confirmAuth(user_id, secret);        
        log.info(result.toString());

        // Если подтверждение данных выполнено успешно
        if (result.getId() >= 0) {
            // Старт сессии указанного пользоваетя для работы в системе
            this.startSession(user_id);
        }
        
        return result;
    }
       
    /**
     * Подтверждение email пользователя в системе
     * @param user_id ID пользователя
     * @param secret  Секретный ключ
     * @return Результат создания пользователя DBResultCreate
     */
    public DBResult confirmEmail(long user_id, String secret) {
        
        log.info("user_id="+user_id+", secret="+secret);
        
        // Подтверждение регистрации пользователя в системе
        DBResult result = modify.confirmEmail(user_id, secret);        
        log.info(result.toString());
        
        return result;
    }
       
    /**
     * Подтверждение телефонного номера пользователя в системе
     * @param user_id ID пользователя
     * @param verify  Код верификации телефонного номера
     * @return Результат создания пользователя DBResultCreate
     */
    public DBResult confirmPhone(long user_id, String verify) {
        
        log.info("user_id="+user_id+", verify="+verify);
        
        // Подтверждение регистрации пользователя в системе
        DBResult result = modify.confirmPhone(user_id, verify);        
        log.info(result.toString());
        
        return result;
    }
    
    /**
     * Возвращает данные пользователя по ID
     * @param user_id ID пользователя
     * @return Данные пользователя
     */
    public User getUser(long user_id) {
        return select.getUser(user_id);
    }
 
    /**
     * Возвращает привилегии пользователя по ID
     * @param user_id ID пользователя
     * @return Привилегии пользователя
     */
    public ArrayList<String> getPrivilege(long user_id) {        
        return select.getPrivilege(user_id);
    }
        
    /**
     * Аутентификация пользователя в системе
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return Результат аутентификации пользователя в БД
     */
    public DBResult login(String login, String password) {
       
        log.info("login="+login+", password="+password);
        
        // Аутентификация пользователя в БД            
        DBResult result = select.login(login, password);
        // Если пользователь аутентифицирован
        if (result.getId() >= 0) {
            // Старт сессии указанного пользоваетя для работы в системе
            this.startSession(result.getId());
        }
        
        log.info(result.toString());
        
        return result;
    }
    
    /**
     * Старт сессии указанного пользоваетя для работы в системе
     * После вызова методов Manage.login, Manage.confirm, OAuthSosial.response 
     * вызывать не нужно, тк вызов выполняется внутри перечисленных методов.
     * @param user Данные пользователя
     */
    public void startSession(User user) {        
        log.info(user.toString());
        // Привилегии пользователя
        ArrayList<String> privilege = this.getPrivilege(user.getId());
        // Аутентификация пользователя в spring security
        springAuth.process(user, privilege); 
    }
    
    /**
     * Старт сессии указанного пользоваетя для работы в системе
     * После вызова методов Manage.login, Manage.confirm, OAuthSosial.response 
     * вызывать не нужно, тк вызов выполняется внутри перечисленных методов.
     * @param user_id ID пользователя
     */
    public void startSession(long user_id) {
        // Если ID пользователя указан
        if (user_id > 0) {
            // Данные пользователя
            User user = select.getUser(user_id);
            // Старт сессии
            if (user != null) startSession(user);
        }    
    }
        
}
