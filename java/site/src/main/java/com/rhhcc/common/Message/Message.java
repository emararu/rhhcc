package com.rhhcc.common.Message;

import javax.mail.MessagingException;
import org.thymeleaf.context.Context;

/**
 *
 * Интерфейс отправки сообщений
 * 
 * @author EMararu
 * @version 0.00.01
 */
public interface Message {
    
    /**
     * Отправка сообщения
     * @param to       Адресат сообщения
     * @param subject  Тема сообщения(если применимо)
     * @param template Шаблон сообщения
     * @param ctx      Параметы передаваемые в шаблон сообщения
     */
    public void sendMail(final String to, final String subject, final String template, Context ctx) throws MessagingException;
}
