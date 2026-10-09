package cephei.dev.event_service.configuration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.RequestInterceptor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Configuration
public class FeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if(attributes == null) {
                return;
            }

            HttpServletRequest request =
                    attributes.getRequest();

            Cookie[] cookies = request.getCookies();

            if(cookies != null) {
                Arrays.stream(cookies)
                        .filter(c -> c.getName().equals("jwt"))
                        .findFirst()
                        .ifPresent(value ->
                                template.header(
                                        "Cookie",
                                        "jwt="+value.getValue())
                        );
            }
        };
    }
}
