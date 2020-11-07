package com.pfirmino.cursomc.repositories;

import com.pfirmino.cursomc.domain.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
    
}
