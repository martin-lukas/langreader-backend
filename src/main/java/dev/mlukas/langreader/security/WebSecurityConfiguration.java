package dev.mlukas.langreader.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private RestBasicAuthenticationEntryPoint jsonAuthenticationEntryPoint;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${langreader.app.prod.server}")
    private String prodServerUrl;
    @Value("${langreader.app.dev.server}")
    private String devServerUrl;

    @Bean
    public DaoAuthenticationProvider customAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/auth/signup",
                        "/langs/all" // for either sign-up page languages or language management
                ).permitAll()
                .antMatchers(
                        "/auth/login",
                        "/users/**",
                        "/texts/**",
                        "/langs",
                        "/langs/native",
                        "/langs/chosen",
                        "/words/**",
                        "/translate/**",
                        "/ext/**",
                        "/stats/**"
                ).authenticated();

        // Setup CORS
        http.cors().and().csrf().disable();
        // TODO: Setup CSRF properly
        // http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // Setup session management to stateless
        // TODO: Move back to stateful BE and session cookie, for logouts?
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Setup HTTP Basic Authentication
        http.httpBasic()
                .authenticationEntryPoint(jsonAuthenticationEntryPoint).and()
                .authenticationProvider(customAuthProvider());
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
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of(
                "Accept",
                "Access-Control-Request-Headers",
                "Access-Control-Request-Method",
                "Authorization",
                "Content-Type",
                "Origin",
                "X-Requested-With",
                "X-XSRF-TOKEN"
        ));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}