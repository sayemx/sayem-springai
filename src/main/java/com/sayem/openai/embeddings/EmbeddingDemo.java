package com.sayem.openai.embeddings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sayem.openai.services.OpenAiService;

@Controller
public class EmbeddingDemo {

	@Autowired
	private OpenAiService chatService;
	
	@GetMapping("/showEmbedding")
	public String showEmbedDemo() {
		return "embedDemo";

	}

	@PostMapping("/embedding")
	public String embed(@RequestParam("text") String text,Model model) {
		
		float[] embeddings = chatService.getEmbedding(text);
		model.addAttribute("response", embeddings);
		
		return "embedDemo";

	}
	

}