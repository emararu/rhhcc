package com.rhhcc.common.cache;

import com.rhhcc.common.type.DBResult;

/**
 * Интерфейс для работы с кэшированными данными
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public interface Cache {
    
    /**
     * Возвращает по ключю текстовое сообщение
     * @param key Числовой ключ текстотвого сообщения
     * @return Найденное текстовое сообщение по переданному ключу
     */
    public DBResult get(long key);
    
    /**
     * Очистка кэша
     */
    public void clear();
    
}
