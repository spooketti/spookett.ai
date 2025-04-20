package spooketti.spookettai.AICommunication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HTTPToGemini {
    private final String apiKey = System.getenv("API_KEY");
    private final String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
    HttpClient client = HttpClient.newHttpClient();

    public HTTPToGemini() {
//        System.out.println(apiKey);
    }

    public String sanitizeResponse(String input) {
        return input.codePoints()
                .filter(cp ->
                        (cp >= 0x20 && cp != 0x7F) &&                  // printable ASCII, exclude DEL
                                (cp <= 0xD7FF || cp >= 0xE000) &&              // exclude surrogate halves
                                cp <= 0x10FFFF)        // remove newlines and tabs
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString()
                .replaceAll("\\s{2,}", " ")  // squash multiple spaces
                .trim();                     // remove leading/trailing whitespace
    }

    public boolean isChatForAI(String message) {
        return message.split("\\s+")[0].equalsIgnoreCase("spokti");
    }

    public String chatToGemini(String message) throws IOException, InterruptedException {
        String prompt = """
                keep all sentences lowercase and respond with 3 sentences at a maximum so try to stay low on word count, 
                do not bold italics or any styling to your text, just the letters and numbers, do not include the < or > character
                when saying someones name, dont include new lines or anything similar
                your name is spokti, the username of the person talking to you is written in the format <username> and what comes after is 
                what they have to say to you: you are a minecraft player in their server for context,
                they may ask for your coordinates for example asking 'where are you?', and if they do respond 'only with im at [coordinate]', 
                literally, use [coordinate] dont fill in the blank, when replying to peoples names
                """;
        return sanitizeResponse(postToGemini(prompt, message));
    }

    private String postToGemini(String prompt, String content) throws IOException, InterruptedException {
        String jsonPayload = """
                {
                    "contents": [{
                        "parts": [{
                            "text": "%s: %s"
                        }]
                    }]
                }
                """.formatted(prompt, content);
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
        byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
        String utf8String = new String(utf8Bytes, StandardCharsets.UTF_8);

        System.out.println(utf8String);
        return utf8String;

    }

}
