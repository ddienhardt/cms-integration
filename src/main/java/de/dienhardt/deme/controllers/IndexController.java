package de.dienhardt.deme.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

import de.dienhardt.deme.service.JsonContentService;
import lombok.NonNull;

@Controller
public class IndexController {

	private final JsonContentService contentService;

	@Autowired
	private WebApplicationContext applicationContext;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	public IndexController(@NonNull JsonContentService contentService) {
		this.contentService = contentService;
	}

	@RequestMapping("/")
	String index(ModelMap model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> page = contentService.getPage();

		Map<String, Object> pageMap = (Map<String, Object>) page.get("page");
		List<Map<String, Object>> sections = (List<Map<String, Object>>) pageMap.get("sections");
		for (Map<String, Object> section : sections) {
			Object sectionTemplateObject = section.get("sectionTemplate");
			if (sectionTemplateObject != null && sectionTemplateObject instanceof String) {
				String sectionTemplate = (String) sectionTemplateObject;
				if ("dynamic".equals(sectionTemplate)) {
					SpringWebContext ctx = new SpringWebContext(req, resp, applicationContext.getServletContext(), Locale.GERMAN, new ModelMap(), applicationContext);
					handleDynamicSection(section, page, ctx);
				}
			}
		}

		model.addAllAttributes(page);
		model.addAttribute("message", "Hello Freaks!");

		System.out.println(page);

		return "index";
	}

	private void handleDynamicSection(Map<String, Object> section, Map<String, Object> page, IContext ctx) {
		// templateEngine.process("templates/exhibitorTeaser", ctx);

		Object typeObject = section.get("type");
		if (typeObject != null && typeObject instanceof String) {
			String type = (String) typeObject;
			if ("exhibitorTeaser".equals(type)) {
				section.put("markup", "<h3>Exhibitor Teaser component here. Tag: " + section.get("tag") + "</h3>");
			}
		}
	}

}
