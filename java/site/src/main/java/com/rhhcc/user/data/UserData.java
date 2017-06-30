package com.rhhcc.user.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import org.apache.commons.codec.digest.DigestUtils;

import com.rhhcc.common.type.Gender;

/**
 * Класс описывающий пользователя в системе
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public class UserData implements Serializable, User{
    
    private static final long serialVersionUID = -3184945331555937705L;

    /* 1. Уникальный идентификатор пользователя */
    private long id;  
    /* 2. Логин пользователя */
    private String login;  
    /* 3. Пароль пользователя */
    private String password;  
    /* 4. Уникальный идентификатор пользователя предоставленный внешней аутентификацией */
    private String oauth;
    /* 5. Уникальный идентификатор провайдера внешней аутентификацией */
    private int provider;
    /* 6. Имя пользователя */
    private String firstname;
    /* 7. Фамилия пользователя */
    private String lastname;
    /* 8. Пол */
    private Gender gender;
    /* 9. Дата рождения пользователя */
    @JsonFormat(shape = Shape.STRING, pattern = "dd.MM.yyyy")
    @DateTimeFormat(pattern = "dd.MM.yyyy") 
    private LocalDate birthday;
    /* 10. EMail пользователя */
    private String email;
    /* 11. Контактный телефон пользователя */
    private String phone;
    /* 12. Путь к аватарке пользователя */
    private String icon;
     
    /**
     * Обрабатывает и возвращает строковое значение
     * @param value Исходное строковое значение
     * @return Обрабатанное строковое значение
     */
    private String processString(String value) {
        return (value == null || value.equals("")) ? null : value;
    }  
    
    /**
     * Обрабатывает и возвращает пароль
     * @param value Исходный пароль
     * @return Обрабатанный пароль
     */
    private String processPassword(String value) {
        return (value == null || value.equals("")) ? null : DigestUtils.md5Hex(value);
    }    
    
    @Override 
    public User setGender(String gender) { 
        if (gender.equals("male"))
            this.gender = Gender.MALE;
        else if (gender.equals("female")) 
            this.gender = Gender.FEMALE;
        return this;
    }   
    
    @Override 
    public User setBirthdayF(String birthday, String format) { 
        if (!birthday.equals("")) {
            DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
            this.birthday = LocalDate.parse(birthday, f); 
        }
        return this;
    }
    
    /* Setters */
    @Override public User setId(long id)                  { this.id        = id;                        return this; }
    @Override public User setLogin(String login)          { this.login     = processString(login);      return this; }
    @Override public User setPassword(String password)    { this.password  = processPassword(password); return this; }
    @Override public User setOauth(String id)             { this.oauth     = processString(id);         return this; }
    @Override public User setProvider(int id)             { this.provider  = id;                        return this; }
    @Override public User setFirstname(String firstname)  { this.firstname = processString(firstname);  return this; }
    @Override public User setLastname(String lastname)    { this.lastname  = processString(lastname);   return this; }
    @Override public User setBirthday(LocalDate birthday) { this.birthday  = birthday;                  return this; }
    @Override public User setEmail(String email)          { this.email     = processString(email);      return this; }
    @Override public User setPhone(String phone)          { this.phone     = processString(phone);      return this; }
    @Override public User setIcon(String path)            { this.icon      = processString(path);       return this; } 
        
    /* Getters */
    @Override public long       getId()         { return this.id;                              }
    @Override public String     getLogin()      { return this.login;                           }
    @Override public String     getPassword()   { return this.password;                        }
    @Override public String     getOauth()      { return this.oauth;                           }
    @Override public int        getProvider()   { return this.provider;                        }
    @Override public String     getFirstname()  { return this.firstname;                       }
    @Override public String     getLastname()   { return this.lastname;                        }
    @Override public String     getName()       { return this.firstname + " " + this.lastname; }
    @Override public String     getEmail()      { return this.email;                           }
    @Override public String     getPhone()      { return this.phone;                           }
    @Override public String     getIcon()       { return this.icon;                            } 
    @Override public Gender     getGender()     { return this.gender;                          }
    @Override public LocalDate  getBirthday()   { return this.birthday;                        }
           
    
    @Override
    public String toString() {
        return "{ id:"        + id        + 
               ", login:"     + login     + ", password:"   + password   +
               ", firstname:" + firstname + ", lastname:"   + lastname   + 
               ", oauth:"     + oauth     + ", provider:"   + provider   + 
               ", gender:"    + gender    +
               ", birthday:"  + birthday  +
               ", email:"     + email     +
               ", phone:"     + phone     +
               ", icon:"      + icon      + 
               " }";
    }
}
