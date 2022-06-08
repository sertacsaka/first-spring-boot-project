package com.developersertac.firstproject;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestSpringBootController {
	 
	@Autowired
	private Environment env;
	
	@RequestMapping("/hello")
	public String Hello() {
		return "Hello World!";
	}
	
	@GetMapping(value = "/tickers")
	public String getBinanceTickers() {
		String uriRoot = env.getProperty("binance.api-root");
		String uriTickers = env.getProperty("binance.price-ticker");
		String uri = uriRoot + uriTickers;
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);
		return result;
	}
}
