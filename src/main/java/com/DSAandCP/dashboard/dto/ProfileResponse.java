package com.DSAandCP.dashboard.dto;

import lombok.Data;

@Data
public class ProfileResponse {
    private String username;
    private int rating;
    private int maxRating;
    private String rank;
    private int totalSolved;
    private String latestSubmission;
}