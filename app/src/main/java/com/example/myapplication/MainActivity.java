package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.*;


import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public class dayData
    {
        private int dateDay, dateMonth;
        private int temperatureDay;
        private String isRain;

        public dayData(int dateDay, int dateMonth, int temperatureDay, String isRain) {
            this.dateDay = dateDay;
            this.dateMonth = dateMonth;
            this.temperatureDay = temperatureDay;
            this.isRain = isRain;
        }

        public int getDateDay() {
            return dateDay;
        }

        public int getDateMonth() {
            return dateMonth;
        }

        public int getTemperatureDay() {
            return temperatureDay;
        }

        public String isRain() {
            return isRain;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView result = findViewById(R.id.cityName);
        TextView country = findViewById(R.id.countryName);
        TextView temperature = findViewById(R.id.temperature);

        result.setVisibility(View.GONE);
        country.setVisibility(View.GONE);
        temperature.setVisibility(View.GONE);

        Button GetWeather = findViewById(R.id.button);
        Button PropButton2 = findViewById(R.id.button2);
        Button PropButton3 = findViewById(R.id.button3);
        Button PropButton4 = findViewById(R.id.button4);
        Button PropButton5 = findViewById(R.id.button5);

        PropButton2.setVisibility(View.GONE);
        PropButton3.setVisibility(View.GONE);
        PropButton4.setVisibility(View.GONE);
        PropButton5.setVisibility(View.GONE);

        TextView PropText3 = findViewById(R.id.textView3);
        TextView PropText4 = findViewById(R.id.textView4);
        TextView PropText5 = findViewById(R.id.textView5);

        PropText3.setVisibility(View.GONE);
        PropText4.setVisibility(View.GONE);
        PropText5.setVisibility(View.GONE);

        ImageView weatherIMG1 = findViewById(R.id.cloudy);
        ImageView weatherIMG2 = findViewById(R.id.cloudy2);
        ImageView weatherIMG3 = findViewById(R.id.cloudy3);
        ImageView weatherIMG4 = findViewById(R.id.cloudy4);

        GetWeather.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                result.setVisibility(View.VISIBLE);
                country.setVisibility(View.VISIBLE);
                temperature.setVisibility(View.VISIBLE);

                StringBuilder weather = null;
                try {
                    weather = GetWeatherJSON();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //result.setText(weather);
                //GetWeather.setVisibility(View.GONE);

                dayData[] dayForecast = new dayData[24];

                String jsonString = weather.toString();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode rootNode = objectMapper.readTree(jsonString);
                    JsonNode locationNode;
                    locationNode = rootNode.get("forecast");
                    locationNode = locationNode.get("forecastday");
                    String id = "";
                    for (JsonNode objectNode : locationNode)
                    {
                        objectNode = objectNode.get("hour");
                        int i = 0;
                        for(JsonNode objecttNode : objectNode)
                        {
                            dayForecast[i] = new dayData(0,0,objecttNode.get("temp_c").asInt(),objecttNode.get("condition").get("text").asText());
                            i++;
                        }
                        break; // Since we will only use the first object in "forecastday" node.
                    }
                    GetWeather.setBackground(Drawable.createFromPath("@drawable/custom_button"));
                    //cloudy.setBackgroundResource(R.drawable.cloudy);
                    weatherIMG2.setBackgroundResource(R.drawable.sunny);
                    weatherIMG3.setBackgroundResource(R.drawable.heavyrain);
                    weatherIMG4.setBackgroundResource(R.drawable.lightrain);

                    result.setText(rootNode.get("location").get("name").asText());
                    country.setText(rootNode.get("location").get("country").asText());
                    String tempC = rootNode.get("current").get("temp_c").asText() + "°";
                    temperature.setText(tempC);

                    String currentWeather = rootNode.get("current").get("condition").get("text").asText();
                    switch (currentWeather)
                    {
                        case "Little Drizzle":
                            weatherIMG1.setBackgroundResource(R.drawable.lightrain);
                            break;
                        case "Light Rain":
                            weatherIMG1.setBackgroundResource(R.drawable.lightrain);
                            break;
                        case "Patchy rain possible":
                            weatherIMG1.setBackgroundResource(R.drawable.lightrain);
                            break;
                        case "Overcast":
                            weatherIMG1.setBackgroundResource(R.drawable.cloudy);
                            break;
                        case "Cloudy":
                            weatherIMG1.setBackgroundResource(R.drawable.cloudy);
                            break;
                        case "Partly cloudy":
                            weatherIMG1.setBackgroundResource(R.drawable.cloudy);
                            break;
                        case "Mist":
                            weatherIMG1.setBackgroundResource(R.drawable.cloudy);
                            break;
                        case "Fog":
                            weatherIMG1.setBackgroundResource(R.drawable.cloudy);
                            break;
                        case "Moderate Rain":
                            weatherIMG1.setBackgroundResource(R.drawable.heavyrain);
                            break;
                        case "Moderate or heavy rain shower":
                            weatherIMG1.setBackgroundResource(R.drawable.heavyrain);
                            break;
                        case "Moderate rain at times":
                            weatherIMG1.setBackgroundResource(R.drawable.heavyrain);
                            break;
                        case "Sunny":
                            weatherIMG1.setBackgroundResource(R.drawable.sunny);
                            break;
                        case "Clear":
                            weatherIMG1.setBackgroundResource(R.drawable.sunny);
                            break;

                    }
                    Button mainButton = findViewById(R.id.button2);
                    mainButton.setText(tempC);

                    TextView fDay = findViewById(R.id.textView2);
                    fDay.setText(currentWeather);












                    PropButton2.setVisibility(View.VISIBLE);
                    PropButton3.setVisibility(View.VISIBLE);
                    PropButton4.setVisibility(View.VISIBLE);
                    PropButton5.setVisibility(View.VISIBLE);

                    TextView PropText3 = findViewById(R.id.textView3);
                    TextView PropText4 = findViewById(R.id.textView4);
                    TextView PropText5 = findViewById(R.id.textView5);

                    PropText3.setVisibility(View.VISIBLE);
                    PropText4.setVisibility(View.VISIBLE);
                    PropText5.setVisibility(View.VISIBLE);
                    


                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }


            }
        });


    }

    public StringBuilder GetWeatherJSON() throws IOException {


        InputStream inputStream = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        return stringBuilder;



        /*
        TextView result = findViewById(R.id.cityName);
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.data);
            // 7 Day Forecast: https://api.weatherapi.com/v1/forecast.json?key=c2e873d8dc8b4e7c8e8103152230511&q=London&days=7
            URL url = new URL("https://api.weatherapi.com/v1/forecast.json?key=c2e873d8dc8b4e7c8e8103152230511&q=London");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }

            scanner.close();
            return informationString;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */ // API Call, Since the API expired I will be using the data.json, London weather 11/16/2023 14:42
    }



}
