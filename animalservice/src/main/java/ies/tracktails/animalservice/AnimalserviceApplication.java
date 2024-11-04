package ies.tracktails.animalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "ies.tracktails.animalservice",
    "ies.tracktails.animalsDataCore",
	"ies.tracktails.animalsDataCore.services",
	"ies.tracktails.animalsDataCore.configurations",
})
@EnableJpaRepositories(basePackages = "ies.tracktails.animalsDataCore.repositories")
@EntityScan(basePackages = "ies.tracktails.animalsDataCore.entities")
public class AnimalserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimalserviceApplication.class, args);
	}

}
