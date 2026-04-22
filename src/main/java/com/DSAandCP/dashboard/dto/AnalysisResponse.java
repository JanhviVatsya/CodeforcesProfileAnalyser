package com.DSAandCP.dashboard.dto;

import lombok.Data;
import java.util.Map;

@Data
public class AnalysisResponse {

    private String username;

    private Map<String, Integer> difficultyStats;

    private Map<String, Integer> topicStats;

    private Map<String, Map<String, Integer>> topicDifficultyStats;
}