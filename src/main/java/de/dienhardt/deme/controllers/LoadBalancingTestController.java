package de.dienhardt.deme.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import de.dienhardt.deme.service.LoadBalancedClientService;

@Controller
public class LoadBalancingTestController {

	public static final String SERVICE_URL = "http://clojure1";
	
	@Autowired
	private LoadBalancedClientService clientService;

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@RefreshScope
	public LoadBalancedClientService loadBalancedClientService() {
		return new LoadBalancedClientService(SERVICE_URL);
	}

	@RequestMapping(value = "/clientServiceInfo", produces = "text/plain")
	@ResponseBody
	public String getInfo() {
		return clientService.getValue();
	}

}
