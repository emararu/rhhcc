package com.rhhcc.common.type;

import org.springframework.ui.Model;

/**
 * Стандартный результат ответа БД на отправленный запрос
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public class DBResult {
    
    private final DBComplete complete;
    private final String path;
    private final long id;
    private final String text;
    
    /**
     * Результат ответа БД
     * @param complete Тип отображаемой страницы с результатом выполнения запроса к БД
     * @param id       Больше 0 идентификатор обработанной записи; Меньше 0 ошибка во время выполнения
     * @param text     Текстовое пояснение ответа БД
     */
    public DBResult(DBComplete complete, long id, String text) {
        this.complete = complete;
        this.path = "/complete/" + complete.name();
        this.id = id;
        this.text = text;
    }
    
    /**
     * Результат ответа БД
     * @param id       Больше 0 идентификатор обработанной записи; Меньше 0 ошибка во время выполнения
     * @param text     Текстовое пояснение ответа БД
     */
    public DBResult(long id, String text) {
        this.complete = null;
        this.path = "/";
        this.id = id;
        this.text = text;
    }
    
    /**
     * Возвращает тип отображаемой страницы с результатом успешного выболнения запроса к БД
     * @return Тип отображаемой страницы
     */
    public DBComplete getComplete() {
        return this.complete;
    }
    
    /**
     * Возвращает путь для перехода на страницу с успешным результатом выполнения запроса к БД
     * @return Путь
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     * Возвращает код ответа БД
     * @return Больше 0 идентификатор обработанной записи; Меньше 0 ошибка во время выполнения
     */
    public long getId() {
        return this.id;
    }
    
    /**
     * Возвращает текстовое пояснение ответа БД
     * @return Текстовое пояснение ответа БД
     */
    public String getText() {
        return this.text;
    }
    
    /**
     * Возвращает строку для перехода на страницу с результатом действия
     * @param model Параметры jsp-страницы
     * @return Путь к странице с результатом обработки
     */
    public String doReturn(Model model) {    
        if (this.getId() >= 0) {
            return "forward:"+this.getPath();
        } else {
            model.addAttribute("complete_type", this.getComplete().name());
            model.addAttribute("error_descr", this.getText());
            return "complete";
        }
    }
    
    @Override
    public String toString() {
        return "path=" + this.getPath() + ", id=" + this.getId() + ", text=" + this.getText();
    }
    
}
