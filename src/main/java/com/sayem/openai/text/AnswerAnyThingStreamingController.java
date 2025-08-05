package com.sayem.openai.text;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.openai.services.OpenAiService;

import reactor.core.publisher.Flux;

@RestController
public class AnswerAnyThingStreamingController {

	@Autowired
	OpenAiService chatService;

	
	@GetMapping("/stream")
    public Flux<String> askAnything(@RequestParam("message") String message, Model model) {
    	
    	return chatService.generateStreamAnswer(message);
    	
    }
	
	@GetMapping("/stream1")
    public Flux<ChatResponse> askAnything1(@RequestParam("message") String message, Model model) {
    	
    	return chatService.generateStreamAnswer1(message);
    	
    }
}