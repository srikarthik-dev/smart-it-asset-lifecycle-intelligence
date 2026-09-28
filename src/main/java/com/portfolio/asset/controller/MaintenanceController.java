package com.portfolio.asset.controller;
import com.portfolio.asset.domain.MaintenanceRecord;
import com.portfolio.asset.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {
    @Autowired
    private AssetService assetService;
    
    @PutMapping("/{id}")
    public MaintenanceRecord updateMaintenance(@PathVariable Long id, @RequestBody MaintenanceRecord record) {
        return assetService.updateMaintenance(id, record);
    }
}
