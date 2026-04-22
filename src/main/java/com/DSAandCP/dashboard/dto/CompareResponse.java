package com.DSAandCP.dashboard.dto;

import lombok.Data;

@Data
public class CompareResponse {

    private AnalysisResponse user1;
    private AnalysisResponse user2;
}