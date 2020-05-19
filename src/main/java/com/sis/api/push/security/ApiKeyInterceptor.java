package com.sis.api.push.security;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sis.api.push.model.AppInfoVO;
import com.sis.api.push.service.APIService;

@Configuration
public class ApiKeyInterceptor extends HandlerInterceptorAdapter
{
    
    @Autowired
    APIService mService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        
        if (handler instanceof HandlerMethod)
        {
            
            HandlerMethod hm = (HandlerMethod) handler;
            
            if (hm.hasMethodAnnotation(KeyRequired.class))
            {
                
                String authPair = request.getHeader("Authorization");
                if (authPair == null)
                    throw new AuthenticationException(request.getRequestURI());
                Map<String, String> authParams = buildParam(authPair);
                String apiKey = authParams.get("key");
                String appid = request.getHeader("appid");
                if (!validateKey(apiKey, appid))
                    throw new AuthenticationException(request.getRequestURI());
                
            }
            
        }
        
        return super.preHandle(request, response, handler);
    }
    
    private boolean validateKey(String apiKey, String appid)
    {
        
        if (appid == null || appid.equals(""))
            return false;
        
        AppInfoVO vo = mService.apiKey(appid);
        
        if (!apiKey.equals(vo.getTOKEN()))
            return false;
        
        return true;
    }
    
    private Map<String, String> buildParam(String source)
    {
        Map<String, String> result = Arrays.stream(source.split(",")).map(s -> s.split("=")).collect(Collectors.toMap(a -> a[0], //key
                                                                                                                      a -> a[1] //value
        ));
        return result;
    }
    
}
