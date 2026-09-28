package com.portfolio.asset.dto;
import lombok.Data;
@Data
public class AssignAssetRequest {
    private String employeeId;
    private String department;
    private String location;
}
