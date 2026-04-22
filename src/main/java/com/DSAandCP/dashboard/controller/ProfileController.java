package com.DSAandCP.dashboard.controller;

import org.springframework.web.bind.annotation.*;

import com.DSAandCP.dashboard.dto.AnalysisResponse;
import com.DSAandCP.dashboard.dto.CompareResponse;
import com.DSAandCP.dashboard.dto.ProfileResponse;
import com.DSAandCP.dashboard.service.CodeforcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/profile")
public class ProfileController{
	
	@Autowired
	private CodeforcesService codeforcesService;
	
	@GetMapping("/codeforces/{handle}")
    public AnalysisResponse analyzeUser(@PathVariable String handle) {
        return codeforcesService.analyzeUser(handle);
    }
	
	@GetMapping("/compare")
	public CompareResponse compareUsers(
	        @RequestParam String user1,
	        @RequestParam String user2) {

	    return codeforcesService.compareUsers(user1, user2);
	}
	
	@GetMapping("/topic-analysis")
	public Map<String, Integer> getTopicAnalysis(
	        @RequestParam String handle,
	        @RequestParam String topic) {

	    return codeforcesService.getTopicAnalysis(handle, topic);
	}
}
