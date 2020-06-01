package com.example.wheatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    Button searchButton;
    TextView result;

    public void search(View view) {

        cityName=findViewById(R.id.cityName);
        searchButton=findViewById(R.id.searchButton);
        result=findViewById(R.id.result);

        String cName=cityName.getText().toString();

        String content;
        Weather weather=new Weather();
        try {
            content=weather.execute("https://openweathermap.org/data/2.5/weather?q="+
                    cName+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();
            Log.i("contentData",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");
            double visibility;
            Log.i("weatherData",weatherData);

            JSONArray array = new JSONArray(weatherData);

            String main="";
            String description="";
            String temperature="";

            for(int i=0 ; i<array.length();i++)
            {
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperature = mainPart.getString("temp");

            visibility = Double.parseDouble(jsonObject.getString("visibility"));
            int visibilityInKilometer = (int) visibility / 1000;

            Log.i("Temperature",temperature);
            Log.i("main",main);
            Log.i("description",description);

            String resultText = "Main :                    "+main +
                    "\n Description :       "+description +
                    "\n Temperature :     "+ temperature +
                    "\n Visibility :             "+ visibilityInKilometer +"K";


            result.setText(resultText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Weather extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... address) {

            try {
                URL url= new URL(address[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";
                char ch;

                while (data != -1)
                {
                    ch = (char)data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
