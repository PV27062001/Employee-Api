package com.sampleDataBase.security;


import com.sampleDataBase.auth.UserDetailServiceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final UserDetailServiceProvider userDetailServiceProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
//        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService customUserNamePassword(){
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("praveen")
//                .password("praveen")
//                .roles("ADMIN")
//                .build();
//        UserDetails userDetails2 = User.withDefaultPasswordEncoder()
//                .username("ravi")
//                .password("praveen")
//                .roles("USER")
//                .build();
//        UserDetails userDetails3 = User.withDefaultPasswordEncoder()
//                .username("ram")
//                .password("praveen")
//                .roles("ADMIN")
//                .build();
//        UserDetails userDetails4 = User.withDefaultPasswordEncoder()
//                .username("lakshman")
//                .password("praveen")
//                .roles("ADMIN")
//                .build();
//        /// by default the passwords are encrypted and decrypted while using the withDefaultPasswordEncoder()
//        System.out.println("password: "+userDetails2.getPassword());
//        return new InMemoryUserDetailsManager(userDetails,userDetails2,userDetails3,userDetails4);
//    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailServiceProvider);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder(10);
    }

}
