package com.portfolio.asset.service;

import com.portfolio.asset.domain.*;
import com.portfolio.asset.dto.*;
import com.portfolio.asset.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetTransferRepository transferRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    @Autowired
    private AssetHealthEngine healthEngine;

    public List<Asset> getAllAssets() { return assetRepository.findAll(); }
    public Optional<Asset> getAssetById(Long id) { return assetRepository.findById(id); }
    public Asset saveAsset(Asset asset) { return assetRepository.save(asset); }
    public void deleteAsset(Long id) { assetRepository.deleteById(id); }

    public Asset updateAsset(Long id, Asset updatedAsset) {
        return assetRepository.findById(id).map(asset -> {
            asset.setBrand(updatedAsset.getBrand());
            asset.setModel(updatedAsset.getModel());
            asset.setPurchaseCost(updatedAsset.getPurchaseCost());
            asset.setPurchaseDate(updatedAsset.getPurchaseDate());
            asset.setWarrantyEndDate(updatedAsset.getWarrantyEndDate());
            asset.setConditionState(updatedAsset.getConditionState());
            asset.setUsageHours(updatedAsset.getUsageHours());
            return assetRepository.save(asset);
        }).orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public Asset assignAsset(Long id, AssignAssetRequest request) {
        Asset asset = assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));
        if (asset.getStatus() == AssetStatus.RETIRED || asset.getStatus() == AssetStatus.LOST) {
            throw new RuntimeException("Cannot assign retired or lost asset");
        }
        
        asset.setAssignedTo(request.getEmployeeId()); // Using employeeId as name for simplicity unless we lookup Employee
        asset.setEmployeeId(request.getEmployeeId());
        asset.setDepartment(request.getDepartment());
        asset.setLocation(request.getLocation());
        asset.setStatus(AssetStatus.ASSIGNED);
        
        return assetRepository.save(asset);
    }

    public Asset transferAsset(Long id, TransferAssetRequest request) {
        Asset asset = assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));
        
        AssetTransfer transfer = new AssetTransfer();
        transfer.setAsset(asset);
        transfer.setPreviousEmployeeId(asset.getEmployeeId());
        transfer.setPreviousLocation(asset.getLocation());
        transfer.setNewEmployeeId(request.getNewEmployeeId());
        transfer.setNewLocation(request.getNewLocation());
        transfer.setReason(request.getReason());
        transfer.setTransferDate(LocalDateTime.now());
        
        transferRepository.save(transfer);
        
        asset.setEmployeeId(request.getNewEmployeeId());
        asset.setAssignedTo(request.getNewEmployeeId());
        asset.setLocation(request.getNewLocation());
        
        return assetRepository.save(asset);
    }
    
    public MaintenanceRecord addMaintenance(Long assetId, MaintenanceRecord record) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new RuntimeException("Asset not found"));
        record.setAsset(asset);
        MaintenanceRecord saved = maintenanceRepository.save(record);
        
        asset.setMaintenanceCount((asset.getMaintenanceCount() == null ? 0 : asset.getMaintenanceCount()) + 1);
        if ("HARDWARE_FAILURE".equals(record.getType()) || "SOFTWARE".equals(record.getType())) {
            asset.setIncidentCount((asset.getIncidentCount() == null ? 0 : asset.getIncidentCount()) + 1);
        }
        if (record.getCompletedDate() != null) {
            asset.setLastMaintenanceDate(record.getCompletedDate());
        }
        assetRepository.save(asset);
        return saved;
    }

    public List<MaintenanceRecord> getMaintenanceRecords(Long assetId) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new RuntimeException("Asset not found"));
        // This is inefficient but works for now. A custom query is better.
        return maintenanceRepository.findAll().stream().filter(m -> m.getAsset().getId().equals(assetId)).toList();
    }
    
    public MaintenanceRecord updateMaintenance(Long id, MaintenanceRecord record) {
        return maintenanceRepository.findById(id).map(m -> {
            m.setStatus(record.getStatus());
            m.setCompletedDate(record.getCompletedDate());
            m.setCost(record.getCost());
            m.setTechnician(record.getTechnician());
            m.setDescription(record.getDescription());
            
            if ("COMPLETED".equals(record.getStatus()) && record.getCompletedDate() != null) {
                Asset asset = m.getAsset();
                asset.setLastMaintenanceDate(record.getCompletedDate());
                assetRepository.save(asset);
            }
            
            return maintenanceRepository.save(m);
        }).orElseThrow(() -> new RuntimeException("Maintenance not found"));
    }

    public LifecycleRecommendationResponse getLifecycle(Long id) {
        Asset asset = assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));
        AssetHealthResponse health = healthEngine.evaluate(asset);
        
        LifecycleRecommendationResponse response = new LifecycleRecommendationResponse();
        response.setHealth(health);
        response.setRecommendation(health.getLifecycleRecommendation());
        response.setJustification("Based on health score of " + health.getScore());
        return response;
    }
}
