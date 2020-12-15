package com.pfirmino.cursomc.services;

import java.util.Date;
import java.util.Optional;

import com.pfirmino.cursomc.domain.Cliente;
import com.pfirmino.cursomc.domain.ItemPedido;
import com.pfirmino.cursomc.domain.PagamentoComBoleto;
import com.pfirmino.cursomc.domain.Pedido;
import com.pfirmino.cursomc.domain.enums.EstadoPagamento;
import com.pfirmino.cursomc.repositories.ItemPedidoRepository;
import com.pfirmino.cursomc.repositories.PagamentoRepository;
import com.pfirmino.cursomc.repositories.PedidoRepository;
import com.pfirmino.cursomc.security.UserSS;
import com.pfirmino.cursomc.services.exceptions.AuthorizationException;
import com.pfirmino.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository repo;
    @Autowired
    private BoletoService boletoService;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private EmailService emailService;

    public Pedido find(Integer id){
        Optional<Pedido> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
         "Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));        
    }

    @Transactional
    public Pedido insert(Pedido obj){
        obj.setId(null);
        obj.setInstante(new Date());
        obj.setCliente(clienteService.find(obj.getCliente().getId()));
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if(obj.getPagamento() instanceof PagamentoComBoleto){
            PagamentoComBoleto pgto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pgto, obj.getInstante());
        }
        obj = repo.save(obj);
        pagamentoRepository.save(obj.getPagamento());

        for(ItemPedido ip: obj.getItens()){
            ip.setDesconto(0.0);
            ip.setProduto(produtoService.find(ip.getProduto().getId()));
            ip.setPreco(ip.getProduto().getPreco());
            ip.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());

        emailService.sendOrderConfirmationHtmlEmail(obj);

        return obj;
    }

    public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderedBy, String direction) {
        UserSS user = UserService.authenticated();

        if(user == null){
            throw new AuthorizationException("Acesso Negado.");
        }

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderedBy);
        Cliente cliente = clienteService.find(user.getId());
        return repo.findByCliente(cliente, pageRequest);
    }
}
