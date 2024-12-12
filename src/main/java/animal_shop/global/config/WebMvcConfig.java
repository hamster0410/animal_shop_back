package animal_shop.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    private final long MAX_AGE_SEC =3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 와일드카드 패턴으로 모든 도메인 허용
                .allowedMethods("*")        // 모든 HTTP 메서드 허용
                .allowedHeaders("*")        // 모든 헤더 허용
                .allowCredentials(true)     // 인증 정보 허용
                .maxAge(3600);              // 캐싱 시간 (1시간)
    }

//
//    @Override
//    public void addCorsMappings(CorsRegistry registry){
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5500")
//                .allowedOrigins("http://192.168.0.169:3000")
//                .allowedOrigins("http://192.168.0.85:3000")
//                .allowedOrigins()
//                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(MAX_AGE_SEC);
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/fancaffe/comment-image")
                .addResourceLocations("file:///C:/fancaffe/post-image");
    }

}
