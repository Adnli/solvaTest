package com.task.solva.service;

import com.task.solva.model.Operation;

import java.util.List;

public interface OperationService {
    public Operation createOperation(Operation operation);
    public Operation topUpLimit(Operation operation);
    public List<Operation> getAllLimits(Long id, String startDate, String endDate);
}
