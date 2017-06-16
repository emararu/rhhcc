package com.rhhcc.user.type;

import com.rhhcc.common.type.DBComplete;
import com.rhhcc.common.type.DBResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Результат ответа БД на отправленный запрос регистрации нового пользователя
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public class DBResultCreate extends DBResult {
        
    private final String secret;
    
    
    /**
     * Результат ответа БД
     * @param complete Тип отображаемой страницы с результатом выполнения запроса к БД
     * @param id       Больше 0 идентификатор обработанной записи; Меньше 0 ошибка во время выполнения
     * @param secret   Секретный ключ
     * @param text     Текстовое пояснение ответа БД
     */
    public DBResultCreate(DBComplete complete, long id, String secret, String text) {
        super(complete, id, text);
        this.secret = secret;
    }        
    
    /**
     * Возвращает секретный ключ
     * @return Секретный ключ
     */
    public String getSecret() {
        return this.secret;
    }
        
    /**
     * Возвращает URL для подтверждения данных по секретному ключу
     * @param context  Контекст сервлета
     * @param path     Адрес для подтверждения ключа
     * @return URL для подтверждения данных
     */
    public String secretConfirmURL(final HttpServletRequest context, String path) {    
        StringBuffer u = context.getRequestURL();
        String p = context.getContextPath();        
        return u.substring(7, u.indexOf(p)+p.length())+path+this.getId()+"/"+this.getSecret()+"/";
    }
    
    @Override
    public String toString() {
        return super.toString() + ", secret=" + getSecret();
    }
    
}
