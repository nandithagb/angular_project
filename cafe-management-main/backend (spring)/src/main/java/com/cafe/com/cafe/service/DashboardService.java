package com.cafe.com.cafe.service;

import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface DashboardService {
    // returns count of each database api --> to display on dashboard
    ResponseEntity<Map<String, Object>> getCount();
}
