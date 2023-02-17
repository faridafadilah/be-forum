package com.forum.server.server.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import com.forum.server.server.interceptor.AdminInterceptor;
// import com.forum.server.server.interceptor.LogoutInterceptor;
// import com.forum.server.server.interceptor.SuperAdminInterceptor;
// import com.forum.server.server.interceptor.UserInterceptor;
import com.forum.server.server.security.jwt.AuthEntryPointJwt;
import com.forum.server.server.security.jwt.AuthTokenFilter;
import com.forum.server.server.security.services.UserDetailsServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig implements WebMvcConfigurer {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/imageMain/**")
        .addResourceLocations("file:imageMain\\");
    registry.addResourceHandler("/imageSub/**")
        .addResourceLocations("file:imageSub\\");
    registry.addResourceHandler("/imageThread/**")
        .addResourceLocations("file:imageThread\\");
    registry.addResourceHandler("/imageUser/**")
        .addResourceLocations("file:imageUser\\");
  }

  // @Autowired
  // private UserInterceptor userInterceptor;

  // @Autowired
  // private AdminInterceptor adminInterceptor;

  // @Autowired
  // private LogoutInterceptor logoutInterceptor;

  // @Autowired
  // private SuperAdminInterceptor superAdminInterceptor;

  @Value("${cors.allowed-origins}")
  private String allowedOrigins;

  @Bean
  public AuthTokenFilter authenticationJwTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests().antMatchers("/api/auth/**").permitAll().and()
        .authorizeRequests().antMatchers("/imageMain/**").permitAll().and()
        .authorizeRequests().antMatchers("/imageSub/**").permitAll().and()
        .authorizeRequests().antMatchers("/imageThread/**").permitAll().and()
        .authorizeRequests().antMatchers("/imageUser/**").permitAll().and()
        .authorizeRequests().antMatchers("/api/main").permitAll().and()
        .authorizeRequests().antMatchers("/api/main/**").permitAll().and()
        .authorizeRequests().antMatchers("/api/sub/**").permitAll().and()
        .authorizeRequests().antMatchers("/api/thread/**").permitAll().and()
        .authorizeRequests().antMatchers("/api/comment/**").permitAll().and()
        .authorizeRequests().antMatchers("/api/sub").permitAll().and()
        .authorizeRequests().antMatchers("/api/thread").permitAll().and()
        .authorizeRequests().antMatchers("/api/comment").permitAll().and()
        .authorizeRequests().antMatchers("/api/userRole/**").permitAll().and()
        .authorizeRequests().antMatchers("/api/userRole/user/**").permitAll().and()
        .authorizeRequests().antMatchers("/api/profile/**").permitAll()
        .antMatchers("/api/test/**").permitAll()
        .anyRequest().authenticated();

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  // @Bean
  // public CorsConfigurationSource corsConfigurationSource() {
  // CorsConfiguration configuration = new CorsConfiguration();
  // configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
  // configuration.addAllowedMethod("*");
  // UrlBasedCorsConfigurationSource source = new
  // UrlBasedCorsConfigurationSource();
  // source.registerCorsConfiguration("/**", configuration);
  // return source;
  // }

  // @Override
  // public void addInterceptors(InterceptorRegistry registry) {
  // WebMvcConfigurer.super.addInterceptors(registry);
  // registry.addInterceptor(userInterceptor).addPathPatterns(
  // "/api/comment",
  // "/api/thread/**",
  // "/api/comment/create",
  // "/api/comment/**"
  // );
  // registry.addInterceptor(adminInterceptor).addPathPatterns(
  // "/api/main/create",
  // "/api/main/**",
  // "/api/sub/create",
  // "/api/sub/**",
  // "/api/thread/create",
  // "/api/thread/**",
  // "/api/comment/create",
  // "/api/comment/**"
  // );
  // registry.addInterceptor(superAdminInterceptor).addPathPatterns(
  // "/api/main/create",
  // "/api/main/**",
  // "/api/sub/create",
  // "/api/sub/**",
  // "/api/thread/create",
  // "/api/thread/**",
  // "/api/comment/create",
  // "/api/comment/**"
  // );
  // registry.addInterceptor(logoutInterceptor).addPathPatterns(
  // "/api/auth/signout"
  // );
  // }
}