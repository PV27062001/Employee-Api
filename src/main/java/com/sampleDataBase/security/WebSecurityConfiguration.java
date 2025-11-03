package com.sampleDataBase.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
//        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService customUserNamePassword(){
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("praveen")
                .password("praveen")
                .roles("ADMIN")
                .build();
        UserDetails userDetails2 = User.withDefaultPasswordEncoder()
                .username("ravi")
                .password("praveen")
                .roles("ADMIN")
                .build();
        UserDetails userDetails3 = User.withDefaultPasswordEncoder()
                .username("ram")
                .password("praveen")
                .roles("ADMIN")
                .build();
        UserDetails userDetails4 = User.withDefaultPasswordEncoder()
                .username("lakshman")
                .password("praveen")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(userDetails,userDetails2,userDetails3,userDetails4);
    }
}
