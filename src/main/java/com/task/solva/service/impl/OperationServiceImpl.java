package com.task.solva.service.impl;

import com.task.solva.model.Currency;
import com.task.solva.model.ExchangeRate;
import com.task.solva.model.Operation;
import com.task.solva.model.Users;
import com.task.solva.repository.ExchangeRateRepository;
import com.task.solva.repository.OperationRepository;
import com.task.solva.repository.UserRepository;
import com.task.solva.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final UserRepository userRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    public Operation createOperation(Operation operation){
        operation.setDate(new Date());
        Optional<Users> user = userRepository.findById(operation.getUserId());
        if(user.isPresent()){
            Users currentUser = user.get();
            ExchangeRate er = getCurrency(operation.getCurrency());
            Double usd;
            double count = 100;
            if(!operation.getCurrency().equals("USD")){
                usd = operation.getValue()/er.getValue();
            } else {
                usd = operation.getValue();
            }
            if(operation.getCategoryId()==1L && currentUser.getProductLimit()<=0){
                Double currentLimit = currentUser.getProductLimit();
                Double result = currentLimit - usd;
                currentUser.setProductLimit(Math.round(result*count)/count);
                operation.setLimitExceeded(true);
                operation.setUserId(currentUser.getId());
                operation.setExchangeRate(er);
            } else if(operation.getCategoryId()==1L){
                Double currentLimit = currentUser.getProductLimit();
                Double result = currentLimit - usd;
                currentUser.setProductLimit(Math.round(result*count)/count);
                operation.setUserId(currentUser.getId());
                operation.setExchangeRate(er);
                if(currentUser.getProductLimit()<0){
                    operation.setLimitExceeded(true);
                }
            } else if(operation.getCategoryId()==2L && currentUser.getServiceLimit()<=0){
                Double currentLimit = currentUser.getServiceLimit();
                Double result = currentLimit - usd;
                currentUser.setServiceLimit(Math.round(result*count)/count);
                operation.setLimitExceeded(true);
                operation.setUserId(currentUser.getId());
                operation.setExchangeRate(er);
            } else if(operation.getCategoryId()==2L){
                Double currentLimit = currentUser.getServiceLimit();
                Double result = currentLimit - usd;
                currentUser.setServiceLimit(Math.round(result*count)/count);
                operation.setUserId(currentUser.getId());
                operation.setExchangeRate(er);
                if(currentUser.getServiceLimit()<0){
                    operation.setLimitExceeded(true);
                }
            }
            userRepository.save(currentUser);
        }
        return operationRepository.save(operation);
    }
    public Operation topUpLimit(Operation operation){
        operation.setDate(new Date());
        Optional<Users> user = userRepository.findById(operation.getUserId());
        if(user.isPresent()){
            Users currentUser = user.get();
            ExchangeRate er = getCurrency(operation.getCurrency());
            operation.setExchangeRate(er);
            double count = 100;
            Double usd;
            if(!operation.getCurrency().equals("USD")){
                usd = operation.getValue()/er.getValue();
            } else {
                usd = operation.getValue();
            }
            if(operation.getCategoryId()==3L){
                Double currentLimit = currentUser.getProductLimit();
                currentUser.setProductLimit((Math.round(currentLimit + usd)*count)/count);
                Double currentOverallProductLimit = currentUser.getOverallProductLimit();
                currentUser.setOverallProductLimit((Math.round(currentOverallProductLimit + usd)*count)/count);
            } else if(operation.getCategoryId()==4L){
                Double currentLimit = currentUser.getServiceLimit();
                currentUser.setServiceLimit((Math.round(currentLimit + usd)*count)/count);
                Double currentOverallServiceLimit = currentUser.getOverallServiceLimit();
                currentUser.setOverallServiceLimit((Math.round(currentOverallServiceLimit + usd)*count)/count);
            }
            userRepository.save(currentUser);
        }
        return operationRepository.save(operation);
    }
    public List<Operation> getAllLimits(Long userId, String startDate, String endDate){
        LocalDate date = LocalDate.parse(startDate);
        LocalDate date1 = LocalDate.parse(endDate);
        Date dateStart = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return operationRepository.getAllLimits(userId, dateStart, dateEnd);
    }
    private ExchangeRate getCurrency(String currency){
        ExchangeRate er = new ExchangeRate();
        if(currency.equals("KZT")){
            er = exchangeRateRepository.getCurrentExchangeRate(Currency.USD_KZT);
        } else if(currency.equals("CNY")){
            er = exchangeRateRepository.getCurrentExchangeRate(Currency.USD_CNY);
        } else if(currency.equals("USD")){
            er = exchangeRateRepository.findById(0L).get();
        }
        return er;
    }
}
