package de.dienhardt.deme.controllers;

import de.dienhardt.deme.service.JsonContentService;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class IndexController {

    private final JsonContentService contentService;

    @Autowired
    public IndexController(@NonNull JsonContentService  contentService) {
        this.contentService = contentService;
    }

    @RequestMapping("/")
    String index(ModelMap model){
        Map page = contentService.getPage();
        model.addAllAttributes(page);
        model.addAttribute("message", "Hello Freaks!");
        return "index";
    }
}
