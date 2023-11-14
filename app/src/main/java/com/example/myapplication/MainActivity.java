package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.*;


import java.io.IOException;
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
        Button GetWeather = findViewById(R.id.button);

        GetWeather.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                StringBuilder weather = GetWeatherJSON();
                result.setText(weather);
                GetWeather.setVisibility(View.GONE);

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






                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }


            }
        });


    }

    public StringBuilder GetWeatherJSON() {
        TextView result = findViewById(R.id.cityName);
        try {
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
    }



}
