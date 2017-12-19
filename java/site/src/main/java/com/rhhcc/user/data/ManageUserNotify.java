package com.rhhcc.user.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.rhhcc.common.notify.MailMessage;
import com.rhhcc.common.type.DBResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Уведомление пользователя
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Service("manageUserNotify")
class ManageUserNotify {   

    private final Logger log = LoggerFactory.getLogger(ManageUserNotify.class);
    
    @Autowired
    @Qualifier("dataSource")
    private BasicDataSource ds;
        
    @Autowired
    @Qualifier("mailMessage")
    private MailMessage mail;
        
    /**
     * Установка в БД флага подтверждения об успешной отправке уведомления на почту пользователя
     * @param id        ID пользователя
     * @param email     EMail пользователя
     * @param secret    Секретный ключ
     */
    private void confirmSendMail(long id, String email, String secret) {

        try (Connection con = ds.getConnection()) {
            
            // Начало транзакции
            con.setAutoCommit(false);
            
            // Отметка ключа как ожидающего подтверждения
            String sql = "{ call usr_confirm_wait(?, ?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);
            
            // IN
            // * ID пользователя
            prep.setLong(1, id);
            // * Секретный ключ
            prep.setString(2, secret);
            // * Пользователь выполняющий действие
            prep.setLong(3, id);
            
            // OUT
            // * Результат работы: 1 - Успешно; <0 - Ошибка
            prep.registerOutParameter(4, java.sql.Types.INTEGER); 
            // * Текстовое описание результата работы        
            prep.registerOutParameter(5, java.sql.Types.VARCHAR);  

            prep.execute();

            DBResult result = new DBResult(prep.getLong(4), prep.getString(5));
            
            if (result.getId() >= 0) con.commit(); else con.rollback();            
            log.info(result.toString());
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
        }
    }
    
    /**
     * Отправка пользователю на электронную почту уведомления о подтверждении и установка в БД флага отправки
     * @param id        ID пользователя
     * @param firstname Имя пользователя
     * @param email     EMail пользователя
     * @param subject   Тема сообщения(если применимо)
     * @param template  Шаблон сообщения
     * @param secret    Секретный ключ
     * @param url       Путь для подтверждения секретного ключа
     */
    @Async
    private void sendEmail(long id, String firstname, String email, final String subject, final String template, String secret, String url) {
        
        log.info("Send email:" + email);
        log.info(url);
        
        try {           
            // Параметры передаваемые в шаблон письма
            Context ctx = new Context();
            ctx.setVariable("userFirstname", firstname);            
            ctx.setVariable("urlConfirm", url);
                        
            // Отправка письма
            mail.send(email, subject, template, ctx); 
            
            // Установка в БД флага подтверждения об успешной отправке уведомления на почту пользователя
            confirmSendMail(id, email, secret);
            
        } catch (Exception e) {
            log.info("Error:" + e.toString());
        }
        
        log.info("Sent");
    }
    
    /**
     * Отправка уведомления о регистрации пользователя на почту и установка в БД флага подтверждения
     * @param id        ID пользователя
     * @param firstname Имя пользователя
     * @param email     EMail пользователя
     * @param secret    Секретный ключ
     * @param url       Путь для подтверждения секретного ключа
     */
    public void create(long id, String firstname, String email, String secret, String url) {
        sendEmail(id, firstname, email, "Пожалуйста, подтвердите регистрацию", "register", secret,  url);
    }
    
    /**
     * Отправка уведомления о подтверждении электронного андреса и установка в БД флага подтверждения
     * @param id        ID пользователя
     * @param firstname Имя пользователя
     * @param email     EMail пользователя
     * @param secret    Секретный ключ
     * @param url       Путь для подтверждения секретного ключа
     */
    public void confirmEmail(long id, String firstname, String email, String secret, String url) {
        sendEmail(id, firstname, email, "Пожалуйста, подтвердите email", "confirm_email", secret,  url);
    }
    
}
