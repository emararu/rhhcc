package com.rhhcc.user.type;

import com.rhhcc.common.type.DBResult;

/**
 * Результат ответа БД на отправленный запрос слияния данных пользователя 
 * из системы внешней аутентификации и данных в БД
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public class DBResultMerge extends DBResult {
        
    private final long groupId;
        
    /**
     * Результат ответа БД
     * @param id      Больше 0 идентификатор обработанной записи; Меньше 0 ошибка во время выполнения
     * @param groupId ID группировки пользователя;
     * @param text    Текстовое пояснение ответа БД
     */
    public DBResultMerge(long id, long groupId, String text) {
        super(id, text);
        this.groupId = groupId;
    }
    
    /**
     * Возвращает ID группировки пользователя;
     * @return ID группировки пользователя;
     */
    public long getGroupId() {
        return this.groupId;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", groupId=" + getGroupId();
    }
    
}
