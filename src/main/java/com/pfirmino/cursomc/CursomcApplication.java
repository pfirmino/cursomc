package com.pfirmino.cursomc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CursomcApplication {
	/*
		Para alterar a porta, adicione a chave abaixo no arquivo application.properties

		server.port=${port:8081}
	
	*/
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

}
