package com.pfirmino.cursomc.config;

import java.util.Arrays;

import com.pfirmino.cursomc.security.JWTAuthenticationFilter;
import com.pfirmino.cursomc.security.JWTAuthorizationFilter;
import com.pfirmino.cursomc.security.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    //Vetor com endpoints autorizados
    private static final String[] PUBLIC_MATCHERS = {
        "/h2-console/**"
    };

    //Vetor com endpoints autorizados
    private static final String[] PUBLIC_MATCHERS_GET = {
        "/categorias/**",
        "/produtos/**"
    };

    //Vetor com endpoints autorizados
    private static final String[] PUBLIC_MATCHERS_POST = {
        "/clientes/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        //Se o profile for Test habilitar acesso ao H2
        if(Arrays.asList(env.getActiveProfiles()).contains("test")){
            http.headers().frameOptions().disable();
        }
        
        //http.cors() --> Aplica as configurações do Bean CorsConfigurationSource
        //.and().csrf().disable(); --> Desabilita a proteção CSRF para ataques que utilizam dados de sessão
        http.cors().and().csrf().disable();

        //.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() --> Autoriza apenas a requisição GET dos Endpoints no vetor
        //.antMatchers(PUBLIC_MATCHERS).permitAll() --> Autoriza os Endpoints do vetor
        //.anyRequest().authenticated(); --> Para todos os outros exige autenticação
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
            .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_POST).permitAll()
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .anyRequest().authenticated();
        
        //Adicionar o filtro de Autenticação
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));

        //Adicionar o filtro de Autorizacao
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

        //Configuração para garantir que a aplicação não crie sessão de usuário
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); 
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        //Configuração para Habilitar multiplas fontes
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }  
}
