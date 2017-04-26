package com.rhhcc.user.auth;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.github.scribejava.apis.FacebookApi;
import com.rhhcc.user.data.User;
import com.rhhcc.user.data.UserData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Аутентификация пользователя через Facebook OAuth2
 * 
 * @author  EMararu
 * @version 0.00.01
 */
@Service("openAuthFacebook")
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OAuthSocialFacebook extends OAuthSocial implements OAuth {

    private final Logger log = LoggerFactory.getLogger(OAuthSocialFacebook.class);
    
    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper objectMapper;
    
    @NotNull
    @Value("${auth.facebook.id}") 
    private int provider;
    
    @NotNull
    @Value("${auth.facebook.client.clientId}") 
    private String clientId;
    
    @NotNull
    @Value("${auth.facebook.client.clientSecret}") 
    private String clientSecret;
    
    @NotNull
    @Value("${auth.facebook.client.clientScope}")
    private String clientScope;
    
    @NotNull
    @Value("${auth.facebook.resource.userInfoUri}") 
    private String userInfoUri; 
    
    @NotNull
    @Value("${auth.facebook.resource.field.oauthId}")
    private String userOAuthId;
    
    @NotNull
    @Value("${auth.facebook.resource.field.firstname}")
    private String userFirstname;
    
    @NotNull
    @Value("${auth.facebook.resource.field.lastname}")
    private String userLastname;
      
    @NotNull      
    @Value("${auth.facebook.resource.field.gender}")
    private String userGender;
        
    @NotNull    
    @Value("${auth.facebook.resource.field.birthday}")
    private String userBirthday;
    
    @NotNull
    @Value("${auth.facebook.resource.field.email}")
    private String userEMail;
        
    @NotNull
    @Value("${auth.facebook.resource.field.icon}")
    private String userIcon;
        
    @NotNull
    @Value("${auth.facebook.resource.format.birthday}")
    private String userFormatBirthday;
            
    @Override
    public String login() {
        return send_request(FacebookApi.instance(), clientId, clientSecret, clientScope);
    }
    
    @Override
    public String data(HttpSession session, Model model, String code, String state, String error) throws IOException {
        return response(session, model, code, state, error, userInfoUri);
    }

    @Override
    User parseData(String data) throws IOException {                
        
        JsonNode jn = objectMapper.readTree(data);
        
        User user = new UserData()
                       .setProvider (this.provider)
                       .setOauth    (jn.at(this.userOAuthId  ).asText())
                       .setFirstname(jn.at(this.userFirstname).asText())
                       .setLastname (jn.at(this.userLastname ).asText())
                       .setGender   (jn.at(this.userGender   ).asText())
                       .setBirthdayF(jn.at(this.userBirthday ).asText(), this.userFormatBirthday)
                       .setEmail    (jn.at(this.userEMail    ).asText())
                       .setIcon     (jn.at(this.userIcon     ).asText());
    
        return user;
    }
       
}
