package com.gzt.wheatherass4;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public double latitude;
    public double longitude;
    public LocationManager locationManager;


    TextView  day1, degree1, day2, degree2, day3, degree3, day4, degree4, day5, day6, day7, day8, degree5, degree6, degree7, degree8,degree, degreemin, degreemax, wind, cntry, mood;
    EditText cityEdit;
    ImageView cloud, p1, p2, p3, p4, p5, p6, p7, p8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cloud = findViewById(R.id.cloud);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.pic5);
        p6 = findViewById(R.id.pic6);
        p7 = findViewById(R.id.pic7);
        p8 = findViewById(R.id.pic8);
        degree = findViewById(R.id.temp);
        degreemin = findViewById(R.id.minTemp);
        degreemax = findViewById(R.id.maxTemp);
        wind = findViewById(R.id.wind);
        cntry = findViewById(R.id.cityCountry);
        mood = findViewById(R.id.status);
        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        day6 = findViewById(R.id.day6);
        day7 = findViewById(R.id.day7);
        day8 = findViewById(R.id.day8);
        degree1 = findViewById(R.id.temp1);
        degree2 = findViewById(R.id.temp2);
        degree3 = findViewById(R.id.temp3);
        degree4 = findViewById(R.id.temp4);
        degree5 = findViewById(R.id.temp5);
        degree6 = findViewById(R.id.temp6);
        degree7 = findViewById(R.id.temp7);
        degree8 = findViewById(R.id.temp8);
        cityEdit = findViewById(R.id.cityEdit);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Toast.makeText(MainActivity.this, latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                    try {
                        forcast();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }


            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

        });
        try{

            forcast();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void forcast() throws ExecutionException, InterruptedException, JSONException {
        String url;
        String url2;

        url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=968b2edc5fb9951b705612c32dece0ab&units=metric";
        url2 = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=968b2edc5fb9951b705612c32dece0ab&units=metric";

        JSONObject object = new JSONObject(new HTTPAsyncTask().execute(url).get());
        this.mood.setText(object.getJSONArray("weather").getJSONObject(0).getString("main"));
        Picasso.get().load("http://openweathermap.org/img/wn/" + object.getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.cloud);
        this.degree.setText(object.getJSONObject("main").getDouble("degree")+"°C");
        this.degreemax.setText(object.getJSONObject("main").getDouble("temp_max") +"°C");
        this.degreemin.setText(object.getJSONObject("main").getDouble("temp_min") +"°C");
        this.wind.setText("Speed: " + object.getJSONObject("wind").getDouble("speed") + " Degree: " + object.getJSONObject("wind").getInt("deg"));
        this.cntry.setText(object.getString("name") + ", " + object.getJSONObject("sys").getString("country"));

        JSONObject object1 = new JSONObject(new HTTPAsyncTask().execute(url2).get());
        int count = 0;
        for (int i = 0; i < 40; i++) {
            if (object1.getJSONArray("list").getJSONObject(i).getString("dt_txt").contains("12:")) {
                if (count == 0) {
                    this.degree1.setText(object1.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("degree") +"°C");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + object1.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.p1);
                    String value = object1.getJSONArray("list").getJSONObject(i).getString("dt_txt").split(" ")[0];
                    String[] value2 = value.split("-");
                    String result = "";
                    for (int j = 0; j < value2.length; j++) {
                        if (j != value2.length - 1)
                            result += value2[j] + "/";
                        else {
                            result += value2[j];
                        }
                    }
                    this.day1.setText(result);
                } else if (count == 1) {
                    this.degree2.setText(object1.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("degree")+"°C");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + object1.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.p2);
                    String value = object1.getJSONArray("list").getJSONObject(i).getString("dt_txt").split(" ")[0];
                    String[] value2 = value.split("-");
                    String result = "";
                    for (int j = 0; j < value2.length; j++) {
                        if (j != value2.length - 1)
                            result += value2[j] + "/";
                        else {
                            result += value2[j];
                        }
                    }
                    this.day2.setText(result);
                } else if (count == 2) {
                    this.degree3.setText(object1.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("degree")+"°C");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + object1.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.p3);
                    String value = object1.getJSONArray("list").getJSONObject(i).getString("dt_txt").split(" ")[0];
                    String[] value2 = value.split("-");
                    String result = "";
                    for (int j = 0; j < value2.length; j++) {
                        if (j != value2.length - 1)
                            result += value2[j] + "/";
                        else {
                            result += value2[j];
                        }
                    }
                    this.day3.setText(result);
                } else if (count == 3) {
                    this.degree4.setText(object1.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("degree") +"°C");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + object1.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.p4);
                    String value = object1.getJSONArray("list").getJSONObject(i).getString("dt_txt").split(" ")[0];
                    String[] value2 = value.split("-");
                    String result = "";
                    for (int j = 0; j < value2.length; j++) {
                        if (j != value2.length - 1)
                            result += value2[j] + "/";
                        else {
                            result += value2[j];
                        }
                    }
                    this.day4.setText(result);
                }
                count++;
            }
        }
    }

    public void forcast2(View view) throws ExecutionException, InterruptedException, JSONException {
        String url;
        String city = String.valueOf(cityEdit.getText());

        if (city.equals("")) {
            Toast.makeText(this, "Please type a city!!", Toast.LENGTH_SHORT).show();
        }

        url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=968b2edc5fb9951b705612c32dece0ab&units=metric";

        JSONObject object = new JSONObject(new HTTPAsyncTask().execute(url).get());
        int count = 0;
        for (int i = 0; i < 40; i++) {
            if (object.getJSONArray("list").getJSONObject(i).getString("dt_txt").contains("12:")) {
                if (count == 0) {
                    this.degree5.setText(object.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("degree")+"°C");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + object.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.p5);
                    String value = object.getJSONArray("list").getJSONObject(i).getString("dt_txt").split(" ")[0];
                    String[] value2 = value.split("-");
                    String result = "";
                    for (int j = 0; j < value2.length; j++) {
                        if (j != value2.length - 1)
                            result += value2[j] + "/";
                        else {
                            result += value2[j];
                        }
                    }
                    this.day5.setText(result);
                } else if (count == 1) {
                    this.degree6.setText(object.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("degree")+"°C");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + object.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.p6);
                    String value = object.getJSONArray("list").getJSONObject(i).getString("dt_txt").split(" ")[0];
                    String[] value2 = value.split("-");
                    String result = "";
                    for (int j = 0; j < value2.length; j++) {
                        if (j != value2.length - 1)
                            result += value2[j] + "/";
                        else {
                            result += value2[j];
                        }
                    }
                    this.day6.setText(result);
                } else if (count == 2) {
                    this.degree7.setText(object.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("degree")+"°C");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + object.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.p7);
                    String value = object.getJSONArray("list").getJSONObject(i).getString("dt_txt").split(" ")[0];
                    String[] value2 = value.split("-");
                    String result = "";
                    for (int j = 0; j < value2.length; j++) {
                        if (j != value2.length - 1)
                            result += value2[j] + "/";
                        else {
                            result += value2[j];
                        }
                    }
                    this.day7.setText(result);
                } else if (count == 3) {
                    this.degree8.setText(object.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("degree")+"°C");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + object.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(this.p8);
                    String value = object.getJSONArray("list").getJSONObject(i).getString("dt_txt").split(" ")[0];
                    String[] value2 = value.split("-");
                    String result = "";
                    for (int j = 0; j < value2.length; j++) {
                        if (j != value2.length - 1)
                            result += value2[j] + "/";
                        else {
                            result += value2[j];
                        }
                    }
                    this.day8.setText(result);
                }
                count++;
            }
        }
    }


    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        public HTTPAsyncTask() {}
        @Override
        protected String doInBackground(String... urls) {
            try {
                return HttpGet(urls[0]);
            } catch(IOException e) {
                return "Unableto to retrive web page";
            }
        }
        private String HttpGet(String myurl) throws IOException {
            InputStream stream = null;
            String result = "";
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.connect();

            stream = conn.getInputStream();

            String line = "";

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            while((line = reader.readLine()) != null) {
                result += line;
            }
            stream.close();
            return result;
        }
    }

}