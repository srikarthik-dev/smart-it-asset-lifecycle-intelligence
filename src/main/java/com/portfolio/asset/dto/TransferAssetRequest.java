package com.portfolio.asset.dto;
import lombok.Data;
@Data
public class TransferAssetRequest {
    private String newEmployeeId;
    private String newLocation;
    private String reason;
}
