package com.portfolio.asset.service;

import com.portfolio.asset.domain.Asset;
import com.portfolio.asset.domain.AssetCondition;
import com.portfolio.asset.dto.AssetHealthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AssetHealthEngineTest {

    private AssetHealthEngine engine;

    @BeforeEach
    public void setup() {
        engine = new AssetHealthEngine();
    }

    @Test
    public void testNewHealthyAsset() {
        Asset asset = new Asset();
        asset.setPurchaseDate(LocalDate.now().minusMonths(6));
        asset.setWarrantyEndDate(LocalDate.now().plusYears(1));
        asset.setConditionState(AssetCondition.EXCELLENT);
        asset.setMaintenanceCount(0);
        asset.setIncidentCount(0);
        asset.setUsageHours(500);

        AssetHealthResponse response = engine.evaluate(asset);
        
        assertEquals("HEALTHY", response.getCategory());
        assertEquals("KEEP_IN_SERVICE", response.getLifecycleRecommendation());
        assertTrue(response.getScore() < 25);
    }

    @Test
    public void testOldAsset() {
        Asset asset = new Asset();
        asset.setPurchaseDate(LocalDate.now().minusYears(6)); // > 5 years
        asset.setWarrantyEndDate(LocalDate.now().plusMonths(6));
        asset.setConditionState(AssetCondition.GOOD);
        asset.setMaintenanceCount(1);
        asset.setIncidentCount(0);

        AssetHealthResponse response = engine.evaluate(asset);
        
        assertTrue(response.getScore() >= 30);
        assertTrue(response.getFactors().stream().anyMatch(f -> f.contains("Asset age: critical risk")));
    }

    @Test
    public void testExpiredWarranty() {
        Asset asset = new Asset();
        asset.setPurchaseDate(LocalDate.now().minusYears(2));
        asset.setWarrantyEndDate(LocalDate.now().minusMonths(1)); // Expired
        asset.setConditionState(AssetCondition.GOOD);
        asset.setMaintenanceCount(1);
        asset.setIncidentCount(0);

        AssetHealthResponse response = engine.evaluate(asset);
        
        assertTrue(response.getFactors().stream().anyMatch(f -> f.contains("Warranty: expired")));
    }

    @Test
    public void testCriticalCondition() {
        Asset asset = new Asset();
        asset.setPurchaseDate(LocalDate.now().minusYears(1));
        asset.setWarrantyEndDate(LocalDate.now().plusYears(2));
        asset.setConditionState(AssetCondition.CRITICAL);
        asset.setMaintenanceCount(0);
        asset.setIncidentCount(0);

        AssetHealthResponse response = engine.evaluate(asset);
        
        assertTrue(response.getScore() >= 40);
        assertTrue(response.getFactors().stream().anyMatch(f -> f.contains("Condition: critical")));
    }

    @Test
    public void testCombinedRiskFactors() {
        Asset asset = new Asset();
        asset.setPurchaseDate(LocalDate.now().minusYears(4)); // High risk age (+15)
        asset.setWarrantyEndDate(LocalDate.now().minusDays(10)); // Expired warranty (+20)
        asset.setConditionState(AssetCondition.POOR); // Poor condition (+20)
        asset.setMaintenanceCount(6); // Highly frequent maintenance (+20)
        asset.setIncidentCount(4); // Frequent incident (+15)
        asset.setUsageHours(15000); // High usage (+10)

        AssetHealthResponse response = engine.evaluate(asset);
        
        // 15+20+20+20+15+10 = 100
        assertEquals(100, response.getScore());
        assertEquals("REPLACEMENT_RECOMMENDED", response.getCategory());
        assertEquals("RETIRE", response.getLifecycleRecommendation());
    }
}
