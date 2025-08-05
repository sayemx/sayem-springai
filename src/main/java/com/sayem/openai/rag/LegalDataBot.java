package com.sayem.openai.rag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sayem.openai.services.OpenAiService;

@Controller
public class LegalDataBot {

	@Autowired
	private OpenAiService chatServcie;

	@GetMapping("/showLegalDataBot")
	public String showProductDataBot() {
		return "legalDataBot";

	}

	@PostMapping("/legalDataBot")
	public String productDataBot(@RequestParam String query, Model model) {
		
		String answer = chatServcie.answer(query);
		model.addAttribute("response", answer);
		return "legalDataBot";

	}

}