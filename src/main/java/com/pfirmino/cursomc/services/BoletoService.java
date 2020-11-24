package com.pfirmino.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import com.pfirmino.cursomc.domain.PagamentoComBoleto;

import org.springframework.stereotype.Service;

@Service
public class BoletoService {
    public void preencherPagamentoComBoleto(PagamentoComBoleto pgto, Date instantePedido){
        Calendar cal = Calendar.getInstance();
        cal.setTime(instantePedido);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        pgto.setDataVencimento(cal.getTime());
    }
}
