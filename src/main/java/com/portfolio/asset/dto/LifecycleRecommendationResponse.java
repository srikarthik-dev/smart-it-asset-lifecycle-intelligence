package com.portfolio.asset.dto;
import lombok.Data;
@Data
public class LifecycleRecommendationResponse {
    private String recommendation;
    private String justification;
    private AssetHealthResponse health;
}
