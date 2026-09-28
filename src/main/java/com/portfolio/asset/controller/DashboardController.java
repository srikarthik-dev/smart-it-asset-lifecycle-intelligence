package com.portfolio.asset.controller;
import com.portfolio.asset.domain.Asset;
import com.portfolio.asset.dto.DashboardSummaryResponse;
import com.portfolio.asset.service.DashboardService;
import com.portfolio.asset.service.AssetService;
import com.portfolio.asset.service.AssetHealthEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetHealthEngine healthEngine;
    
    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() { return dashboardService.getSummary(); }
    
    @GetMapping("/by-type")
    public Map<String, Long> getByType() {
        return assetService.getAllAssets().stream()
            .filter(a -> a.getAssetType() != null)
            .collect(Collectors.groupingBy(a -> a.getAssetType().name(), Collectors.counting()));
    }
    
    @GetMapping("/by-status")
    public Map<String, Long> getByStatus() {
        return assetService.getAllAssets().stream()
            .filter(a -> a.getStatus() != null)
            .collect(Collectors.groupingBy(a -> a.getStatus().name(), Collectors.counting()));
    }
    
    @GetMapping("/by-condition")
    public Map<String, Long> getByCondition() {
        return assetService.getAllAssets().stream()
            .filter(a -> a.getConditionState() != null)
            .collect(Collectors.groupingBy(a -> a.getConditionState().name(), Collectors.counting()));
    }
    
    @GetMapping("/by-health")
    public Map<String, Long> getByHealth() {
        return assetService.getAllAssets().stream()
            .collect(Collectors.groupingBy(a -> healthEngine.evaluate(a).getCategory(), Collectors.counting()));
    }
    
    @GetMapping("/by-department")
    public Map<String, Long> getByDepartment() {
        return assetService.getAllAssets().stream()
            .filter(a -> a.getDepartment() != null)
            .collect(Collectors.groupingBy(Asset::getDepartment, Collectors.counting()));
    }
    
    @GetMapping("/warranty-alerts")
    public List<Asset> getWarrantyAlerts() {
        LocalDate now = LocalDate.now();
        return assetService.getAllAssets().stream()
            .filter(a -> a.getWarrantyEndDate() != null && 
                         (now.isAfter(a.getWarrantyEndDate()) || ChronoUnit.DAYS.between(now, a.getWarrantyEndDate()) <= 30))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/replacement-recommendations")
    public List<Asset> getReplacementRecommendations() {
        return assetService.getAllAssets().stream()
            .filter(a -> "REPLACEMENT_RECOMMENDED".equals(healthEngine.evaluate(a).getCategory()))
            .collect(Collectors.toList());
    }
}
