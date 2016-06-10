package org.project.openbaton.nubomedia.paas.api.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lto on 25/05/16.
 */
@Configuration
//@EnableWebMvc
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthorizeInterceptor interceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns("/oauth/token");
    }

}
