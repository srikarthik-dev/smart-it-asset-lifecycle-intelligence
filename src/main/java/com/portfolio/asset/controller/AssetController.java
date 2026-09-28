package com.portfolio.asset.controller;
import com.portfolio.asset.domain.*;
import com.portfolio.asset.dto.*;
import com.portfolio.asset.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    
    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetHealthEngine healthEngine;
    
    @GetMapping
    public List<Asset> getAllAssets() { return assetService.getAllAssets(); }
    
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Asset createAsset(@RequestBody Asset asset) { return assetService.saveAsset(asset); }
    
    @PutMapping("/{id}")
    public Asset updateAsset(@PathVariable Long id, @RequestBody Asset asset) { return assetService.updateAsset(id, asset); }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/health")
    public ResponseEntity<AssetHealthResponse> getAssetHealth(@PathVariable Long id) {
        return assetService.getAssetById(id).map(a -> ResponseEntity.ok(healthEngine.evaluate(a))).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/lifecycle")
    public LifecycleRecommendationResponse getLifecycle(@PathVariable Long id) { return assetService.getLifecycle(id); }
    
    @PutMapping("/{id}/assign")
    public Asset assignAsset(@PathVariable Long id, @RequestBody AssignAssetRequest req) { return assetService.assignAsset(id, req); }
    
    @PutMapping("/{id}/transfer")
    public Asset transferAsset(@PathVariable Long id, @RequestBody TransferAssetRequest req) { return assetService.transferAsset(id, req); }
    
    @PostMapping("/{id}/maintenance")
    public MaintenanceRecord addMaintenance(@PathVariable Long id, @RequestBody MaintenanceRecord record) { return assetService.addMaintenance(id, record); }
    
    @GetMapping("/{id}/maintenance")
    public List<MaintenanceRecord> getMaintenance(@PathVariable Long id) { return assetService.getMaintenanceRecords(id); }
}
