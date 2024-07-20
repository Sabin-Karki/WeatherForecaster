import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

public class WeatherAppGUI extends JFrame {

    private JSONObject weatherData;

    WeatherAppGUI() {
        super("Weather Forecaster");

        // set size of gui
        setSize(470, 600);

        // set whether the gui can be resized or not
        setResizable(false);

        // load gui to center
        setLocationRelativeTo(null);

        // operation performed when closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(null);

        addGUIcomponents();
    }

    public void addGUIcomponents() {
        // textfield
        JTextField tf1 = new JTextField(60);
        tf1.setBounds(6, 10, 380, 31);
        tf1.setFont(new Font("Dialog", Font.PLAIN, 23));
        add(tf1);

        // Weather condition image
        JLabel conditionImage = new JLabel(loadImage("Assets/cloudy.png"));
        conditionImage.setBounds(28, 110, 390, 220);
        conditionImage.setHorizontalAlignment(SwingConstants.CENTER);
        add(conditionImage);

        // temperature text
        JLabel tempText = new JLabel("10 °C");
        tempText.setBounds(10, 320, 400, 59);
        tempText.setFont(new Font("Dialog", Font.BOLD, 48));
        tempText.setHorizontalAlignment(SwingConstants.CENTER);
        add(tempText);

        // weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(-10, 380, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // humidity image
        JLabel humidityImage = new JLabel(loadImage("Assets/humidity.png"));
        humidityImage.setBounds(15, 480, 74, 64);
        add(humidityImage);

        // humidity text
        JLabel humidityTest = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityTest.setBounds(90, 480, 84, 54);
        humidityTest.setFont(new Font("Dialog", Font.PLAIN, 15));
        add(humidityTest);

        // wind speed image
        JLabel windImg = new JLabel(loadImage("Assets/windspeed.png"));
        windImg.setBounds(220, 480, 81, 65);
        add(windImg);

        // windspeed text
        JLabel windTest = new JLabel("<html><b>Windspeed</b><br> 15 km/h</html>");
        windTest.setBounds(304, 480, 98, 55);
        windTest.setFont(new Font("Dialog", Font.PLAIN, 18));
        add(windTest);

        // Search button 
        JButton searchLogo = new JButton(loadImage("Assets/search.png"));
        searchLogo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchLogo.setBounds(390, 7, 50, 34);
        searchLogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get location from user
                String userInput = tf1.getText();

                // validate input - remove white space to ensure non-empty text
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return; // Exit if input is empty or whitespace
                }

                // retrieve weather data
                weatherData = WeatherApp.getWeatherData(userInput);

                // Check if weatherData is null
                if (weatherData == null) {
                    System.out.println("Error: No weather data found.");
                    return; // Exit if no data is retrieved
                }

                // update GUI

                // update Weather image
                String weatherCondition = (String) weatherData.get("weather_condition");

                // depending on the condition, we will update the weather image that corresponds with the condition
                switch (weatherCondition) {
                    case "Clear":
                        conditionImage.setIcon(loadImage("Assets/clear.png"));
                        break;
                    case "Cloudy":
                        conditionImage.setIcon(loadImage("Assets/cloudy.png"));
                        break;
                    case "Rainy":
                        conditionImage.setIcon(loadImage("Assets/rain.png"));
                        break;
                    case "Snowy":
                        conditionImage.setIcon(loadImage("Assets/snow.png"));
                        break;
                    default:
                        conditionImage.setIcon(loadImage("Assets/default.png")); // Fallback image
                        break;
                }

                // update temperature text
                double temperature = (double) weatherData.get("temperature");
                tempText.setText(temperature + " °C");

                // update weather condition text
                weatherConditionDesc.setText(weatherCondition);

                // update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityTest.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                // update windspeed text
                double windspeed = (double) weatherData.get("windspeed");
                windTest.setText("<html><b>Windspeed</b> " + windspeed + " km/h</html>");
            }
        });
        add(searchLogo);
    }

    // used to create image in gui 
    private ImageIcon loadImage(String resourcePath) {
        try {
            // read image file from path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // returns image icon so our gui can render
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }
}
