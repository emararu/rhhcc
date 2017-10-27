package com.rhhcc.user.auth;

import java.io.IOException;
import java.util.Random;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
        
import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import com.rhhcc.common.cache.Cache;
import com.rhhcc.user.data.ManageUser;
import com.rhhcc.user.data.User;
import com.rhhcc.user.type.DBResultMerge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Базовый класс для аутентификация пользователя в сервисе предоставляющем 
 * возможность внешней аутентификации по протоколу OAuth2
 * 
 * @author  EMararu
 * @version 0.00.01
 */
abstract class OAuthSocial {    

    private final Logger log = LoggerFactory.getLogger(OAuthSocial.class);
        
    @Autowired
    @Qualifier("manageUser")
    private ManageUser manageUser; 
    
    @Autowired
    @Qualifier("cacheMessage")
    private Cache cache;
    
    // Сервис предоставляющий внешнюю аутентификацию
    private OAuth20Service service;
    // Секретный ключ для предотвращения межсайтовых атак
    private String secretState;
    
    /**
     * Отправляет запрос внешней аутентификации
     * @param api          Сервис внешней аутентификации
     * @param clientId     Идентификатор клиентского приложения в сервисе предоставляющем внешнюю аутентификацию
     * @param clientSecret Секретный ключ клиентского приложения в сервисе предоставляющем внешнюю аутентификацию
     * @param clientScope  Перечень данных которые необходимо получить от сервиса внешней аутентификации(указывается в соответствии с правилами сервиса внешней аутентификации)
     * @return URL на который необходимо перенаправить пользователя для аутентификации
     */
    protected String send_request(BaseApi<OAuth20Service> api, String clientId, String clientSecret, String clientScope) {
        // URL по которому выполняется запрос на внешнюю аутентификацию
        final String urlCurrent  = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getRequestURL().toString();
        // URL на который необходимо направить ответ
        final String urlCallback = urlCurrent.substring(0, urlCurrent.lastIndexOf("/")+1) + "data";
        log.info("urlCallback=" + urlCallback);
                
        // Секретный ключ для предотвращения межсайтовых атак
        secretState = "secret" + new Random().nextInt(999_999);
        
        // Экземпляр сервиса предоставляющего внешнюю аутентификацию
        service = new ServiceBuilder()
                     .apiKey(clientId)
                     .apiSecret(clientSecret)
                     .scope(clientScope)
                     .state(secretState)
                     .callback(urlCallback)
                     .build(api);
        
        // URL на который необходимо перенаправить пользователя для аутентификации
        final String urlAuth = service.getAuthorizationUrl();
        log.info("urlAuth=" + urlAuth);

        return urlAuth;
    }
    
    /**
     * Отправляет запрос данных пользователя и принимает ответ от сервиса предоставляющего внешнюю аутентификацию
     * @param model       Объект для хранения данных пользователя на время запроса
     * @param code        Токен присланный сервисом предоставляющего внешнюю аутентификацию для запроса данных пользователя
     * @param state       Секретный ключ возвращенный сервисом предоставляющим внешнюю аутентификацию для предотвращения межсайтовых атак
     * @param error       Ошибка возникшая во время внешней аутентификации
     * @param userInfoUri URL на который будет отпрален запрос данных необходимых от сервиса внешней аутентификации
     * @return URL на который необходимо перенаправить пользователя для аутентификации
     * @throws IOException 
     */
    protected String response(Model model, String code, String state, String error, String userInfoUri) throws IOException {
        
        log.info("code=" + code + ", state=" + state + ", error=" + error + ", userInfoUri=" + userInfoUri);
        
        // URL на который будет перенаправлен пользователь после аутентификации
        String urlAuth;
        
        // Проверка секретного кода сгенерированного приложением и кода присланного сервисом 
        // внешней аутентификации на раверство для предотвращения межсайтовых атак
        if (secretState.equals(state)) {
            
            // Проверка отсутствия ошибок во время внешней аутентификации
            if (error == null) {
                
                final OAuth2AccessToken accessToken = service.getAccessToken(code);
                final OAuthRequest request = new OAuthRequest(Verb.GET, userInfoUri, service);
                service.signRequest(accessToken, request); 
                final Response response = request.send();

                log.info(accessToken.toString());
                log.info(response.toString());
                log.info("CallbackData" + response.getBody());

                // Текущий пользователь
                User user = parseData(response.getBody()); 

                // Слияние и обновление данных пользователя в БД и из системы внешней аутентификации
                DBResultMerge mergeResult = (DBResultMerge)manageUser.merge(user);
                
                // Проверка слияния данных
                if (mergeResult.getId() > 0 ) {
                    // ID пользователя
                    user.setId(mergeResult.getId());
                    // ID группировки пользователя
                    user.setGroupId(mergeResult.getGroupId());
                    // Старт сессии пользоваетя для работы в системе
                    manageUser.startSession(user);
                    // Сообщение об успешном входе в систему
                    model.addAttribute("message", cache.get(2).getText());
                    urlAuth = "user.auth.success";
                } else {
                    // Сообщение об ошибке слияния данных
                    model.addAttribute("message", mergeResult.getText());
                    urlAuth = "user.auth.failure";
                }
                
            } else {
                // Сообщение об ошибке аутентификации
                model.addAttribute("message", String.format(cache.get(-6).getText(), error));
                urlAuth = "user.auth.failure";
            }
            
        } else {
            log.error("secretState=" + secretState + " <> " + state);
            // Сообщение об отказе в доступе
            model.addAttribute("message", cache.get(-7).getText());
            urlAuth = "user.auth.failure";
        }
        
        return urlAuth;
    }
    
    /**
     * Выполняет преобразование json в user
     * @param data Данные пользователя в формате json
     * @return Пользователь системы
     */
    abstract User parseData(String data) throws IOException;
}
