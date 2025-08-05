package com.sayem.openai.embeddings;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sayem.openai.services.OpenAiService;

@Controller
public class SupportTicketHelper {

	@Autowired
	private OpenAiService chatService;

	@GetMapping("/showSupportTicket")
	public String showJobSearchHelper() {
		return "supportTicketSearchHelper";

	}

	@PostMapping("/supportTicketSearchHelper")
	public String jobSearchHelper(@RequestParam String query, Model model) {
		
		List<Document> supportTickets = chatService.searchSupportTicket(query);
		
		model.addAttribute("response", supportTickets);

		return "supportTicketSearchHelper";

	}

}