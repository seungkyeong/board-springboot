package configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import constant.AppConstant;

/* Cors 설정 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        	.allowedOrigins(AppConstant.HttpConfig.ORIGIN_URL) 			// 허용 출처
            .allowedMethods(
            		AppConstant.HttpConfig.HTTP_GET + "," +
            		AppConstant.HttpConfig.HTTP_POST + "," +
            		AppConstant.HttpConfig.HTTP_OPTIONS) 				// 허용 HTTP method
            .allowedHeaders(AppConstant.HttpConfig.HEADER_AUTHORIZATION)//허용 헤더 지정
            .allowCredentials(true) 									//쿠키 인증 요청 허용
            .maxAge(3000); 												//50분 동안 pre-flight 캐싱
    }
}