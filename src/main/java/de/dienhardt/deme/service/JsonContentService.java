package de.dienhardt.deme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonContentService {

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPage() {
		URL resource = getClass().getResource("/data/page.json");
		Assert.notNull(resource, resource.toString());
		try {
			return new ObjectMapper().readValue(resource, HashMap.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
