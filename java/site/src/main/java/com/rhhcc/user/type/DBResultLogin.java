package com.rhhcc.user.type;

import com.rhhcc.common.type.DBResult;

/**
 * Результат ответа БД на отправленный запрос аутентификации пользователя 
 * в системе по данным в БД
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public class DBResultLogin extends DBResult {
        
    private final long group_id;
        
    /**
     * Результат ответа БД
     * @param id       Больше 0 идентификатор обработанной записи; Меньше 0 ошибка во время выполнения
     * @param group_id ID группировки пользователя;
     * @param text     Текстовое пояснение ответа БД
     */
    public DBResultLogin(long id, long group_id, String text) {
        super(id, text);
        this.group_id = group_id;
    }
    
    /**
     * Возвращает ID группировки пользователя;
     * @return ID группировки пользователя;
     */
    public long getGroupId() {
        return this.group_id;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", group_id=" + getGroupId();
    }
    
}
