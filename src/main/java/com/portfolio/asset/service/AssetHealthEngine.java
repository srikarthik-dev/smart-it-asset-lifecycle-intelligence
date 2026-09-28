package com.portfolio.asset.service;
import com.portfolio.asset.domain.Asset;
import com.portfolio.asset.domain.AssetCondition;
import com.portfolio.asset.dto.AssetHealthResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssetHealthEngine {

    public AssetHealthResponse evaluate(Asset asset) {
        int riskScore = 0;
        List<String> factors = new ArrayList<>();
        
        // 1. Age Risk (older than 3 years increases risk)
        if (asset.getPurchaseDate() != null) {
            long years = ChronoUnit.YEARS.between(asset.getPurchaseDate(), LocalDate.now());
            if (years >= 5) {
                riskScore += 30;
                factors.add("Asset age: critical risk (>5 years)");
            } else if (years >= 3) {
                riskScore += 15;
                factors.add("Asset age: high risk (>3 years)");
            }
        }
        
        // 2. Warranty Risk
        if (asset.getWarrantyEndDate() != null) {
            if (LocalDate.now().isAfter(asset.getWarrantyEndDate())) {
                riskScore += 20;
                factors.add("Warranty: expired");
            } else if (ChronoUnit.DAYS.between(LocalDate.now(), asset.getWarrantyEndDate()) < 30) {
                riskScore += 10;
                factors.add("Warranty: expiring soon");
            }
        } else {
            riskScore += 20;
            factors.add("Warranty: unknown/none");
        }
        
        // 3. Maintenance Risk
        if (asset.getMaintenanceCount() > 5) {
            riskScore += 20;
            factors.add("Maintenance history: highly frequent");
        } else if (asset.getMaintenanceCount() > 2) {
            riskScore += 10;
            factors.add("Maintenance history: frequent");
        }
        
        // 4. Incident Risk
        if (asset.getIncidentCount() > 3) {
            riskScore += 15;
            factors.add("Incident history: frequent");
        }
        
        // 5. Condition Risk
        if (asset.getConditionState() == AssetCondition.CRITICAL) {
            riskScore += 40;
            factors.add("Condition: critical");
        } else if (asset.getConditionState() == AssetCondition.POOR) {
            riskScore += 20;
            factors.add("Condition: poor");
        }
        
        // 6. Usage Risk
        if (asset.getUsageHours() > 10000) {
            riskScore += 10;
            factors.add("Usage: high");
        }
        
        // Determine category and recommendation
        AssetHealthResponse response = new AssetHealthResponse();
        response.setScore(Math.min(riskScore, 100));
        response.setFactors(factors);
        
        if (riskScore >= 75) {
            response.setCategory("REPLACEMENT_RECOMMENDED");
            response.setLifecycleRecommendation("RETIRE");
        } else if (riskScore >= 50) {
            response.setCategory("AT_RISK");
            response.setLifecycleRecommendation("MAINTENANCE_REQUIRED");
        } else if (riskScore >= 25) {
            response.setCategory("MONITOR");
            response.setLifecycleRecommendation("MONITOR");
        } else {
            response.setCategory("HEALTHY");
            response.setLifecycleRecommendation("KEEP_IN_SERVICE");
        }
        
        return response;
    }
}
