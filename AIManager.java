import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIManager {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "phi3";
    private HttpClient client;

    public AIManager() {
        this.client = HttpClient.newHttpClient();
    }

    public String generateNarration(String promptContext) {
        try {
            // We tell the AI explicitly to use simple, easy words
            String simplePrompt = "Describe this RPG room in 2 short, simple sentences using very easy English words: " + promptContext;

            String jsonPayload = String.format(
                "{\"model\": \"%s\", \"prompt\": \"%s\", \"stream\": false}",
                MODEL_NAME,
                simplePrompt.replace("\"", "\\\"").replace("\n", " ")
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String narration = parseOllamaResponse(response.body());
                speakText(narration); // Read simple text out loud
                return narration;
            } else {
                return "[AI Narrator is offline. Using classic text.]";
            }
        } catch (Exception e) {
            return "[AI Server not detected. Ensure Ollama is running.]";
        }
    }

    private String parseOllamaResponse(String jsonResponse) {
        try {
            int responseKeyIndex = jsonResponse.indexOf("\"response\":\"");
            if (responseKeyIndex != -1) {
                int startIndex = responseKeyIndex + 12;
                int endIndex = jsonResponse.indexOf("\",\"done\":", startIndex);
                if (endIndex == -1) {
                    endIndex = jsonResponse.indexOf("\",\"done_reason\":", startIndex);
                }
                if (endIndex == -1) {
                    endIndex = jsonResponse.lastIndexOf("\"}");
                }
                
                if (endIndex != -1 && endIndex > startIndex) {
                    String extracted = jsonResponse.substring(startIndex, endIndex);
                    return extracted.replace("\\n", " ").replace("\\\"", "\"");
                }
            }
        } catch (Exception e) {
            // fallback
        }
        return "The mists of the dungeon swirl, hiding the details.";
    }

    private void speakText(String text) {
        new Thread(() -> {
            try {
                String cleanText = text.replace("'", "").replace("\"", "").replace("\n", " ");
                String psCommand = "Add-Type -AssemblyName System.Speech; " +
                                   "$synth = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                                   "$synth.Speak('" + cleanText + "');";
                
                ProcessBuilder pb = new ProcessBuilder("powershell", "-Command", psCommand);
                pb.start();
            } catch (Exception e) {
                // Speech playback skipped on failure
            }
        }).start();
    }
}
