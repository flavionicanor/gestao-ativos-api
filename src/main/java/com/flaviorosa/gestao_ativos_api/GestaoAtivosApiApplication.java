package com.flaviorosa.gestao_ativos_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GestaoAtivosApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(GestaoAtivosApiApplication.class, args);
	}

}
