package com.pfirmino.cursomc.services;

import java.util.Random;

import com.pfirmino.cursomc.domain.Cliente;
import com.pfirmino.cursomc.repositories.ClienteRepository;
import com.pfirmino.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    public void sendNewPassword(String email){
        Cliente cliente = clienteRepository.findByEmail(email);
        if(cliente == null){
            throw new ObjectNotFoundException("Email não encontrado");
        }
        String newPassword = newPassword();
        cliente.setSenha(passwordEncoder.encode(newPassword));
        clienteRepository.save(cliente);
        emailService.sendNewPasswordEmail(cliente, newPassword);
    }

    private String newPassword() {
        char[] vet = new char[10];
        for(int i=0; i<10; i++){
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private char randomChar() {
        int opt = random.nextInt(3);
        if(opt == 0){//Gera um digito
            return (char) (random.nextInt(10) + 48);
        }
        else if(opt == 1){//Gera letra maiuscula
            return (char) (random.nextInt(26) + 65);
        }
        else { // Gera letra minuscula
            return (char) (random.nextInt(26) + 97);
        }
    }
}
