package com.rhhcc.common.notify;
 
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.ServletContextResource;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;

/**
 *
 * Реализация отправки email
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Service("mailMessage")
public class MailMessage implements Mail {
        
    @Autowired
    @Qualifier("mailSender")
    private JavaMailSender mailSender;
 
    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;
    
    @Autowired
    ServletContext servletContext;
    
    @Override
    public void send(final String to, final String subject, final String template, Context ctx) throws MessagingException {
        
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setTo(to);
        message.setSubject("RHHCC | " + subject);
        
        final String content = templateEngine.process(template, ctx);
        message.setText(content, true);
        
        ServletContextResource file = new ServletContextResource(servletContext, "/img/logo.png");
        message.addInline("logo", file);        

        mailSender.send(mimeMessage);
    }
    
}
