package de.dienhardt.deme.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;

import de.dienhardt.deme.service.JsonContentService;
import lombok.NonNull;

@Controller
public class IndexController {

	private final JsonContentService contentService;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	public IndexController(@NonNull JsonContentService contentService) {
		this.contentService = contentService;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		// SpringResourceTemplateResolver automatically integrates with Spring's
		// own resource resolution infrastructure, which is highly recommended.
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(this.applicationContext);
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		// HTML is the default value, added here for the sake of clarity.
		templateResolver.setTemplateMode(StandardTemplateModeHandlers.HTML5.getTemplateModeName());
		// Template cache is true by default. Set to false if you want
		// templates to be automatically updated when modified.
		templateResolver.setCacheable(true);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		// SpringTemplateEngine automatically applies SpringStandardDialect and
		// enables Spring's own MessageSource message resolution mechanisms.
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		return viewResolver;
	}

	@RequestMapping("/")
	String index(ModelMap model, TemplateEngine templateEngine) {
		Map<String, Object> page = contentService.getPage();
		// System.out.println(templateEngine.process("index", ???));

		model.addAllAttributes(page);
		model.addAttribute("message", "Hello Freaks!");
		return "index";
	}

}
