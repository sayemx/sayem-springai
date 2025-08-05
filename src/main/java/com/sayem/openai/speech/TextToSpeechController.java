package com.sayem.openai.speech;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sayem.openai.services.OpenAiService;

@Controller
public class TextToSpeechController {

	@Autowired
	private OpenAiService chatService;

	// Display the image upload form
	@GetMapping("/showTextToSpeech")
	public String showUploadForm() throws IOException {
		return "textToSpeech";
	}

	@GetMapping("/textToSpeech")
	public ResponseEntity<byte[]> streamAudio(@RequestParam String text) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
		httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=output.mp3");
		
		byte[] response = chatService.textToSpeech(text);
		
		return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
	}
}