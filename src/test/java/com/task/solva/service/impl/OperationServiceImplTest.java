package com.task.solva.service.impl;

import com.task.solva.model.ExchangeRate;
import com.task.solva.model.Operation;
import com.task.solva.repository.OperationRepository;
import com.task.solva.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OperationServiceImplTest {
    @Mock
    private OperationRepository operationRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private OperationServiceImpl operationService;

    @Test
    public void checkOperation(){
        List<Operation> result = operationService.getAllLimits(1L, "2024-08-07", "2024-08-10");

        operationService.createOperation(new Operation(1L, "KZT", 40000.0, new Date(), 2L, 1L,
                true, new ExchangeRate(1L, "USD/KZT", 458.5, new Date())));
        Assertions.assertNotNull(result);
    }

    @Test
    public void checkTopUpLimits(){
        Operation operation = getTopUpLimitOperation();
        operationService.topUpLimit(operation);
    }



    private List<Operation> getOperations(){
        Operation op = new Operation();
        Operation op2 = new Operation();

        op.setId(2L);
        op.setCurrency("CNY");
        op.setValue(500.0);
        op.setDate(new Date());
        op.setCategoryId(2L);
        op.setUserId(1L);
        op.setLimitExceeded(false);
        op.setExchangeRate(new ExchangeRate(2L, "USD/CNY", 7.88, new Date()));

        op2.setId(3L);
        op2.setCurrency("KZT");
        op2.setValue(5000.0);
        op2.setDate(new Date());
        op2.setCategoryId(1L);
        op2.setUserId(1L);
        op2.setLimitExceeded(true);
        op2.setExchangeRate(new ExchangeRate(3L, "USD/KZT", 457.88, new Date()));

        return List.of(op, op2);
    }
    private Operation getTopUpLimitOperation(){
        Operation operation = new Operation();
        operation.setUserId(1L);
        operation.setCurrency("CNY");
        operation.setValue(1000.0);
        operation.setCategoryId(4L);
        return operation;
    }
}
