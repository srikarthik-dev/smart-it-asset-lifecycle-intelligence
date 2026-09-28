package com.portfolio.asset.service;

import com.portfolio.asset.domain.*;
import com.portfolio.asset.dto.*;
import com.portfolio.asset.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AssetTransferRepository transferRepository;
    @Mock
    private MaintenanceRepository maintenanceRepository;
    @Mock
    private AssetHealthEngine healthEngine;

    @InjectMocks
    private AssetService assetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAssignAvailableAsset() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setStatus(AssetStatus.AVAILABLE);

        AssignAssetRequest req = new AssignAssetRequest();
        req.setEmployeeId("E123");
        req.setDepartment("IT");
        req.setLocation("NY");

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArguments()[0]);

        Asset updated = assetService.assignAsset(1L, req);
        assertEquals("E123", updated.getEmployeeId());
        assertEquals(AssetStatus.ASSIGNED, updated.getStatus());
    }

    @Test
    public void testAssignRetiredAssetThrowsException() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setStatus(AssetStatus.RETIRED);

        AssignAssetRequest req = new AssignAssetRequest();
        req.setEmployeeId("E123");

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        Exception exception = assertThrows(RuntimeException.class, () -> assetService.assignAsset(1L, req));
        assertEquals("Cannot assign retired or lost asset", exception.getMessage());
    }

    @Test
    public void testTransferAsset() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setEmployeeId("E123");
        asset.setLocation("NY");

        TransferAssetRequest req = new TransferAssetRequest();
        req.setNewEmployeeId("E456");
        req.setNewLocation("CA");
        req.setReason("Relocation");

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArguments()[0]);

        Asset updated = assetService.transferAsset(1L, req);
        
        assertEquals("E456", updated.getEmployeeId());
        assertEquals("CA", updated.getLocation());
        verify(transferRepository, times(1)).save(any(AssetTransfer.class));
    }

    @Test
    public void testAddMaintenance() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setMaintenanceCount(1);
        asset.setIncidentCount(0);

        MaintenanceRecord record = new MaintenanceRecord();
        record.setType("HARDWARE_FAILURE");

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(maintenanceRepository.save(any(MaintenanceRecord.class))).thenAnswer(i -> i.getArguments()[0]);

        assetService.addMaintenance(1L, record);

        assertEquals(2, asset.getMaintenanceCount());
        assertEquals(1, asset.getIncidentCount()); // Because it's hardware failure
        verify(assetRepository, times(1)).save(asset);
    }
}
