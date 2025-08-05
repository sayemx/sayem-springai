package com.sayem.openai.text.prompttemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sayem.openai.services.OpenAiService;
import com.sayem.openai.text.prompttemplate.dto.CountryCuisine;

@Controller
public class CuisineHelperController {
	@Autowired
    private OpenAiService chatService;

    @GetMapping("/showCuisineHelper")
    public String showChatPage() {
         return "cuisineHelper";
    }

    @PostMapping("/cuisineHelper")
    public String getChatResponse(@RequestParam("country") String country, 
    		@RequestParam("numCuisines") String numCuisines,
    		@RequestParam("language") String language,
    		Model model) {
    	
    	CountryCuisine countryCuisines = chatService.getCuisines(country, numCuisines, language);
    	
    	model.addAttribute("countryCuisines", countryCuisines);
    	
        return "cuisineHelper";
    }
}
