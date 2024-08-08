package com.task.solva.service.impl;

import com.task.solva.model.ExchangeRate;
import com.task.solva.repository.ExchangeRateRepository;
import com.task.solva.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    public ExchangeRate getExchangeRate(String currency){
        ExchangeRate er = new ExchangeRate();
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            Date currentTime = new Date();
            Date today = new Date(currentTime.getTime() - 60000);
            long diffDay = today.getTime() - 86400000;
            Date yesterday = new Date(diffDay);
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Future<Response> responseFuture = client.prepareGet("https://api.twelvedata.com/time_series?apikey=9cb9e8a5118a4ec098cb05503cae1f4e&interval=1day&symbol="
                    + currency + "&country=Kazakhstan&type=index&timezone=Asia/Almaty&format=JSON&start_date="
                    + formater.format(yesterday)
                    + "&end_date=" + formater.format(today)
                    + "&dp=2&outputsize=12").execute();
            Response response = responseFuture.get();
            client.close();
            JSONObject jsonObject = new JSONObject(response.getResponseBody());
            JSONObject j1 = (JSONObject) jsonObject.get("meta");
            JSONArray jsonArray = (JSONArray) jsonObject.get("values");
            JSONObject j2 = (JSONObject) jsonArray.get(0);
            er.setName(j1.getString("symbol"));
            LocalDate date = LocalDate.parse((CharSequence) j2.get("datetime"));
            er.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            er.setValue(Double.valueOf((String) j2.get("close")));
            exchangeRateRepository.save(er);
        } catch (Exception e){
            e.printStackTrace();
        }
        return er;
    }
    public ExchangeRate getLocalExchangeRate(String currency){
        ExchangeRate er = exchangeRateRepository.getCurrentExchangeRate(currency);
        return er;
    }
}
