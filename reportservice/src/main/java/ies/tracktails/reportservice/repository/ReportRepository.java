package ies.tracktails.reportservice.repository;

import ies.tracktails.reportservice.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}

