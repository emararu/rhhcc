package com.rhhcc.user.data;

import com.rhhcc.common.type.Visibility;

/**
 * Интерфейс описывающий настройки пользователя в системе
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public interface UserSetting {
    
    /**
     * Устанавливает видимость даты рождения пользователя другим пользователям
     * @param visibility {@see UserDataVisibilityState}
     * @return Текущий пользователь
     */
    public UserSetting setIsBirthday(Visibility visibility);
        
    /**
     * Устанавливает видимость EMail пользователя другим пользователям
     * @param visibility {@see UserDataVisibilityState}
     * @return Текущий пользователь
     */
    public UserSetting setIsEmail(Visibility visibility);
        
    /**
     * Устанавливает видимость телефона пользователя другим пользователям
     * @param visibility {@see UserDataVisibilityState}
     * @return Текущий пользователь
     */
    public UserSetting setIsPhone(Visibility visibility);
        
    /**
     * Возвращает видимость даты рождения пользователя другим пользователям
     * @return {@see UserDataVisibilityState}
     */
    public Visibility getIsBirthday();
    
    /**
     * Возвращает видимость EMail пользователя другим пользователям
     * @return {@see UserDataVisibilityState}
     */
    public Visibility getIsEmail();
    
    /**
     * Возвращает видимость телефона пользователя другим пользователям
     * @return {@see UserDataVisibilityState}
     */
    public Visibility getIsPhone();
    
};