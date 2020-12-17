package com.pfirmino.cursomc.services;

import javax.mail.internet.MimeMessage;

import com.pfirmino.cursomc.domain.Cliente;
import com.pfirmino.cursomc.domain.Pedido;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    void sendOrderConfirmationEmail(Pedido obj);
    void sendEmail(SimpleMailMessage msg);
    void sendOrderConfirmationHtmlEmail(Pedido obj);
    void sendHtmlEmail(MimeMessage msg);
    void sendNewPasswordEmail(Cliente cliente, String newPassword);
}
