package dev.mlukas.langreader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final String prodServerUrl;
    private final String devServerUrl;

    public WebSecurityConfiguration(
            @Value("${langreader.app.prod.server}") String prodServerUrl,
            @Value("${langreader.app.dev.server}") String devServerUrl
    ) {
        super();
        this.prodServerUrl = prodServerUrl;
        this.devServerUrl = devServerUrl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();

//        http.authorizeRequests()
//                .antMatchers(
//                        "/api/auth/**",
//                        "/api/langs/all",
//                        "/api/home").permitAll()
//                .antMatchers(
//                        "/api/words/**",
//                        "/api/texts/**",
//                        "/api/langs/**",
//                        "/api/ext/**",
//                        "/api/translate/**",
//                        "/api/users/**", // restricted in the controller itself
//                        "/api/stats/**").authenticated()
//                .anyRequest().permitAll();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add(prodServerUrl);
        if (!devServerUrl.isBlank()) {
            allowedOrigins.add(devServerUrl);
        }
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}