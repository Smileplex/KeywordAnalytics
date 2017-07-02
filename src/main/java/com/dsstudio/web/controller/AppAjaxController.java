package com.dsstudio.web.controller;

import com.dsstudio.web.service.StockJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppAjaxController {

	@Autowired
	StockJsonService stockJsonService;

	@RequestMapping(value = "/api/getJsonFile", method = RequestMethod.GET)
	public String getJsonFile(@RequestParam("stock") String stockName) {
		return stockJsonService.generate(stockName, 1);
	}
}
