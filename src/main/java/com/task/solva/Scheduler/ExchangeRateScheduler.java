package com.task.solva.Scheduler;

import com.task.solva.model.Currency;
import com.task.solva.service.ExchangeRateService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
public class ExchangeRateScheduler {

    private final ExchangeRateService exchangeRateService;

    @Scheduled(cron = "0 0 8 * * *", zone = "GMT+5")
    public void checkExchangeRate(){
        exchangeRateService.getExchangeRate(Currency.USD_KZT);
        exchangeRateService.getExchangeRate(Currency.USD_CNY);
    }
}
