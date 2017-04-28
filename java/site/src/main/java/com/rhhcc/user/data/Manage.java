package com.rhhcc.user.data;

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
     * @return Результат создания пользователя
     */
    public long create(User user);
    
    
    public User get();
    
}
