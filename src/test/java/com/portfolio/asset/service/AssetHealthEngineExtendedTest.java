package com.portfolio.asset.service;

import com.portfolio.asset.domain.Asset;
import com.portfolio.asset.domain.AssetCondition;
import com.portfolio.asset.dto.AssetHealthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class AssetHealthEngineExtendedTest {
    private AssetHealthEngine engine;

    @BeforeEach
    public void setup() {
        engine = new AssetHealthEngine();
    }

    @Test
    public void testAgeBoundaries() {
        Asset asset1 = new Asset();
        asset1.setPurchaseDate(LocalDate.now().minusYears(2).minusMonths(11));
        asset1.setWarrantyEndDate(LocalDate.now().plusYears(1));
        asset1.setUsageHours(0); asset1.setMaintenanceCount(0); asset1.setIncidentCount(0);
        assertTrue(engine.evaluate(asset1).getScore() < 15);

        Asset asset2 = new Asset();
        asset2.setPurchaseDate(LocalDate.now().minusYears(3).minusDays(1));
        asset2.setWarrantyEndDate(LocalDate.now().plusYears(1));
        asset2.setUsageHours(0); asset2.setMaintenanceCount(0); asset2.setIncidentCount(0);
        assertTrue(engine.evaluate(asset2).getScore() >= 15);

        Asset asset3 = new Asset();
        asset3.setPurchaseDate(LocalDate.now().minusYears(5).minusDays(1));
        asset3.setWarrantyEndDate(LocalDate.now().plusYears(1));
        asset3.setUsageHours(0); asset3.setMaintenanceCount(0); asset3.setIncidentCount(0);
        assertTrue(engine.evaluate(asset3).getScore() >= 30);
    }

    @Test
    public void testWarrantyBoundaries() {
        Asset asset1 = new Asset();
        asset1.setWarrantyEndDate(LocalDate.now().plusDays(31));
        assertFalse(engine.evaluate(asset1).getFactors().stream().anyMatch(f -> f.contains("Warranty")));

        Asset asset2 = new Asset();
        asset2.setWarrantyEndDate(LocalDate.now().plusDays(29));
        assertTrue(engine.evaluate(asset2).getFactors().stream().anyMatch(f -> f.contains("Warranty: expiring soon")));

        Asset asset3 = new Asset();
        asset3.setWarrantyEndDate(LocalDate.now().minusDays(1));
        assertTrue(engine.evaluate(asset3).getFactors().stream().anyMatch(f -> f.contains("Warranty: expired")));
    }

    @Test
    public void testMaintenanceThresholds() {
        Asset asset1 = new Asset();
        asset1.setMaintenanceCount(2);
        assertFalse(engine.evaluate(asset1).getFactors().stream().anyMatch(f -> f.contains("Maintenance")));

        Asset asset2 = new Asset();
        asset2.setMaintenanceCount(3);
        assertTrue(engine.evaluate(asset2).getFactors().stream().anyMatch(f -> f.contains("Maintenance history: frequent")));

        Asset asset3 = new Asset();
        asset3.setMaintenanceCount(6);
        assertTrue(engine.evaluate(asset3).getFactors().stream().anyMatch(f -> f.contains("Maintenance history: highly frequent")));
    }
    
    @Test
    public void testIncidentThresholds() {
        Asset asset1 = new Asset();
        asset1.setIncidentCount(3);
        assertFalse(engine.evaluate(asset1).getFactors().stream().anyMatch(f -> f.contains("Incident")));

        Asset asset2 = new Asset();
        asset2.setIncidentCount(4);
        assertTrue(engine.evaluate(asset2).getFactors().stream().anyMatch(f -> f.contains("Incident")));
    }
    
    @Test
    public void testConditionScores() {
        Asset asset1 = new Asset(); asset1.setConditionState(AssetCondition.EXCELLENT);
        Asset asset2 = new Asset(); asset2.setConditionState(AssetCondition.POOR);
        Asset asset3 = new Asset(); asset3.setConditionState(AssetCondition.CRITICAL);
        
        int s1 = engine.evaluate(asset1).getScore();
        int s2 = engine.evaluate(asset2).getScore();
        int s3 = engine.evaluate(asset3).getScore();
        
        assertTrue(s2 > s1);
        assertTrue(s3 > s2);
        assertTrue(engine.evaluate(asset2).getFactors().stream().anyMatch(f -> f.contains("Condition: poor")));
        assertTrue(engine.evaluate(asset3).getFactors().stream().anyMatch(f -> f.contains("Condition: critical")));
    }
    
    @Test
    public void testUsageThreshold() {
        Asset asset1 = new Asset(); asset1.setUsageHours(9999);
        assertFalse(engine.evaluate(asset1).getFactors().stream().anyMatch(f -> f.contains("Usage")));

        Asset asset2 = new Asset(); asset2.setUsageHours(10001);
        assertTrue(engine.evaluate(asset2).getFactors().stream().anyMatch(f -> f.contains("Usage: high")));
    }
    
    @Test
    public void testCategoryMapping() {
        Asset asset = new Asset();
        
        // 0-24 -> HEALTHY
        asset.setConditionState(AssetCondition.EXCELLENT);
        assertEquals("HEALTHY", engine.evaluate(asset).getCategory());
        
        // 25-49 -> MONITOR
        asset.setConditionState(AssetCondition.POOR); // +20
        asset.setWarrantyEndDate(LocalDate.now().plusDays(10)); // +10
        assertEquals("MONITOR", engine.evaluate(asset).getCategory());
        
        // 50-74 -> AT_RISK
        asset.setConditionState(AssetCondition.CRITICAL); // +40
        asset.setWarrantyEndDate(LocalDate.now().minusDays(10)); // +20
        assertEquals("AT_RISK", engine.evaluate(asset).getCategory());
        
        // 75+ -> REPLACEMENT_RECOMMENDED
        asset.setPurchaseDate(LocalDate.now().minusYears(6)); // +30
        assertEquals("REPLACEMENT_RECOMMENDED", engine.evaluate(asset).getCategory());
    }
    
    @Test
    public void testLifecycleMapping() {
        Asset asset = new Asset();
        assertEquals("KEEP_IN_SERVICE", engine.evaluate(asset).getLifecycleRecommendation());
        
        asset.setConditionState(AssetCondition.POOR); 
        asset.setWarrantyEndDate(LocalDate.now().plusDays(10));
        assertEquals("MONITOR", engine.evaluate(asset).getLifecycleRecommendation());
        
        asset.setConditionState(AssetCondition.CRITICAL);
        asset.setWarrantyEndDate(LocalDate.now().minusDays(10));
        assertEquals("MAINTENANCE_REQUIRED", engine.evaluate(asset).getLifecycleRecommendation());
        
        asset.setPurchaseDate(LocalDate.now().minusYears(6));
        assertEquals("RETIRE", engine.evaluate(asset).getLifecycleRecommendation());
    }
}
