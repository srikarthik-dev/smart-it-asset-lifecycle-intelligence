package com.portfolio.asset.controller;

import com.portfolio.asset.domain.Asset;
import com.portfolio.asset.domain.AssetStatus;
import com.portfolio.asset.service.AssetService;
import com.portfolio.asset.service.AssetHealthEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssetControllerTest {

    @Mock
    private AssetService assetService;
    
    @Mock
    private AssetHealthEngine healthEngine;

    @InjectMocks
    private AssetController assetController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAssets() {
        when(assetService.getAllAssets()).thenReturn(Arrays.asList(new Asset(), new Asset()));
        List<Asset> result = assetController.getAllAssets();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAssetByIdFound() {
        Asset asset = new Asset();
        asset.setId(1L);
        when(assetService.getAssetById(1L)).thenReturn(Optional.of(asset));
        
        ResponseEntity<Asset> response = assetController.getAssetById(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testGetAssetByIdNotFound() {
        when(assetService.getAssetById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Asset> response = assetController.getAssetById(1L);
        assertTrue(response.getStatusCode().is4xxClientError());
    }
    
    @Test
    public void testCreateAsset() {
        Asset asset = new Asset();
        when(assetService.saveAsset(any(Asset.class))).thenReturn(asset);
        Asset result = assetController.createAsset(asset);
        assertNotNull(result);
    }
    
    @Test
    public void testDeleteAsset() {
        ResponseEntity<Void> response = assetController.deleteAsset(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(assetService, times(1)).deleteAsset(1L);
    }
}
