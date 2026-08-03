package snapLink.Url.Config.Security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    public UserDetailsService userDetailsService;


    /**
     * BCrypt is a one-way, salted hashing algorithm designed to be
     * deliberately slow (defeats brute force). NEVER use plain
     * MD5/SHA256 for passwords — they're too fast, and unsalted.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    /**
     * THE FILTER CHAIN — this defines the actual security rules.
     *
     * Every incoming HTTP request passes through a chain of filters.
     * Spring Security inserts many filters automatically (CSRF, CORS,
     * session management, exception handling, etc). We:
     *   1. Disable CSRF (not needed for stateless token-based APIs;
     *      CSRF protects cookie-based session auth, which we don't use)
     *   2. Set session policy to STATELESS (no HttpSession is created
     *      or used — every request must carry its own JWT)
     *   3. Declare which URLs need which roles
     *   4. Plug in our custom JwtAuthFilter BEFORE Spring's own
     *      UsernamePasswordAuthenticationFilter, so that by the time
     *      Spring gets to checking "is this user authenticated?",
     *      our filter has already populated the SecurityContext from
     *      the JWT.
     */


  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth.requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();

    }


    /**
     * AuthenticationManager is what actually performs authentication
     * (checks username + password). We expose it as a bean so our
     * AuthController can call authenticationManager.authenticate(...)
     * during login.
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider prov = new DaoAuthenticationProvider(customUserDetailsService);
        prov.setPasswordEncoder(passwordEncoder);
        return prov;
    }

  

}

