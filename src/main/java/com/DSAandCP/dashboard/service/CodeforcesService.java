package com.DSAandCP.dashboard.service;

import com.DSAandCP.dashboard.dto.AnalysisResponse;
import com.DSAandCP.dashboard.dto.CompareResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CodeforcesService {

    private final RestTemplate restTemplate = new RestTemplate();

    public AnalysisResponse analyzeUser(String handle) {

        String submissionsUrl = "https://codeforces.com/api/user.status?handle="
                + handle + "&from=1&count=200";

        Map response = restTemplate.getForObject(submissionsUrl, Map.class);
        List submissions = (List) response.get("result");

        Set<String> solvedProblems = new HashSet<>();

        Map<String, Integer> difficultyMap = new HashMap<>();
        Map<String, Integer> topicMap = new HashMap<>();
        Map<String, Map<String, Integer>> topicDifficultyMap = new HashMap<>();

        for (Object obj : submissions) {
            Map sub = (Map) obj;

            if (!"OK".equals(sub.get("verdict"))) continue;

            Map problem = (Map) sub.get("problem");

            String problemId = problem.get("contestId") + "-" + problem.get("index");

            if (solvedProblems.contains(problemId)) continue;
            solvedProblems.add(problemId);

            Integer rating = (Integer) problem.get("rating");
            List<String> tags = (List<String>) problem.get("tags");

            String bucket = getDifficultyBucket(rating);

            // Difficulty
            difficultyMap.put(bucket, difficultyMap.getOrDefault(bucket, 0) + 1);

            // Topics
            for (String tag : tags) {

                topicMap.put(tag, topicMap.getOrDefault(tag, 0) + 1);

                topicDifficultyMap.putIfAbsent(tag, new HashMap<>());
                Map<String, Integer> inner = topicDifficultyMap.get(tag);

                inner.put(bucket, inner.getOrDefault(bucket, 0) + 1);
            }
        }

        AnalysisResponse responseDto = new AnalysisResponse();
        responseDto.setUsername(handle);
        responseDto.setDifficultyStats(difficultyMap);
        responseDto.setTopicStats(topicMap);
        responseDto.setTopicDifficultyStats(topicDifficultyMap);

        return responseDto;
    }

    private String getDifficultyBucket(Integer rating) {
        if (rating == null) return "unknown";

        int lower = (rating / 200) * 200;
        int upper = lower + 200;

        return lower + "-" + upper;
    }

    public CompareResponse compareUsers(String user1, String user2) {

        AnalysisResponse analysis1 = analyzeUser(user1);
        AnalysisResponse analysis2 = analyzeUser(user2);

        CompareResponse response = new CompareResponse();
        response.setUser1(analysis1);
        response.setUser2(analysis2);

        return response;
    }
    
    public Map<String, Integer> getTopicAnalysis(String handle, String topic) {

        AnalysisResponse analysis = analyzeUser(handle);

        Map<String, Map<String, Integer>> topicDifficultyMap =
                analysis.getTopicDifficultyStats();

        return topicDifficultyMap.getOrDefault(topic, new HashMap<>());
    }
}