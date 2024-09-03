import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;


// Peter Approach

public class CustomClassesApproach {

    // Define the BankHolidayData and its nested classes inside this class
    public static class BankHolidayData {
        @JsonProperty("england-and-wales")
        public Region englandAndWales;

        public static class Region {
            public List<Event> events;
        }

        public static class Event {
            public String title;
            public LocalDate date;
        }
    }

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

        // Parse the JSON response into the custom classes
        BankHolidayData bankHolidayData = objectMapper.readValue(response.body(), BankHolidayData.class);

        var today = LocalDate.now();
        var upcomingEvents = new ArrayList<BankHolidayData.Event>();

        for (BankHolidayData.Event event : bankHolidayData.englandAndWales.events) {
            if (event.date.isBefore(today)) {
                continue;
            }
            upcomingEvents.add(event);

            if (upcomingEvents.size() == 5) {
                break;
            }
        }

        // Print the upcoming events
        upcomingEvents.forEach(event -> System.out.println(event.title + ": " + event.date));
    }
}


