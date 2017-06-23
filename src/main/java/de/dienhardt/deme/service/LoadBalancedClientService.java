package de.dienhardt.deme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoadBalancedClientService {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	@Autowired
	private Environment environment;

	protected String serviceUrl;

	@Value("${hallo}")
	public String hallo;

	public LoadBalancedClientService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}
	
	public String getValue() {
		return restTemplate.getForObject(serviceUrl + "/", String.class) + "\n\n" + environment.getProperty("hallo") + "\n\n" + hallo;
	}

}
