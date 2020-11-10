package com.pfirmino.cursomc.services;

import java.util.Arrays;
import java.util.Optional;

import com.pfirmino.cursomc.domain.Categoria;
import com.pfirmino.cursomc.repositories.CategoriaRepository;
import com.pfirmino.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repo;

    public Categoria buscar(Integer id){
        Optional<Categoria> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
         "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));        
    }

    public Categoria inserir(Categoria obj){
        obj.setId(null);
        return repo.save(obj);
    }
}
