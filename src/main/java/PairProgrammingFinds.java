import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PairProgrammingFinds {

    // Lists, Maps and events
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

        // Get the JSON response as a String
        String jsonResponse = response.body();

        // Create an ObjectMapper instance from Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON response into a Map
        Map<String, Object> bankHolidayData = objectMapper.readValue(jsonResponse, Map.class);
        Object englandAndWalesData = bankHolidayData.get("england-and-wales");
        System.out.println(englandAndWalesData);

        // Peters way
        var response2 = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        var objectMapper2 = new ObjectMapper();
        //JSONODE
        var bankHolidayData2 = objectMapper2.readValue(response2.body(), Map.class);
        var englandAndWalesData2 = (Map<?, ?>) bankHolidayData2.get("england-and-wales");
        var events = (List<?>) englandAndWalesData2.get("events");
        //JSONODE
        var today = LocalDate.now();

        var upcomingEvents = new ArrayList<Map<String, Object>>();
        for (var event : events) {
            var title = (String) ((Map<?, ?>) event).get("title");
            var date = LocalDate.parse((String) ((Map<?, ?>) event).get("date"));

            if (date.isBefore(today)) {
                continue;
            }

            var eventDetails = new HashMap<String, Object>();
            eventDetails.put("title", title);
            eventDetails.put("date", date);
            upcomingEvents.add(eventDetails);

            if (upcomingEvents.size() == 5) {
                break;
            }
        }

        // Return or use the upcomingEvents list as needed
        System.out.println(upcomingEvents);

        // My way
//        List<String> nextFiveHolidays = events.stream()
//                .limit(5)  // Limit to the next 5 holidays
//                .map(event -> event.get("date") + ": " + event.get("title"))
//                .collect(Collectors.toList());
//
//        // Print the next five bank holidays
//        nextFiveHolidays.forEach(System.out::println);
    }
}
