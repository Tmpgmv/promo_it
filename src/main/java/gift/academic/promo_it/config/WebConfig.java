package gift.academic.promo_it.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class WebConfig {
    /*
    * Для работы Swagger требуется выключить
    * механизм CORS.
    * Выключим его для всех сред,
    * кроме продуктивной.
    * Для этого ориентируемся на свойство debug.
    * */
    @Value("${spring.application.debug:false}")
    private boolean debug;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        if (debug) {
            return request -> null;  // CORS OFF
        }
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        return request -> config;
    }
}