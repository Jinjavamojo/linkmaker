/*
 * =============================================================================
 *
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package thymeleafexamples.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;
import thymeleafexamples.springsecurity.config.EncodingFilter;
import thymeleafexamples.springsecurity.service.UserService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SpringSecurityConfig() {
        super();
    }


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http
                .authorizeRequests()
                .antMatchers("/pay/**").hasAnyRole("ANONYMOUS","EMPLOYEE","ADMIN")
                .antMatchers("/error.html").anonymous()
                .antMatchers("/index.html").hasAnyRole("EMPLOYEE","ADMIN")
                .antMatchers("/project/**").hasAnyRole("EMPLOYEE","ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/shared/**").hasAnyRole("EMPLOYEE","ADMIN")

            .and()
                .formLogin()
                .loginPage("/login.html")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/login-error.html")
                .permitAll()
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login.html")

            .and()
                .exceptionHandling()
                .accessDeniedPage("/403.html");
                //.and()
                //.addFilterBefore(new LinkHandleFilter(),BasicAuthenticationFilter.class);
        http.addFilterBefore(filter, CsrfFilter.class);


    }


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("jim").password("{noop}demo").roles("ADMIN").and()
//                .withUser("bob").password("{noop}demo").roles("USER").and()
//                .withUser("ted").password("{noop}demo").roles("USER","ADMIN");
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationProvider bean definition
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }


}
