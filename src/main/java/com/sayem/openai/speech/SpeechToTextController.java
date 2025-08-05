package com.sayem.openai.speech;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sayem.openai.services.OpenAiService;

@Controller
public class SpeechToTextController {

    // Define the folder where images will be saved
    private static final String UPLOAD_DIR = "D:\\Sayem-Workspace\\springai\\image-uploads";
    
    @Autowired
    private OpenAiService chatService;

    // Display the image upload form
    @GetMapping("/showSpeechToText")
    public String showUploadForm() {
        return "speechToText";
    }

    @PostMapping("/speechToText")
    public String uploadImage(String prompt, @RequestParam("file") MultipartFile file, Model model, 
    		RedirectAttributes redirectAttributes) {
    	
    	if(file.isEmpty()) {
    		model.addAttribute("message", "Please select a file to uploa");
    		return "speechToText";
    	}
    	
    	try {
    		
    		Path uploadDir = Paths.get(UPLOAD_DIR);
    		if(Files.notExists(uploadDir)) {
    			Files.createDirectories(uploadDir);
    		}
    		
    		Path path = uploadDir.resolve(file.getOriginalFilename());
    		Files.write(path, file.getBytes(), StandardOpenOption.CREATE);
    		
    		String response = chatService.speechToText(path.toString());
    		
    		model.addAttribute("transcription", response);
    		
    	}catch (IOException e){
    		model.addAttribute("message", "Failed to upload file");
    	}
    	
    	
        return "speechToText";
    }
}