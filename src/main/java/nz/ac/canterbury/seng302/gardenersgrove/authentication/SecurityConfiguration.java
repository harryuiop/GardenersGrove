package nz.ac.canterbury.seng302.gardenersgrove.authentication;

import nz.ac.canterbury.seng302.gardenersgrove.authentication.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Custom Security Configuration
 * Such functionality was previously handled by WebSecurityConfigurerAdapter
 */
// don't worry if the "com.baeldung.security" comes up red in IntelliJ
@Configuration
@EnableWebSecurity
@ComponentScan("com.baeldung.security")
public class SecurityConfiguration {

    /**
     * Our Custom Authentication Provider {@link CustomAuthenticationProvider}
     */
    private final CustomAuthenticationProvider authProvider;

    /**
     * @param authProvider Our Custom Authentication Provider {@link CustomAuthenticationProvider} to be injected in
     */
    public SecurityConfiguration(CustomAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    /**
     * Create an Authentication Manager with our {@link CustomAuthenticationProvider}
     *
     * @param http http security configuration object from Spring
     * @return a new authentication manager
     * @throws Exception if the AuthenticationManager can not be built
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
                AuthenticationManagerBuilder.class
        );
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();

    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http The HttpSecurity object used to configure security for HTTP requests.
     * @return A SecurityFilterChain object representing the configured security filter chain.
     * @throws Exception if an error occurs during the configuration process.
     *                   <p>
     *                   This method configures the security filter chain for HTTP requests, specifying various security rules
     *                   for different endpoints and request patterns.
     *                   <p>
     *                   It allows access to certain static resources (e.g., CSS, images) and H2 console without authentication.
     *                   It configures Cross-Site Request Forgery (CSRF) protection, ignoring specific request patterns.
     *                   It permits access to "/" (root), "/register", "/login", and "/check-email-duplication" endpoints to anyone.
     *                   It restricts access to "/admin" endpoint to users with the "ADMIN" role.
     *                   It specifies the configuration for form-based login, including login page, processing URL, and default success URL.
     *                   It specifies the configuration for logout, including logout URL, logout success URL, invalidating session, and removing cookies.
     * @see HttpSecurity
     * @see SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] staticResources = {
                "/css/**",
                "/images/**",
                "/fonts/**",
                "/scripts/**",
                "/javascript/**",
        };

        // Configure security rules for HTTP requests
        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2/**"))
                                .permitAll()
                )
                .headers(headers ->
                        headers.frameOptions(Customizer.withDefaults()).disable()
                )
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2/**"),
                                AntPathRequestMatcher.antMatcher("/login/**"),
                                AntPathRequestMatcher.antMatcher("/register/**"),
                                AntPathRequestMatcher.antMatcher("/check-email-duplication/**"),
                                AntPathRequestMatcher.antMatcher("/logout"))

                )

                .authorizeHttpRequests(request ->
                        // Allow "/", "/register", and "/login" to anyone (permitAll)
                        request
                                .requestMatchers("/", "/register", "/login", "/check-email-duplication")
                                .permitAll()
                                // Only allow admins to reach the "/admin" page
                                .requestMatchers("/admin")
                                .hasRole("ADMIN")
                                .requestMatchers(staticResources)
                                .permitAll()
                                // Any other request requires authentication
                                .anyRequest()
                                .authenticated()
                )
                // Define logging in, a POST "/login" endpoint now exists under the hood, after login redirect to user page
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/view-all")
                                .failureHandler(new LoginAuthenticationFailureHandler())
                )
                // Define logging out, a POST "/logout" endpoint now exists under the hood, redirect to "/login", invalidate session and remove cookie
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/home")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

}

