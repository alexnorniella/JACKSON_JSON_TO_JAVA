import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonNodeApproach {

    public static void main(String[] args) throws Exception {

        // Create the HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Build the HttpRequest
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://www.gov.uk/bank-holidays.json"))
                .GET()
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        // Create an ObjectMapper instance from Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON response into a Map<String, Object> using TypeReference
        Map<String, Object> bankHolidayData = objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});

        // Now you can proceed as before
        Map<String, Object> englandAndWalesData = (Map<String, Object>) bankHolidayData.get("england-and-wales");
        List<Map<String, Object>> events = (List<Map<String, Object>>) englandAndWalesData.get("events");

        var today = LocalDate.now();
        var upcomingEvents = new ArrayList<Map<String, Object>>();

        for (var event : events) {
            var title = (String) event.get("title");
            var date = LocalDate.parse((String) event.get("date"));

            if (date.isBefore(today)) {
                continue;
            }

            upcomingEvents.add(Map.of("title", title, "date", date));

            if (upcomingEvents.size() == 5) {
                break;
            }
        }

        System.out.println(upcomingEvents);
    }
}

