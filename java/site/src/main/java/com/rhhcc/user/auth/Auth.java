package com.rhhcc.user.auth;

import java.util.ArrayList;
import com.rhhcc.user.data.User;

/**
 * Интерфейс описывающий общие функции для всех доступных типов аутентификация пользователя в системе
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public interface Auth {
    
    /**
     * Проверка аутентификации пользователя в системе
     * @return True - Пользователь аутентифицирован, False - Пользователь не аутентифицирован
     */
    public boolean isAuth();
    
    /**
     * Выполняет аутентификация пользователя в spring security
     * @param user      Данные пользователя
     * @param privilege Привилегии пользователя
     */
    public void process(User user, ArrayList<String> privilege);
    
}
