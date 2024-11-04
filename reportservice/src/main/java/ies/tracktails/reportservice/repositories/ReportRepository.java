package ies.tracktails.reportservice.repositories;

import ies.tracktails.reportservice.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByAnimalId(Long animalId);
}

