package com.example.Research.Analyst;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class ResearchService {

    @Value("${Url}") // Ensure correct key in application.properties (case-sensitive)
    private String url;
    @Value("${Key}")
    private String key;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    // Correct WebClient injection
    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectt) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectt;
    }

 private String buildPrompt(TextRes textRes)  {
        StringBuilder str = new StringBuilder();




        str.append("Summarize the given text.\n");

        str.append(textRes.getContent());
        return str.toString();
    }

    public Mono<String> generate(TextRes textRes) {
        String prompt = buildPrompt(textRes);
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // Make the WebClient call reactively and return Mono<String>
        return webClient.post()
                .uri(url + key)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractAnswer);  // Process the response reactively
    }

    private String extractAnswer(String response) {
        try {
          //  ObjectMapper objectMapper = new ObjectMapper();
   JsonNode rootNode=objectMapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (JsonProcessingException e) {
            return "JSON Processing Error: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected Error: " + e.getMessage();
        }
    }

}

