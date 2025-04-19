package spooketti.spookettai.AICommunication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPToGemini {
    private final String apiKey = System.getenv("API_KEY");
    private final String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
    HttpClient client = HttpClient.newHttpClient();

    public HTTPToGemini()
    {
//        System.out.println(apiKey);
    }

    public void postToGemini(String content) throws IOException, InterruptedException {
        String jsonPayload = """
            {
                "contents": [{
                    "parts": [{
                        "text": "tell me about this: %s"
                    }]
                }]
            }
            """.formatted(content);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body());

        String text = rootNode
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        System.out.println(text);

    }

}
