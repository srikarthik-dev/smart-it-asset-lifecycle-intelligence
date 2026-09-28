package com.portfolio.asset.repository;
import com.portfolio.asset.domain.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {
}
