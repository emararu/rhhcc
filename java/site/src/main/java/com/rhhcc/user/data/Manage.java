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
     * Создание пользователя
     * @param user Данные пользователя
     * @return Результат создания пользователя @see DBResultCreate
     */
    public DBResult create(User user);
    
    
    // public User get();
    
}
