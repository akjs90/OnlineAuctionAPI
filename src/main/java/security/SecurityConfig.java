package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses=CustomUserDetailsService.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CustomAuthenticationSuccessHandler handler;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		     .antMatchers("/welcome").authenticated()
		     .antMatchers("/admin/**","/auction/**").hasRole("ADMIN")
		     .antMatchers("/item/**").access("hasRole('ROLE_USER')")
		     .and()
		     .formLogin().loginPage("/login").successHandler(handler)
		     .usernameParameter("username").passwordParameter("password")
		     .and()
		     .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout");
	http.exceptionHandling().accessDeniedPage("/403");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
}
