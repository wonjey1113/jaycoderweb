package com.jaycoder.web.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jaycoder.web.config.auth.PrincipalDetailService;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근하면 권한체크를 미리한다.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;	
	
	@Autowired
	private PrincipalDetailService principalDetailService;
		
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}

	  // 시뮤리티가 대신 로그인해주는데 password를 가로채기를 하는데
		// 해당 password가 뭘로 해쉬가 되어 회원가입이 되어있는지 알아야
		// 같은 해쉬로 암호해해서 DB에 있는 해쉬와 비교를 할수 있음.	 
	 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 	auth.userDetailsService(principalDetailService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
			http
					.authorizeRequests()
							.antMatchers("/","/account/register","/css/**","/api/**").permitAll()
							.anyRequest().authenticated()
							.and()
					.formLogin()
							.loginPage("/account/login")
							.loginProcessingUrl("/account/login") /* 스프링 시뮤리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인 해준다. */
							.defaultSuccessUrl("/")
							.permitAll()
							.and()
					.logout()
							.permitAll();
		}
	
//		@Autowired
//		public void configureGlobal(AuthenticationManagerBuilder auth) 
//		  throws Exception {
//		    auth.jdbcAuthentication()
//		      .dataSource(dataSource)
//		      .passwordEncoder(passwordEncoder())
//		      .usersByUsernameQuery("select username, password, enabled "  // 유저 정보
//		        + "from user "
//		        + "where username = ?")
//		      .authoritiesByUsernameQuery("select u.username,r.name "     // 권한 정보
//		        + "from user_role ur inner join user u on  ur.user_id =  u.id "
//		      	+ "inner join role r on ur.role_id = r.id "	
//		        + "where u.username = ?");
//		}	
		
		@Bean
		public PasswordEncoder passwordEncoder() {
		    return new BCryptPasswordEncoder();
		}
		
	
		
}
