package ies.tracktails.reportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ies.tracktails.reportservice", "ies.tracktails.animalsDataCore"})
@EnableJpaRepositories(basePackages = {"ies.tracktails.reportservice.repositories", "ies.tracktails.animalsDataCore.repositories"})
@EntityScan(basePackages = {"ies.tracktails.reportservice.entities", "ies.tracktails.animalsDataCore.entities"})
public class ReportserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportserviceApplication.class, args);
	}

}
