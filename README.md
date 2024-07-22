# Weather Forecaster

Weather Forecaster is a Java-based application that fetches and displays weather data for a given location. The application uses the Open-Meteo API to retrieve weather data, which includes temperature, weather condition, humidity, and wind speed. The GUI is built using Swing and displays the weather information with corresponding images.

## Features

- Fetches real-time weather data using the Open-Meteo API.
- Displays temperature, weather condition, humidity, and wind speed.
- Updates weather condition image based on the fetched data.
- Simple and user-friendly GUI.

## Project Structure
WeatherForecaster/
├── assets/

│ ├── clear.png
│ ├── cloudy.png
│ ├── humidity.png
│ ├── rain.png
│ ├── search.png
│ ├── snow.png
│ ├── windspeed.png


├── lib/
│ ├── json-simple-1.1.1.jar

├── src/
│ ├── Main.java
│ ├── WeatherApp.java
│ ├── WeatherAppGUI.java
├── README.md

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Internet connection to fetch weather data from the API

  ## Credits

- Weather data is fetched from the [Open-Meteo API](https://open-meteo.com/).
- JSON parsing is done using the `json-simple-1.1.1` library.



