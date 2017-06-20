package de.dienhardt.deme.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonContentService {

	private ObjectMapper mapper = new ObjectMapper();

	private boolean isPreview = false;
	
	private Logger LOGGER = LoggerFactory.getLogger(JsonContentService.class);

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPage(String path) {
		LOGGER.info("got request for path " + path);
		if ("/".equals(path)) {
			path = "/page";
		}

		URL resource = getResource(path);
		if (resource == null || resource.toString() == null) {
			return null;
		}
		try {
			return mapper.readValue(resource, HashMap.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public JsonNode getPageAsJsonNode(String path) {
		LOGGER.info("got request for path " + path);
		if ("/".equals(path)) {
			path = "/index";
		}
		
		URL resource = getResource(path);
		if (resource == null || resource.toString() == null) {
			return null;
		}
		try {
			return mapper.readValue(resource, JsonNode.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private URL getResource(String path) {
		if (isPreview) {
			// get from /opt/firstpirit
			try {
				return new File(String.format("/opt/firstspirit5/web/%s", path)).toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			// get from classpath
			return getClass().getResource(String.format("/data/%s.json", path));
		}
		return null;
	}

}
