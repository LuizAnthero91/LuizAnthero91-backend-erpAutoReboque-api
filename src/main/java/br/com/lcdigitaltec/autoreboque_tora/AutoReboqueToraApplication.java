package br.com.lcdigitaltec.autoreboque_tora;

import br.com.lcdigitaltec.autoreboque_tora.infra.storage.R2Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(R2Properties.class)
public class AutoReboqueToraApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				AutoReboqueToraApplication.class,
				args
		);
	}
}