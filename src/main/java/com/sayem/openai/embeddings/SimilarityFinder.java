package com.sayem.openai.embeddings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sayem.openai.services.OpenAiService;

@Controller
public class SimilarityFinder {

	@Autowired
	private OpenAiService chatService;
	
	@GetMapping("/showSimilarityFinder")
	public String showSimilarityFinder() {
		return "similarityFinder";

	}

	@PostMapping("/similarityFinder")
	public String findSimilarity(@RequestParam("text1") String text1,
								 @RequestParam("text2") String text2,
								 Model model) {
		
		double simEmbedding = chatService.getSimEmbedding(text1, text2);
		model.addAttribute("response", simEmbedding);
		return "similarityFinder";

	}
	

}