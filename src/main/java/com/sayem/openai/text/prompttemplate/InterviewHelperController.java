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
public class InterviewHelperController {
	@Autowired
    private OpenAiService chatService;

    @GetMapping("/showInterviewHelper")
    public String showChatPage() {
         return "interviewHelper";
    }

    @PostMapping("/interviewHelper")
    public String getChatResponse(@RequestParam("company") String company, 
    		@RequestParam("jobTitle") String jobTitle,
    		@RequestParam("strength") String strength,
    		@RequestParam("weakness") String weakness,
    		Model model) {
    	
    	String response = chatService.getInterviewGuide(company, jobTitle, strength, weakness);
    	
    	model.addAttribute("response", response);
    	
        return "interviewHelper";
    }
}
