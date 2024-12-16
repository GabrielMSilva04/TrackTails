package ies.tracktails.reportservice.repositories;

import ies.tracktails.reportservice.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByAnimalId(Long animalId);
}

