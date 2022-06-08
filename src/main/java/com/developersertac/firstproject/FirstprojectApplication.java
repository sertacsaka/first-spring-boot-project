package com.developersertac.firstproject;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication
public class FirstprojectApplication {
	 
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(FirstprojectApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(BinanceKlineRepository repository) {
		return args -> {
			List<BinanceKline> klines = getBinanceKlines("BTCUSDT", "1d");

			repository.deleteAll();
			repository.insert(klines);
		};
	}

	public List<BinanceKline> getBinanceKlines(String symbol, String interval) throws JsonParseException, IOException {
		List<BinanceKline> klines = null;
		
		String uriRoot = env.getProperty("binance.api-root");
		String uriPrice = env.getProperty("binance.klines");
		String uri = uriRoot + uriPrice + "?symbol=" + symbol + "&interval=" + interval;
		
		RestTemplate restTemplate = new RestTemplate();
		String json = restTemplate.getForObject(uri, String.class);
		
		ObjectMapper ob = new ObjectMapper();
		SimpleModule module = new SimpleModule("CustomBinanceKindleDeserializer", new Version(1, 0, 0, null, null, null));
		module.addDeserializer(BinanceKline.class, new CustomBinanceKindleDeserializer());
		ob.registerModule(module);
		klines = (List<BinanceKline>) ob.readValue(json, new TypeReference<List<BinanceKline>>(){});
		
		return klines;
	}
}
