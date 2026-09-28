package com.portfolio.asset.domain;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_transfers")
@Data
public class AssetTransfer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
    
    private String previousEmployee;
    private String previousEmployeeId;
    private String newEmployee;
    private String newEmployeeId;
    
    private String previousLocation;
    private String newLocation;
    
    private String reason;
    private LocalDateTime transferDate;
}
