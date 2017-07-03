package de.dienhardt.deme.loadbalancing;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.core.env.Environment;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;

public class LocalFirstLoadBalancingRule extends RoundRobinRule {

	private Logger LOGGER = LoggerFactory.getLogger(LocalFirstLoadBalancingRule.class);

	@Autowired
	private InetUtils inet;

	@Autowired
	private Environment env;
	
	private String ipAddress;
	
	@PostConstruct
	private void init() {
		ipAddress = env.getProperty("ip.addr");
		if (ipAddress == null || ipAddress.isEmpty()) {
			ipAddress = inet.findFirstNonLoopbackHostInfo().getIpAddress();
		}
	}

	public LocalFirstLoadBalancingRule() {
		super();
	}

	public LocalFirstLoadBalancingRule(ILoadBalancer lb) {
		super(lb);
	}

	@Override
	public Server choose(Object key) {
		LOGGER.info("key: " + key);
		System.out.println("key: " + key);
		Server localInstance = getFirstLocalInstance("clojure1");
		if(localInstance != null) {
			LOGGER.info("found local instance: {} - isAlive: {} - isReadyToServe {}", localInstance, localInstance.isAlive(), localInstance.isReadyToServe());
			return localInstance;
		}
		return super.choose(key);
	}

	public Server getFirstLocalInstance(String serviceId) {
		ILoadBalancer lb = getLoadBalancer();
		List<Server> instances = lb.getReachableServers();
		LOGGER.debug("local ip addr: " + ipAddress);
		for (Server server : instances) {
			LOGGER.debug("found instance {} on {}", server.getId(), server.getHost());
			if (server.getHost().equals(ipAddress)) {
				return server;
			}
		}
		return null;
	}
}
