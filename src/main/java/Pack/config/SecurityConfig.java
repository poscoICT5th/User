package Pack.config;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import Pack.service.UserService;

@Configuration
@EnableWebSecurity        //spring security 를 적용한다는 Annotation
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    
	private final JwtTokenProvider jwtTokenProvider;
    
    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    	.httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
    	
    	.csrf()
    	.disable() // csrf 보안 토큰 disable처리.
//    	.ignoringAntMatchers("/logout")
//    	.and()
    	.sessionManagement()
    	.maximumSessions(1)
    	.expiredUrl("/failed")
    	.maxSessionsPreventsLogin(false);

    	http
    	// JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    	.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
    			UsernamePasswordAuthenticationFilter.class)
//				.ignoringAntMatchers("/login")
//		    	.ignoringAntMatchers("/signup")
    	
//		    	
//		        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
    	.authorizeRequests() // 요청에 대한 사용권한 체크
    	.antMatchers("/admin/**").hasRole("ADMIN")
    	.antMatchers("/user/**").hasRole("USER")
    	.anyRequest().permitAll(); // 그외 나머지 요청은 누구나 접근 가능
//    	.and().cors();
    	
    	http
    	.logout()
    	.logoutUrl("/logout")
    	.invalidateHttpSession(true)
    	.logoutSuccessUrl("/successLogout")
    	.deleteCookies("JSESSIONID")
    	.clearAuthentication(true)
    	.permitAll();
    	
    	http.cors().configurationSource(corsConfigurationSource());
		        
//            .formLogin()
//                .loginPage("/login")	// 로그인 페이지
//                .loginProcessingUrl("/login_proc")	// 구현한 로그인 페이지
//                .defaultSuccessUrl("/user_access")	// 로그인 성공 시 제공할 페이지
//                .failureUrl("/access_denied") // 인증에 실패했을 때 보여주는 화면 url, 로그인 form으로 파라미터값 error=true로 보낸다.	로그인 실패 시 제공할 페이지
//                .and()
//            .csrf().disable();		//로그인 창.	사이트 간 요청 위조(Cross-Site Request Forgery) 공격 방지 기능 키기
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }
     
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
    	return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
