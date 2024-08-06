package com.gorselprog.bankmanagementv3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Service
public class StockService {

    @Value("${alphavantage.apikey}")
    private String apiKey;

    public Map<String, Object> getStockData(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=1min&apikey=" + apiKey;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return response;
    }
}
