package com.rhhcc.user.type;

/**
 * Результат ответа БД на отправленный запрос обновления данных пользователя
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public class DBResultUpdate extends DBResultCreate {
    
    private final String verify;
        
    /**
     * Результат ответа БД
     * @param id     Больше 0 идентификатор обработанной записи; Меньше 0 ошибка во время выполнения
     * @param secret Секретный ключ для подтверждения email
     * @param verify Проверочный код для подтверждения телефонного номера
     * @param text   Текстовое пояснение ответа БД
     */
    public DBResultUpdate(long id, String secret, String verify, String text) {
        super(id, secret, text);
        this.verify = verify;
    } 
    
    /**
     * Возвращает проверочный код для подтверждения телефонного номера
     * @return Секретный ключ
     */
    public String getVerify() {
        return this.verify;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", verify=" + getVerify();
    }
    
}
