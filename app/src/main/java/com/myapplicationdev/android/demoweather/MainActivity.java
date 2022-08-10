package com.myapplicationdev.android.demoweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ListView lvWeather;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvWeather = findViewById(R.id.lvWeather);
        client = new AsyncHttpClient();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //This line simply creates a new empty Array List of Weather objects with the variable name alWeather
        ArrayList<Weather> alWeather = new ArrayList<Weather>();

        //Remember the AsyncHttpClient object that we created in Section D?
        //The line above will connect it to the URL where the weather data is located.
        //In other words, you will have to change this URL string accordingly depending on what the address is for your data.
        //It will then wait for a response from the URL.
        client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast", new JsonHttpResponseHandler() {

            String area;
            String forecast;


            //This is the onSuccess() listener.
            //Once a response is successfully received by the app from the site, all the code within here with trigger.
            //Notice this listener has a variable named response.
            //This represents the entire JSON Object data that is being received (as shown above)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                    try {
                        //Recall that in Section E, it was mentioned that the data that we are interested in is contained
                        //in the JSON Array named “forecasts”. As you can see from the image,
                        //this array is actually inside another JSON Array named “items”.
                        //Therefore, the line of code above is used to reference this “items” array.
                        JSONArray jsonArrItems = response.getJSONArray("items");
                        //If you look at “items” carefully, you can see that “forecasts” is actually found in the very first JSON Object of “items”.
                        //Therefore, the first line above is used to reference this first object.
                        //The second line is then used to reference the “forecasts” JSON Array.
                        JSONObject firstObj = jsonArrItems.getJSONObject(0);
                        JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");
                        //Now that we have the “forecasts” JSON Array,
                        //the next logical step would be to use a for-loop to go through the entire array,
                        //and get the weather forecast JSON Object that is at each position.
                        for(int i = 0; i < jsonArrForecasts.length(); i++) {
                            JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                            //For each weather forecast JSON Object, we will retrieve the values of area and forecast.
                            //Using these values, we will then create a new Weather object,
                            //and insert it into the Array List alWeather.
                            area = jsonObjForecast.getString("area");
                            forecast = jsonObjForecast.getString("forecast");
                            Weather weather = new Weather(area, forecast);
                            alWeather.add(weather);
                        }
                    }
                    catch(JSONException e){

                    }

                    //POINT X – Code to display List View
                    ArrayAdapter<Weather> aaWeather = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, alWeather);
                    lvWeather.setAdapter(aaWeather);

                }//end onSuccess

        });
    }//end onResume

}