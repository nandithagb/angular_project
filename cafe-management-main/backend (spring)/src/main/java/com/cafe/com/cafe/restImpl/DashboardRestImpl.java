package com.cafe.com.cafe.restImpl;

import com.cafe.com.cafe.rest.DashboardRest;
import com.cafe.com.cafe.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class DashboardRestImpl implements DashboardRest {

    @Autowired
    DashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        try {
            return dashboardService.getCount();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
