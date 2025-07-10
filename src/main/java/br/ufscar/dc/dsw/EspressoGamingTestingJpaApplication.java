package br.ufscar.dc.dsw;

import br.ufscar.dc.dsw.config.seeders.DatabaseSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EspressoGamingTestingJpaApplication {

	@Autowired
	private DatabaseSeeder databaseSeeder;

	public static void main(String[] args) {
		SpringApplication.run(EspressoGamingTestingJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializationRunner() {
		return args -> {
			databaseSeeder.seedDatabase();
		};
	}
}