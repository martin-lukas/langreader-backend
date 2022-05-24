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
        // Use CORS configuration defined in the bean below.
        http.cors().and().csrf().disable();

        http.authorizeRequests()
                .antMatchers(
                        "/api/auth/login",
                        "/api/auth/signup",
                        "/api/langs/all" // for either sign-up page languages or language management
                ).permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers(
                        "/api/auth/logout",
                        "/api/users/**",
                        "/api/texts/**",
                        "/api/langs/**",
                        "/api/words/**",
                        "/api/translate/**",
                        "/api/ext/**",
                        "/api/stats/**"
                ).hasRole("USER");
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
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}