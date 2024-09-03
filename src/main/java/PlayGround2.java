package package0021_atJsonAlias;

import com.fasterxml.jackson.databind.ObjectMapper; // ObjectMapper class
import com.fasterxml.jackson.annotation.*; // @JsonAlias annotation

// de-serialize Json string to Java object

//@JsonAlias allows you to handle field naming mis-matches
//between the Json key-value pairs and the corresponding
//Java object fields during de-serialization (i.e. going
//from Json to Java)

// In this example the "brand" field in the Car object will
// map to "brand", "brandName", "brand_name" or "myBrandName"
// in the Json key-value pairs. The "model" field will map
// to "model" or "type" in the Json key-value pairs.

public class PlayGround2 {

    public static void main(String[] args) throws Exception {
        String jsonString = "{ \"brandName\":\"Honda\", \"type\":\"Accord\", \"color\":\"red\" }";
        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(jsonString, Car.class);
        System.out.println(car.brand);
        System.out.println(car.model);
        System.out.println(car.color);
    }
}

class Car {
    // map this field to the json key-value pair that has
    // "brand", "brandName", "brand_name", or "myBrandName" as its key
    @JsonAlias({ "brandName", "brand_name", "myBrandName" })
    public String brand;

    // map this field to the Json key-value pair that has
    // "model" or "type" as its key
    @JsonAlias({ "type" })
    public String model;

    public String color;
}

