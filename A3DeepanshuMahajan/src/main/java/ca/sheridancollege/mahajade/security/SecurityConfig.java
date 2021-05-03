package ca.sheridancollege.mahajade.security;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LoggingAccessDeniedHandler accessDeniedHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() {
		JdbcUserDetailsManager jdbc = new JdbcUserDetailsManager();
		jdbc.setDataSource(dataSource);
		ArrayList<GrantedAuthority> employee = new ArrayList<GrantedAuthority>();
		employee.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

		ArrayList<GrantedAuthority> user = new ArrayList<GrantedAuthority>();
		user.add(new SimpleGrantedAuthority("ROLE_USER"));

		User u1 = new User("employee1", passwordEncoder().encode("pass"), employee);
		User u2 = new User("user1", passwordEncoder().encode("pass"), user);
		User u3 = new User("employee2", passwordEncoder().encode("pass"), employee);
		User u4 = new User("employee3", passwordEncoder().encode("pass"), employee);
		
		jdbc.createUser(u1);
		jdbc.createUser(u2);
		jdbc.createUser(u3);
		jdbc.createUser(u4);

		return jdbc;
	}
	
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/h2-console/**").and().ignoring().antMatchers("/css/**");
	}

	@Override
	public void configure(HttpSecurity s) throws Exception {
		s.requiresChannel().anyRequest().requiresSecure().and()
		.authorizeRequests().antMatchers("/").permitAll()
		.and().authorizeRequests().antMatchers("/addEmployee").permitAll()
		.and().authorizeRequests().antMatchers("/login").permitAll()
		.and().authorizeRequests().antMatchers("/home").hasRole("EMPLOYEE")
		.and().authorizeRequests().antMatchers("/addPost").hasRole("EMPLOYEE")
		.anyRequest().authenticated()
		.and().formLogin().loginPage("/login").permitAll()
		.and().logout().clearAuthentication(true).invalidateHttpSession(true)
		.logoutSuccessUrl("/login?logout").permitAll()
		.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}

}
