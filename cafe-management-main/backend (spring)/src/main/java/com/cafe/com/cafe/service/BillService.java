package com.cafe.com.cafe.service;

import com.cafe.com.cafe.modal.Bill;
import com.cafe.com.cafe.modal.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {

    // creates the pdf document for the bill api
    ResponseEntity<String> generateReport(Map<String, Object> requestMap);

    // retrieves all bills api
    ResponseEntity<List<Bill>> getBills();

    // retrieves bills as a pdf (byte array) api
    ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);

    // deletes a bill api
    ResponseEntity<String> deleteBill(Integer id);

}
