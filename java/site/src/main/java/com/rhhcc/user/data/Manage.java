package com.rhhcc.user.data;

import java.util.ArrayList;
import com.rhhcc.common.type.DBResult;

/**
 * Интерфейс управления данными аккаунта пользователя
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public interface Manage {

    /**
     * Регистрация пользователя
     * @param user Данные пользователя
     * @return Результат создания пользователя DBResultCreate
     */
    public DBResult create(User user);
    
    /**
     * Подтверждение регистрации пользователя в системе
     * @param user_id ID пользователя
     * @param secret  Секретный ключ
     * @return Результат создания пользователя DBResultCreate
     */
    public DBResult confirm(long user_id, String secret);
    
    /**
     * Возвращает данные пользователя по ID
     * @param user_id ID пользователя
     * @return Данные пользователя
     */
    public User get(long user_id);
    
    /**
     * Возвращает привилегии пользователя по ID
     * @param user_id ID пользователя
     * @return Привилегии пользователя
     */
    public ArrayList<String> getPrivilege(long user_id);
    
}
