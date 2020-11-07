package com.pfirmino.cursomc.services;

import java.util.Optional;

import com.pfirmino.cursomc.domain.Cliente;
import com.pfirmino.cursomc.repositories.ClienteRepository;
import com.pfirmino.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository repo;

    public Cliente buscar(Integer id){
        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
         "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));        
    }
}
