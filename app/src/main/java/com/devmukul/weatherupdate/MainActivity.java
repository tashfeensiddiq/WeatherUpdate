package com.devmukul.weatherupdate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String data;
    List<WeatherData> listData;
    ListView lv;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetDataAsycTask().execute();
        lv = (ListView) findViewById(R.id.weather_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this,"Clicked "+position,Toast.LENGTH_LONG ).show();
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(i);
            }
        });

    }

    public class GetDataAsycTask extends AsyncTask<Void,Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Please Wait...");
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            data = getDataFromUrl("http://api.openweathermap.org/data/2.5/forecast/daily?APPID=e361c6ae553dc2faf9364cd13b1bc038&q=Dhaka&mode=json&units=metric&cnt=7");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            parseData(data);
            MyAdapter adapter = new MyAdapter(MainActivity.this, listData);
            lv.setAdapter(adapter);
            pd.dismiss();
        }
    }

    void parseData(String jsonData){
        try {
            listData =new ArrayList<>();
            JSONObject obj = new JSONObject(jsonData);
            JSONArray arr = obj.getJSONArray("list");
            for(int i=0;i<arr.length();i++){
                JSONObject arrayItem = arr.getJSONObject(i);
                String dt = arrayItem.getString("dt");
                String humidity = arrayItem.getString("humidity");

                String tempData = arrayItem.getString("temp");
                JSONObject tempObj = new JSONObject(tempData);
                String maxTemp = tempObj.getString("max");
                String minTemp = tempObj.getString("min");

                JSONArray temparr = arrayItem.getJSONArray("weather");
                JSONObject weatherObj = temparr.getJSONObject(0);
                String main = weatherObj.getString("main");
                String description = weatherObj.getString("description");
                String icon = weatherObj.getString("icon");

                WeatherData wd = new WeatherData(dt,minTemp,maxTemp,humidity,main,description,icon);
                listData.add(wd);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    String getDataFromUrl(String url){
        String line = "";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL urls = new URL(url);
            connection = (HttpURLConnection) urls.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(stream);
            reader = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                buffer.append(line);

            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return line;
    }
}
