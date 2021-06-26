package com.feira.cruzeiro.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {
	
	@Autowired
	private DataSource dataSource;
	
	private static final String[] AUTH_LIST = {
	        "/",
	        "/feiracruzeiro",
	        "/lojas/{id}",
	        "/lojas",
	        "/servicos",
	        "/restaurantes",
	        "/eventos",
	        "/buscalojas",
	        "/eventos/{id}"
	        };
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
        .antMatchers(AUTH_LIST).permitAll()
        .anyRequest().authenticated()
        .and().formLogin(form -> form 
        		.loginPage("/login")
        		.defaultSuccessUrl("/lojaform", true)
        		.permitAll())
        .logout(logout -> logout.logoutUrl("/logout"))
        .csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(encoder);
	}
	
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**", "/css/**", "/img/**", "/js/**");
        web.ignoring().antMatchers("/bootstrap/**", "/vendor/**");
        web.ignoring().antMatchers("/fotos-lojas/**", "/fotos-eventos/**");
    }
}
