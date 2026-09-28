package com.portfolio.asset.dto;
import lombok.Data;
import java.util.List;

@Data
public class AssetHealthResponse {
    private int score;
    private String category; // HEALTHY, MONITOR, AT_RISK, REPLACEMENT_RECOMMENDED
    private List<String> factors;
    private String lifecycleRecommendation;
}
