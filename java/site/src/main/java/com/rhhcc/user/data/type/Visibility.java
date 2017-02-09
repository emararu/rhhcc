package com.rhhcc.user.data.type;

/**
 * Видимость полей текущего пользователя другим пользователям: 
 * <ul>
 * <li><i>NOBODY</i>      Никому не видно</li>
 * <li><i>REGISTERED</i>  Только зарегистрированным пользователям</li>
 * <li><i>ALL</i>         Все видят</li>
 * </ul>
 * 
 * @author  EMararu
 * @version 0.00.01
 */
public enum Visibility { NOBODY, REGISTERED, ALL }
