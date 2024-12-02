package ies.tracktails.reportservice.repositories;

import ies.tracktails.reportservice.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByAnimalId(Long animalId);
}

