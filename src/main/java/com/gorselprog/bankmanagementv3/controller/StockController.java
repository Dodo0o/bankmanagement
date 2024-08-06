package com.gorselprog.bankmanagementv3.controller;

import com.gorselprog.bankmanagementv3.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stocks")
    public String getStockData(@RequestParam String symbol, Model model) {
        Map<String, Object> stockData = stockService.getStockData(symbol);
        model.addAttribute("stockData", stockData);
        return "stock-data";
    }
}
