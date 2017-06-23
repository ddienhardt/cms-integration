package de.dienhardt.deme.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UrlPathHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import de.dienhardt.deme.service.JsonContentService;
import lombok.NonNull;

@Controller
public class IndexController {

	/**
	 * Returs the value as map or null if the actual value is null or of different type.
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> asMap(Map<String, Object> map, String key) {
		Object object = map.get(key);

		try {
			if (object != null && object instanceof Map) {
				return (Map<String, Object>) object;
			}
		} catch (ClassCastException e) {
			System.out.println(e);
			return null;
		}

		return null;
	}

	/**
	 * Returs the value as List of Map<String, Object> or null if the actual value is null or of different type.
	 */
	@SuppressWarnings("unchecked")
	private static List<Map<String, Object>> asMapList(Map<String, Object> map, String key) {
		Object object = map.get(key);

		try {
			if (object != null && object instanceof List) {
				return (List<Map<String, Object>>) object;
			}
		} catch (ClassCastException e) {
			System.out.println(e);
			return null;
		}

		return null;
	}

	private Logger LOGGER = LoggerFactory.getLogger(JsonContentService.class);

	private final JsonContentService contentService;

	@Autowired
	private UrlPathHelper pathHelper;

	@Autowired
	private TemplateEngine templateEngine;

//	@Autowired
//	private DiscoveryClient discoveryClient;

	@Autowired
	public IndexController(@NonNull JsonContentService contentService) {
		this.contentService = contentService;
	}

	@RequestMapping("/content/**")
	public String index(ModelMap model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path = pathHelper.getPathWithinServletMapping(req);

		Map<String, Object> page = contentService.getPage(path);
		if (page == null) {
			return "";
		}

		LOGGER.debug("page: " + page);
		Map<String, Object> pageMap = asMap(page, "page");
		List<Map<String, Object>> sections = asMapList(pageMap, "sections");
		for (Map<String, Object> section : sections) {
			if ("dynamic".equals(section.get("sectionTemplate"))) {
				handleDynamicSection(section, page);
			}
		}

		model.addAllAttributes(page);
		model.addAttribute("message", "Hello Freaks!");

		return "index";
	}

	@RequestMapping(value = "/health", produces = "text/plain")
	@ResponseBody
	public String healthCheck() {
//		LOGGER.info("description: " + discoveryClient.description());
//		for (String string : discoveryClient.getServices()) {
//			LOGGER.info("service: " + string);
//		}
//
//		List<ServiceInstance> instances = discoveryClient.getInstances("clojure1");
//		for (ServiceInstance serviceInstance : instances) {
//			LOGGER.info("instance: " + serviceInstance);
//		}

		return "OK";
	}

	private void handleDynamicSection(Map<String, Object> section, Map<String, Object> page) {
		Object typeObject = section.get("type");
		if (typeObject != null && typeObject instanceof String) {
			String type = (String) typeObject;
			if ("exhibitorTeaser".equals(type)) {
				LOGGER.debug("exhibitorTeaser Tag: " + section.get("tag"));
				// templateEngine.process(templateName, context)
				Context context = new Context(Locale.GERMAN, section);
				for (Entry<String, Object> entry : context.getVariables().entrySet()) {
					LOGGER.debug(entry.getKey() + " - " + entry.getValue());
				}

				context.getVariables().put("section", section);
				section.put("markup", templateEngine.process("/exhibitor/exhibitorTeaser", context));
			}
		}
	}

}
