package com.pfirmino.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    //Vetor com endpoints autorizados
    private static final String[] PUBLIC_MATCHERS = {
        "/h2-console/**"
    };

    //Vetor com endpoints autorizados
    private static final String[] PUBLIC_MATCHERS_GET = {
        "/categorias/**",
        "/produtos/**"
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
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .anyRequest().authenticated();
        
        //Configuração para garantir que a aplicação não crie sessão de usuário
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); 
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        //Configuração para Habilitar multiplas fontes
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
