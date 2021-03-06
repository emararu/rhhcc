package com.rhhcc.user.auth;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;

/**
 * Интерфейс аутентификации пользователя в системе по средствам 
 * протокола открытой аутентификации(OAuth)
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public interface OAuth {
    
    /**
     * Аутентификация пользователя в системе
     * @return URL на который необходимо перенаправить пользователя для аутентификации
     */
    public String login();
    
    /**
     * Отправляет запрос данных пользователя и принимает ответ от сервиса предоставляющего внешнюю аутентификацию
     * @param session     Объект запроса для хранения сессионных данных пользователя
     * @param model       Объект для хранения данных пользователя на время запроса
     * @param code        Токен присланный сервисом предоставляющего внешнюю аутентификацию для запроса данных пользователя
     * @param state       Секретный ключ возвращенный сервисом предоставляющим внешнюю аутентификацию для предотвращения межсайтовых атак
     * @param error       Ошибка возникшая во время внешней аутентификации
     * @return URL на который необходимо перенаправить пользователя для аутентификации
     * @throws IOException 
     */
    public String data(HttpSession session, Model model, String code, String state, String error) throws IOException;
    
}
