package ies.tracktails.animalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
	"ies.tracktails.animalservice",
	"ies.tracktails.animalsDataCore"
})
@ComponentScan(basePackages = {
	"ies.tracktails.animalservice",
	"ies.tracktails.animalsDataCore"
})
@EntityScan(basePackages = {
	"ies.tracktails.animalsDataCore.entities",
	"ies.tracktails.animalservice.entities"
})
@EnableJpaRepositories(basePackages = {
	"ies.tracktails.animalsDataCore.repositories",
	"ies.tracktails.animalservice.repositories"
})
public class AnimalserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimalserviceApplication.class, args);
	}

}
