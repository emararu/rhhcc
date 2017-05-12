package com.rhhcc.user.data;

import com.rhhcc.common.type.Gender;
import java.time.LocalDate;

/**
 * Интерфейс описывающий пользователя в системе
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public interface User {
    
    /**
     * Устанавливает уникальный ID пользователя
     * @param id Уникальный идентификатор пользователя
     * @return Текущий пользователь
     */
    public User setId(long id);
    
    /**
     * Устанавливает логин пользователя
     * @param login Логин пользователя
     * @return Текущий пользователь
     */
    public User setLogin(String login);
    
    /**
     * Устанавливает пароль пользователя
     * @param password Пароль пользователя
     * @return Текущий пользователь
     */
    public User setPassword(String password);
    
    /**
     * Устанавливает уникальный ID пользователя предоставленный внешней аутентификацией
     * @param id Уникальный идентификатор пользователя
     * @return Текущий пользователь
     */
    public User setOauth(String id);
        
    /**
     * Устанавливает идентификатор провайдера внешней аутентификацией
     * @param id Уникальный идентификатор провайдера
     * @return Текущий пользователь
     */
    public User setProvider(int id);
    
    /**
     * Устанавливает имя пользователя
     * @param firstname Имя пользователя
     * @return Текущий пользователь
     */
    public User setFirstname(String firstname);
    
    /**
     * Устанавливает фамилию пользователя
     * @param lastname Фамилия пользователя
     * @return Текущий пользователь
     */
    public User setLastname(String lastname);
    
    /**
     * Устанавливает пол пользователя
     * @param gender Пол пользователя
     * @return Текущий пользователь
     */
    public User setGender(String gender);
    
    /**
     * Устанавливает число, месяц, год рождения пользователя
     * @param birthday Дата рождения пользователя
     * @param format   Формат даты
     * @return Текущий пользователь
     */
    public User setBirthday(LocalDate birthday);
    public User setBirthdayF(String birthday, String format);
        
    /**
     * Устанавливает EMail пользователя
     * @param email EMail пользователя
     * @return Текущий пользователь
     */
    public User setEmail(String email);
        
    /**
     * Устанавливает контактный телефон пользователя
     * @param phone Контактный телефон пользователя
     * @return Текущий пользователь
     */
    public User setPhone(String phone);
        
    /**
     * Устанавливает путь к аватарке пользователя
     * @param path Путь к аватарке пользователя
     * @return Текущий пользователь
     */
    public User setIcon(String path);

    
    /**
     * Возвращает уникальный ID пользователя
     * @return Уникальный идентификатор пользователя
     */
    public long getId();
    
    /**
     * Возвращает логин пользователя
     * @return Логин пользователя
     */
    public String getLogin();
    
    /**
     * Возвращает пароль пользователя
     * @return Пароль пользователя
     */
    public String getPassword();
            
    /**
     * Возвращает уникальный ID пользователя предоставленный внешней аутентификацией
     * @return Уникальный идентификатор пользователя
     */
    public String getOauth();
        
    /**
     * Возвращает идентификатор провайдера внешней аутентификацией
     * @return Уникальный идентификатор провайдера
     */
    public int getProvider();
    
    /**
     * Возвращает имя пользователя
     * @return Имя пользователя
     */
    public String getFirstname();
    
    /**
     * Возвращает фамилию пользователя
     * @return Фамилия пользователя
     */
    public String getLastname();
    
    /**
     * Возвращает пол пользователя
     * @return Пол пользователя
     */
    public Gender getGender();
    
    /**
     * Возвращает число, месяц, год рождения пользователя
     * @return Дата рождения пользователя
     */
    public LocalDate getBirthday();
        
    /**
     * Возвращает EMail пользователя
     * @return EMail пользователя
     */
    public String getEmail();
        
    /**
     * Возвращает контактный телефон пользователя
     * @return Контактный телефон пользователя
     */
    public String getPhone();
        
    /**
     * Возвращает путь к аватарке пользователя
     * @return Путь к аватарке пользователя
     */
    public String getIcon();

};