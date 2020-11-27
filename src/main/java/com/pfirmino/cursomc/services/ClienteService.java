package com.pfirmino.cursomc.services;

import java.util.List;
import java.util.Optional;

import com.pfirmino.cursomc.domain.Cidade;
import com.pfirmino.cursomc.domain.Cliente;
import com.pfirmino.cursomc.domain.Endereco;
import com.pfirmino.cursomc.domain.enums.TipoCliente;
import com.pfirmino.cursomc.dto.ClienteDTO;
import com.pfirmino.cursomc.dto.ClienteNewDTO;
import com.pfirmino.cursomc.repositories.ClienteRepository;
import com.pfirmino.cursomc.repositories.EnderecoRepository;
import com.pfirmino.cursomc.services.exceptions.DataIntegrityException;
import com.pfirmino.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository repo;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private BCryptPasswordEncoder pwdEncoder;

    public Cliente find(Integer id) {
        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }

    @Transactional
    public Cliente insert(Cliente obj){
        obj.setId(null);
        obj = repo.save(obj);
        enderecoRepository.saveAll(obj.getEndereços());
        return obj;
    }

    public Cliente update(Cliente obj) {
        Cliente newObj = find(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
        }
    }

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderedBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderedBy);
        return repo.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO objDTO) {
        return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
    }

    public Cliente fromDTO(ClienteNewDTO objDTO) {
        Cliente cli = new Cliente(
                null, 
                objDTO.getNome(), 
                objDTO.getEmail(), 
                objDTO.getCpfOuCnpj(), 
                TipoCliente.toEnum(objDTO.getTipo()),
                pwdEncoder.encode(objDTO.getSenha()));

        Cidade cid = new Cidade(objDTO.getCidadeID(), null, null);

        Endereco end = new Endereco(
                null, 
                objDTO.getLogradouro(), 
                objDTO.getNumero(), 
                objDTO.getComplemento(), 
                objDTO.getBairro(),
                objDTO.getCep(), 
                cli,
                cid);

        cli.getEndereços().add( end );
        cli.getTelefones().add( objDTO.getTelefone1());
        if( objDTO.getTelefone2() != null ){
            cli.getTelefones().add( objDTO.getTelefone2());
        }
        if( objDTO.getTelefone3() != null ){
            cli.getTelefones().add( objDTO.getTelefone3());
        }

        return cli;
    }

    private void updateData(Cliente newObj, Cliente obj){
       newObj.setNome(obj.getNome());
       newObj.setEmail(obj.getEmail());
    }
}
