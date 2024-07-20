//retrieve data from API - this backend logic fetches 
//latest weather data from external API and return it
//GUI displays data to user

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {
   

    
    @SuppressWarnings("unchecked")
    public static JSONObject getWeatherData(String locationName) {
        //get location coordinates using geolocation api
        JSONArray locationData = getLocationData(locationName);

        // Check if location data is found
        if (locationData == null || locationData.isEmpty()) {
            System.out.println("Error: No location data found.");
            return null;
        }

        //extract latitude and longitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude"); // Corrected key name
        double longitude = (double) location.get("longitude"); // Corrected key name

        //build api request url with location coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" + 
                           "latitude=" + latitude + 
                           "&longitude=" + longitude + 
                           "&hourly=temperature_2m,relative_humidity_2m,weathercode,wind_speed_10m&timezone=Asia%2FSingapore";  

        try {
            //call api and get response
            HttpURLConnection conn = fetchApiResponse(urlString);
            //check for response status
            //200 - means connection was good
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: API fetching unsuccessful");
                return null;
            } else {
                System.out.println("Response code: " + conn.getResponseCode());
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    //read and store into string builder
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();
                conn.disconnect();

                //parse through data
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObject = (JSONObject) parser.parse(resultJson.toString());

                //retrieving the hourly weather data
                JSONObject hourly = (JSONObject) resultJsonObject.get("hourly");

                //we want to get current hour's data
                //so we need to get index of our current hour (create a method to get current time index)
                JSONArray time = (JSONArray) hourly.get("time");
                int index = findIndexofCurrentTime(time);

                //retrieving temperature data
                JSONArray temp = (JSONArray) hourly.get("temperature_2m");
                double temperature = (double) temp.get(index);

                //retrieving weather code
                JSONArray weathercode = (JSONArray) hourly.get("weathercode"); // Corrected key name
                String weatherCondition = convertWeatherCode((long) weathercode.get(index));

                //retrieving humidity
                JSONArray humid = (JSONArray) hourly.get("relative_humidity_2m");
                long humidity = (long) humid.get(index);
         
                //retrieve windspeed
                JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");
                double windspeed = (double) windspeedData.get(index);

                //now we will create our own JSONObject where we will pass the values that we retrieved so far in our gui
                JSONObject weatherData = new JSONObject();

               
                weatherData.put("temperature", temperature);
                weatherData.put("weather_condition", weatherCondition);
                weatherData.put("humidity", humidity);
                weatherData.put("windspeed", windspeed);
              
                return weatherData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //retrieves geographic coordinates
    public static JSONArray getLocationData(String locationName) {
        // Replace whitespace in location name to + to be in API request format
        locationName = locationName.replaceAll(" ", "+");
    
        // Build API URL with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";
    
        try {
            // Call API to get a response
            HttpURLConnection conn = fetchApiResponse(urlString);
    
            // Check response status
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null; // Return null if connection fails
            } else {
                // Store API result
                System.out.println("Response Code: " + conn.getResponseCode());
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
    
                // Read and store the result JSON data into StringBuilder
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                // Close scanner
                scanner.close();
    
                // Close URL connection
                conn.disconnect();
    
                // Parse JSON string to JSON object
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObject = (JSONObject) parser.parse(resultJson.toString());
    
                // Get list of location data the API generated from location name
                JSONArray locationData = (JSONArray) resultJsonObject.get("results");
                return locationData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            //attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //set request method to GET 
            conn.setRequestMethod("GET");

            //connect to our API
            conn.connect();
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //could not make connection
        return null;
    }

    private static int findIndexofCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();
  
        //iterate through the time list and see which one matches our current time
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                //return index
                return i;
            }
        }

        return 0; // Return 0 if current time is not found
    }

    public static String getCurrentTime() {
        //get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        //format date to be 2024-07-20T00:00 (this is how it is read in api)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00"); // Corrected pattern

        //format and print the current date and time
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }

    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";
        if (weathercode == 0L) {
            weatherCondition = "Clear";
        } else if (weathercode <= 3 && weathercode >= 0L) {
            weatherCondition = "Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 60 && weathercode <= 99L)) {
            weatherCondition = "Rainy";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            weatherCondition = "Snowy";
        }
        return weatherCondition;
    }
}