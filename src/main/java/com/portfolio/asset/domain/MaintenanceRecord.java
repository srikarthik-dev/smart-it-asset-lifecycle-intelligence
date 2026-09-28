package com.portfolio.asset.domain;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_records")
@Data
public class MaintenanceRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
    
    private String type; // PREVENTIVE, REPAIR, HARDWARE_FAILURE, SOFTWARE, INSPECTION
    private String description;
    
    private LocalDate reportedDate;
    private LocalDate completedDate;
    
    private Double cost;
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private String technician;
    private String status; // OPEN, IN_PROGRESS, COMPLETED
}
