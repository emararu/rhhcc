package com.rhhcc.user.data;

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
    
    // public User get();
    
}
