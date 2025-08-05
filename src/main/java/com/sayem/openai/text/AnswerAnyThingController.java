package com.sayem.openai.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sayem.openai.services.OpenAiService;

@Controller
public class AnswerAnyThingController {

	@Autowired
    private OpenAiService chatService;

    @GetMapping("/showAskAnything")
    public String showAskAnything() {
         return "askAnything";
    }

    @PostMapping("/askAnything")
    public String askAnything(@RequestParam("question") String question, Model model) {
    	
    	String answer = chatService.generateAnswer(question).getResult().getOutput().getText();
    	
    	model.addAttribute("question", question);
    	model.addAttribute("answer", answer);
    	
        return "askAnything";
    }
}