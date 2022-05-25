package dev.mlukas.langreader.security;

import dev.mlukas.langreader.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private JsonBasicAuthenticationEntryPoint jsonAuthenticationEntryPoint;
    @Autowired
    private UserService userService;
    @Value("${langreader.app.prod.server}")
    private String prodServerUrl;
    @Value("${langreader.app.dev.server}")
    private String devServerUrl;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
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
                .antMatchers(
                        "/api/users/**",
                        "/api/texts/**",
                        "/api/langs/**",
                        "/api/words/**",
                        "/api/translate/**",
                        "/api/ext/**",
                        "/api/stats/**"
                ).authenticated();

        http.httpBasic().authenticationEntryPoint(jsonAuthenticationEntryPoint);
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