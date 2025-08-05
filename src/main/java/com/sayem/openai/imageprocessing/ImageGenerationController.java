package com.sayem.openai.imageprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sayem.openai.services.OpenAiService;

@Controller
public class ImageGenerationController {

	@Autowired
	private OpenAiService chatService;

	@GetMapping("/showImageGenerator")
	public String showImageGenerator() {
		return "imageGenerator";

	}

	@PostMapping("/imageGenerator")
	public String imageGenerator(@RequestParam String prompt, Model model) {
		
		String imageUrl = chatService.generateImage(prompt);
		
		model.addAttribute("response", imageUrl);
		
		return "imageGenerator";

	}

}