package com.portfolio.asset.dto;
import lombok.Data;

@Data
public class DashboardSummaryResponse {
    private long totalAssets;
    private long assigned;
    private long available;
    private long underMaintenance;
    private long warrantyExpiring;
    private long warrantyExpired;
    private long atRisk;
    private long replacementRecommended;
}
