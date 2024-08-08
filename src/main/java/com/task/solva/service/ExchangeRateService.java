package com.task.solva.service;

import com.task.solva.model.ExchangeRate;

public interface ExchangeRateService {
    ExchangeRate getExchangeRate(String currency);
}
