package com.portfolio.asset.domain;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Data
public class Asset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String assetCode;
    
    @Enumerated(EnumType.STRING)
    private AssetType assetType;
    
    private String brand;
    private String model;
    
    @Column(unique = true)
    private String serialNumber;
    
    private LocalDate purchaseDate;
    private Double purchaseCost;
    private LocalDate warrantyEndDate;
    
    private String assignedTo;
    private String employeeId;
    private String department;
    private String location;
    
    @Enumerated(EnumType.STRING)
    private AssetStatus status;
    
    @Enumerated(EnumType.STRING)
    private AssetCondition conditionState;
    
    private Integer usageHours = 0;
    private Integer maintenanceCount = 0;
    private Integer incidentCount = 0;
    
    private LocalDate lastMaintenanceDate;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = AssetStatus.AVAILABLE;
        if (conditionState == null) conditionState = AssetCondition.EXCELLENT;
        if (usageHours == null) usageHours = 0;
        if (maintenanceCount == null) maintenanceCount = 0;
        if (incidentCount == null) incidentCount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
