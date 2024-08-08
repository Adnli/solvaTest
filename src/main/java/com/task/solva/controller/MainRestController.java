package com.task.solva.controller;

import com.task.solva.model.ExchangeRate;
import com.task.solva.model.Operation;
import com.task.solva.service.ExchangeRateService;
import com.task.solva.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MainRestController {
    private final ExchangeRateService exchangeRateService;
    private final OperationService operationService;

    @PostMapping(value = "/operation")
    public ResponseEntity<Operation> operation(@RequestBody Operation operation) {
        Operation response = operationService.createOperation(operation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/getExchangeRates")
    public ResponseEntity<ExchangeRate> getExchangeRates(@RequestParam(value = "currency") String currency) {
        ExchangeRate response = exchangeRateService.getExchangeRate(currency);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/topUpLimit")
    public ResponseEntity<Operation> topUpLimit(@RequestBody Operation operation) {
        Operation response = operationService.topUpLimit(operation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/getLimits")
    public List<Operation> getLimits(@RequestParam(value = "start_date") String startDate,
                                     @RequestParam(value = "end_date") String endDate,
                                     @RequestParam(value = "user_id") Long id){
        return operationService.getAllLimits(id, startDate, endDate);
    }
}
