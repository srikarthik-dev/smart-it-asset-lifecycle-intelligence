package com.portfolio.asset.service;
import com.portfolio.asset.domain.Asset;
import com.portfolio.asset.domain.AssetStatus;
import com.portfolio.asset.dto.DashboardSummaryResponse;
import com.portfolio.asset.dto.AssetHealthResponse;
import com.portfolio.asset.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private AssetRepository assetRepository;
    
    @Autowired
    private AssetHealthEngine healthEngine;
    
    public DashboardSummaryResponse getSummary() {
        List<Asset> assets = assetRepository.findAll();
        DashboardSummaryResponse summary = new DashboardSummaryResponse();
        
        summary.setTotalAssets(assets.size());
        summary.setAssigned(assets.stream().filter(a -> a.getStatus() == AssetStatus.ASSIGNED).count());
        summary.setAvailable(assets.stream().filter(a -> a.getStatus() == AssetStatus.AVAILABLE).count());
        summary.setUnderMaintenance(assets.stream().filter(a -> a.getStatus() == AssetStatus.UNDER_MAINTENANCE).count());
        
        long expiring = 0;
        long expired = 0;
        long atRisk = 0;
        long replacement = 0;
        
        LocalDate now = LocalDate.now();
        
        for (Asset asset : assets) {
            if (asset.getWarrantyEndDate() != null) {
                if (now.isAfter(asset.getWarrantyEndDate())) {
                    expired++;
                } else if (ChronoUnit.DAYS.between(now, asset.getWarrantyEndDate()) <= 30) {
                    expiring++;
                }
            }
            
            AssetHealthResponse health = healthEngine.evaluate(asset);
            if ("AT_RISK".equals(health.getCategory())) {
                atRisk++;
            } else if ("REPLACEMENT_RECOMMENDED".equals(health.getCategory())) {
                replacement++;
            }
        }
        
        summary.setWarrantyExpiring(expiring);
        summary.setWarrantyExpired(expired);
        summary.setAtRisk(atRisk);
        summary.setReplacementRecommended(replacement);
        
        return summary;
    }
}
