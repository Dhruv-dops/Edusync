package configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AccessControlConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	    http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.requestMatchers(
	                "/",
	                "/index",
	                "/register",
	                "/regForm",
	                "/login",
	                "/images/**")
	        .permitAll()

	        .requestMatchers("/admin/**")
	        .hasRole("ADMIN")

	        .requestMatchers(
	                "/profile",
	                "/enrollment",
	                "/certificate/**",
	                "/eduAi")
	        .hasRole("STUDENT")

	        .anyRequest()
	        .authenticated()
	    )

	    .formLogin(form -> form

	        .loginPage("/login")

	        .loginProcessingUrl("/login")
	        .failureUrl("/login?error=true")
	        .successHandler(
	            (request,response,authentication)->{

	                boolean isAdmin =
	                        authentication
	                        .getAuthorities()
	                        .stream()
	                        .anyMatch(a ->
	                            a.getAuthority()
	                             .equals("ROLE_ADMIN"));

	                if(isAdmin){

	                    response.sendRedirect(
	                            "/admin/dashboard");

	                }else{

	                    response.sendRedirect(
	                            "/index");
	                }

	            })

	        .permitAll())

	    .logout(logout -> logout

	        .logoutSuccessUrl("/login")
	        .permitAll());

	    return http.build();
	}

}
