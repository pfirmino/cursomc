package com.pfirmino.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfirmino.cursomc.dto.CredenciaisDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                    JWTUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
            HttpServletResponse res) throws AuthenticationException{
        try{
            //Instanciar Objeto a partir dos dados que vieram na requisição
            CredenciaisDTO creds = new ObjectMapper()
                .readValue(req.getInputStream(), CredenciaisDTO.class);
            
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
            

            //Método authenticate verifica se o usuário e senha são válidos
            Authentication auth = authenticationManager.authenticate(authToken);
            return auth;
        }
        catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    public void successfulAuthentication(HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException{

        //getPrincipal retorna o usuario do spring security, a partir disso é feito um casting
        //para a classe UserSS e a partir disso pegamos o email (getUsername)
        String username = ((UserSS) auth.getPrincipal()).getUsername();

        //Geramos o Token
        String token = jwtUtil.generateToken(username);

        //Adicionamos o token ao cabeçalho
        res.addHeader("Authorization", "Bearer " + token);
    }
}
