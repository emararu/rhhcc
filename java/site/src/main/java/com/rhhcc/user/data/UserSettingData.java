package com.rhhcc.user.data;

import java.io.Serializable;

import com.rhhcc.user.data.type.Visibility;

/**
 * Класс описывающий настройки пользователя в системе
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public class UserSettingData implements Serializable, UserSetting {
    
    private static final long serialVersionUID = 6403710278284455643L;
    
    /* 1. Видимость даты рождения пользователя другим пользователям */
    private Visibility isBirthday;
    /* 2. Видимость EMail пользователя другим пользователям */
    private Visibility isEmail;    
    /* 3. Видимость телефона пользователя другим пользователям */
    private Visibility isPhone;
            
    /* Setters */
    @Override public UserSetting setIsBirthday(Visibility visibility) { this.isBirthday = visibility; return this; }
    @Override public UserSetting setIsEmail(Visibility visibility)    { this.isEmail = visibility;    return this; }
    @Override public UserSetting setIsPhone(Visibility visibility)    { this.isPhone = visibility;    return this; }
        
    /* Getters */
    @Override public Visibility getIsBirthday() { return this.isBirthday; }
    @Override public Visibility getIsEmail()    { return this.isEmail;    }
    @Override public Visibility getIsPhone()    { return this.isPhone;    }
           
    
    @Override
    public String toString() {
        return "{ isBirthday:" + isBirthday + 
               ", isEmail:"    + isEmail    +
               ", isPhone:"    + isPhone    + 
               " }";
    }
}
