package configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import util.JwtRequestFilter;

@Configuration
public class JwtFilterConfiguration {
    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtFilter() {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtRequestFilter());
        registrationBean.addUrlPatterns("/api/*"); // JWT 필터를 적용할 URL 패턴
        registrationBean.setOrder(1);  // 필터 순서
        return registrationBean;
    }
}
