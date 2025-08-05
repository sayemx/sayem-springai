package com.sayem.openai.services;

import java.util.List;
import java.util.Map;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions.Builder;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.sayem.openai.text.prompttemplate.dto.CountryCuisine;

@Service
public class OpenAiService {

	private ChatClient chatClient;
	
	@Autowired
	private EmbeddingModel embeddingModel;
	
	@Autowired
	private VectorStore vectorStore;
	
	@Autowired
	private OpenAiImageModel openAiImageModel;
	
	@Autowired
	OpenAiAudioTranscriptionModel openaiAudioTranscriptionModel;
	
	@Autowired
	OpenAiAudioSpeechModel openaiAudioSpeechModel;
	
	public OpenAiService(ChatClient.Builder builder) {
		
//		chatClient = builder.build();
		
		ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
		
		chatClient = builder.defaultAdvisors(
				MessageChatMemoryAdvisor.builder(chatMemory)
				.build())
				.build();
		
	}
	
	public ChatResponse generateAnswer(String question) {
		return chatClient.prompt(question).call().chatResponse();
	}
	
	public ChatResponse generateAnswerWithRoles(String question) {
		return chatClient.prompt().system("You are an expert, you can answer anything.")
								  .user(question).call().chatResponse();
	}
	
	public String getTravelGuide(String city, String month, String language, String budget) {
		
		PromptTemplate promptTemplate = new PromptTemplate("Welcome to the {city} travel guide!\n"
				+ "If you're visiting in {month}, here's what you can do:\n"
				+ "1. Must-visit attractions.\n"
				+ "2. Local cuisine you must try.\n"
				+ "3. Useful phrases in {language}.\n"
				+ "4. Tips for traveling on a {budget} budget.\n"
				+ "Enjoy your trip!");
		
		Prompt prompt = promptTemplate.create(Map.of("city", city, "month", month, "language", language, "budget", budget));
		
		
		return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
	}

	public CountryCuisine getCuisines(String country, String numCuisines, String language) {
		
		PromptTemplate promptTemplate = new PromptTemplate("You are an expert in traditional cuisines.\n"
				+ "You provide information about a specific dish from a specific\n"
				+ "country.\n"
				+ "Answer the question: What is the traditional cuisine of {country}?\n"
				+ "Avoid giving information about fictional places. If the country is\n"
				+ "fictional\n"
				+ "or non-existent answer: I don't know.");
		
		Prompt prompt = promptTemplate.create(Map.of("country", country, "numCuisines", numCuisines, "language", language));
		return chatClient.prompt(prompt).call().entity(CountryCuisine.class);
	}

	public String getInterviewGuide(String company, String jobTitle, String strength, String weakness) {
		
		PromptTemplate promptTemplate = new PromptTemplate("You are a career coach. Provide tailored interview tips for the\n"
				+ "position of {position} at {company}.\n"
				+ "Highlight your strengths in {strengths} and prepare for questions\n"
				+ "about your weaknesses such as {weaknesses}.");
		
		Prompt prompt = promptTemplate.create(Map.of("position", jobTitle, 
													 "company", company, 
													 "strengths", strength, 
													 "weaknesses", weakness));
		
		return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
	}
	
	public float[] getEmbedding(String text){
		float[] embed = embeddingModel.embed(text);
		
		return embed;
	}
	
	public double getSimEmbedding(String text1, String text2) {
		List<float[]> embed = embeddingModel.embed(List.of(text1, text2));
		
		return cosineSimilarity(embed.get(0), embed.get(1));
	}
	
	private double cosineSimilarity(float[] vectorA, float[] vectorB) {
		if (vectorA.length != vectorB.length) {
			throw new IllegalArgumentException("Vectors must be of the same length");
		}

		// Initialize variables for dot product and magnitudes
		double dotProduct = 0.0;
		double magnitudeA = 0.0;
		double magnitudeB = 0.0;

		// Calculate dot product and magnitudes
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			magnitudeA += vectorA[i] * vectorA[i];
			magnitudeB += vectorB[i] * vectorB[i];
		}

		// Calculate and return cosine similarity
		return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));
	}
	
	
	public List<Document> searchJobs(String query){
		return vectorStore.similaritySearch(query);
	}

	public List<Document> searchSupportTicket(String query) {
		return vectorStore.similaritySearch(query);
	}
	
	public String answer(String query) {
		return chatClient.prompt(query).advisors(
				new QuestionAnswerAdvisor(vectorStore))
				.call().content();
	}
	
	public String generateImage(String prompt) {
		
		ImageResponse response = openAiImageModel.call(new ImagePrompt(prompt, OpenAiImageOptions
				.builder()
				.width(1024)
				.height(1024)
				.N(1)
				.build()));
		
		return response.getResult().getOutput().getUrl();
	}

	public String explainImage(String prompt, String path) {
		
		String explanation = chatClient.prompt().user(u -> u.text(prompt)
				.media(MimeTypeUtils.IMAGE_JPEG, new FileSystemResource(path)))
				.call()
				.content();
		
		return explanation;
	}

	public String decideDiet(String prompt, String path1, String path2) {
		
		String suggestion = chatClient.prompt().user(u -> u.text(prompt)
				.media(MimeTypeUtils.IMAGE_JPEG, new FileSystemResource(path1))
				.media(MimeTypeUtils.IMAGE_JPEG, new FileSystemResource(path2)))
				.call()
				.content();
		
		return suggestion;
	}

	public String speechToText(String path) {
		
		OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder().language("en").build();
		AudioTranscriptionPrompt audioTranscriptionPrompt = new AudioTranscriptionPrompt(new FileSystemResource(path), options);
		String output = openaiAudioTranscriptionModel.call(audioTranscriptionPrompt).getResult().getOutput();
		
		ChatResponse response = generateAnswer("Create a quiz of 10 questions which will have 4 options to select, also give answer, use the below paragraph\n" + output);
		
		return response.getResult().getOutput().getText();
	}
	
	public byte[] textToSpeech(String text) {
		return openaiAudioSpeechModel.call(text);
	}
	
}
