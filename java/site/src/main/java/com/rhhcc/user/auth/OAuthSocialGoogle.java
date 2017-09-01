package com.rhhcc.user.auth;

import java.io.IOException;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.github.scribejava.apis.GoogleApi20;
import com.rhhcc.user.data.User;
import com.rhhcc.user.data.UserData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Аутентификация пользователя через Google OAuth2
 * 
 * @author  EMararu
 * @version 0.00.01
 */
@Service("openAuthGoogle")
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OAuthSocialGoogle extends OAuthSocial implements OAuth {

    private final Logger log = LoggerFactory.getLogger(OAuthSocialGoogle.class);
    
    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper objectMapper;
    
    @NotNull
    @Value("${auth.google.id}") 
    private int provider;
    
    @NotNull
    @Value("${auth.google.client.clientId}") 
    private String clientId;
    
    @NotNull
    @Value("${auth.google.client.clientSecret}") 
    private String clientSecret;
    
    @NotNull
    @Value("${auth.google.client.clientScope}")
    private String clientScope;  
    
    @NotNull
    @Value("${auth.google.resource.userInfoUri}") 
    private String userInfoUri;  

    @NotNull
    @Value("${auth.google.resource.field.oauthId}")
    private String userOAuthId;
    
    @NotNull
    @Value("${auth.google.resource.field.firstname}")
    private String userFirstname;
    
    @NotNull
    @Value("${auth.google.resource.field.lastname}")
    private String userLastname;
           
    @NotNull 
    @Value("${auth.google.resource.field.gender}")
    private String userGender;
           
    @NotNull 
    @Value("${auth.google.resource.field.birthday}")
    private String userBirthday;
    
    @NotNull
    @Value("${auth.google.resource.field.email}")
    private String userEMail;
        
    @NotNull
    @Value("${auth.google.resource.field.icon}")
    private String userIcon;
        
    @NotNull
    @Value("${auth.google.resource.format.birthday}")
    private String userFormatBirthday;
                
    @Override
    public String login() {                
        return send_request(GoogleApi20.instance(), clientId, clientSecret, clientScope);
    }
    
    @Override
    public String data(Model model, String code, String state, String error) throws IOException {
        return response(model, code, state, error, userInfoUri);
    }

    @Override
    User parseData(String data) throws IOException {
        
        JsonNode jn = objectMapper.readTree(data);
        
        User user = new UserData()
                       .setProvider  (this.provider)
                       .setOauth     (jn.at(this.userOAuthId  ).asText())
                       .setFirstname (jn.at(this.userFirstname).asText())
                       .setLastname  (jn.at(this.userLastname ).asText())
                       .setGender    (jn.at(this.userGender   ).asText())
                       .setBirthdayOf(jn.at(this.userBirthday ).asText(), this.userFormatBirthday)
                       .setEmail     (jn.at(this.userEMail    ).asText())
                       .setIcon      (jn.at(this.userIcon     ).asText());
        
        return user;
    }
    
}
