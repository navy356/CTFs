package org.shatranj.diwana.shatranjserver.security;

import org.shatranj.diwana.shatranjserver.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class MyWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    AuthenticationService authenticationService;

    @Autowired
    public MyWebSecurityConfigurerAdapter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                    .antMatchers("/api/register")
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/api/players")
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                .httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authentication)
            throws Exception
    {
        authentication.authenticationProvider(new MyAuthenticationProvider(authenticationService));
    }

    //@Bean
    //CorsConfigurationSource corsConfigurationSource() {
    //    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    //    return source;
    //}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomBCryptPasswordEncoder();
    }
}
