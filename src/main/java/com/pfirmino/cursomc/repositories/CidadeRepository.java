package com.pfirmino.cursomc.repositories;

import com.pfirmino.cursomc.domain.Cidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer>{
    
}
