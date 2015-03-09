package com.fakecompany.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonRetriever extends AsyncTask<String, Void, String>
{
    public WeatherPage linkedPage;

    protected String doInBackground(String... urls)
    {
        try
        {
            URL url = new URL(urls[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json = in.readLine();
            in.close();
            return json;
        }
        catch(IOException e)
        {
            Log.e("WeatherApp", "Connectivity error while retrieving JSON", e);
            return null;
        }
    }

    protected void onPostExecute(String json)
    {
        try
        {
            JSONObject jObject = new JSONObject(json);
            linkedPage.info.cityName = jObject.getString("name");
            linkedPage.info.country = jObject.getJSONObject("sys").getString("country");
            linkedPage.info.sunrise = jObject.getJSONObject("sys").getLong("sunrise");
            linkedPage.info.sunset = jObject.getJSONObject("sys").getLong("sunset");
            linkedPage.info.currentTemp = jObject.getJSONObject("main").getDouble("temp");
            linkedPage.info.minTemp = jObject.getJSONObject("main").getDouble("temp_min");
            linkedPage.info.maxTemp = jObject.getJSONObject("main").getDouble("temp_max");
            linkedPage.info.windSpeed = jObject.getJSONObject("wind").getDouble("speed");
            linkedPage.info.windDirection = jObject.getJSONObject("wind").getDouble("deg");
        }
        catch (JSONException e)
        {
            Log.e("WeatherApp", "Error parsing JSON", e);
        }

        linkedPage.jsonRetrieved = true;

        if (linkedPage.isAdded())
            linkedPage.updatePage();
    }
}