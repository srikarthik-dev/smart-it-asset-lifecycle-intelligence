package com.portfolio.asset.service;

import com.portfolio.asset.domain.Asset;
import com.portfolio.asset.domain.AssetStatus;
import com.portfolio.asset.dto.AssetHealthResponse;
import com.portfolio.asset.dto.DashboardSummaryResponse;
import com.portfolio.asset.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DashboardServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetHealthEngine healthEngine;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSummary() {
        Asset a1 = new Asset(); a1.setStatus(AssetStatus.ASSIGNED);
        Asset a2 = new Asset(); a2.setStatus(AssetStatus.AVAILABLE); a2.setWarrantyEndDate(LocalDate.now().minusDays(1));
        Asset a3 = new Asset(); a3.setStatus(AssetStatus.UNDER_MAINTENANCE); a3.setWarrantyEndDate(LocalDate.now().plusDays(10));
        
        List<Asset> assets = Arrays.asList(a1, a2, a3);
        when(assetRepository.findAll()).thenReturn(assets);
        
        AssetHealthResponse health1 = new AssetHealthResponse(); health1.setCategory("HEALTHY");
        AssetHealthResponse health2 = new AssetHealthResponse(); health2.setCategory("REPLACEMENT_RECOMMENDED");
        AssetHealthResponse health3 = new AssetHealthResponse(); health3.setCategory("AT_RISK");
        
        when(healthEngine.evaluate(a1)).thenReturn(health1);
        when(healthEngine.evaluate(a2)).thenReturn(health2);
        when(healthEngine.evaluate(a3)).thenReturn(health3);

        DashboardSummaryResponse summary = dashboardService.getSummary();

        assertEquals(3, summary.getTotalAssets());
        assertEquals(1, summary.getAssigned());
        assertEquals(1, summary.getAvailable());
        assertEquals(1, summary.getUnderMaintenance());
        assertEquals(1, summary.getWarrantyExpired());
        assertEquals(1, summary.getWarrantyExpiring());
        assertEquals(1, summary.getAtRisk());
        assertEquals(1, summary.getReplacementRecommended());
    }
}
