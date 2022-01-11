package com.mathx.weatherx;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    ImageView background;
    TextView current_temp_textView;
    EditText editText;
    Button button;
    GridLayout gridLayoutTemp;
    int ButtonHideTime=1500;
    GridLayout gridLayout;
    int weather_id;
    String API_KEY= BuildConfig.API_KEY;
    String weather_description;
    ImageView thermometer;
    String temperature;
    String latitude;
    String longitude;
    TextView errors;
    String city;
    SQLiteDatabase weatherDatabase;

    // Weather Description TextView
    TextView weather;
    TextView aqis;

    // Table Text Views
    TextView dayX;
    TextView day1;
    TextView day2;
    TextView day3;
    TextView day4;
    TextView day5;
    TextView morning1;
    TextView morning2;
    TextView morning3;
    TextView morning4;
    TextView morning5;
    TextView night1;
    TextView night2;
    TextView night3;
    TextView night4;
    TextView night5;


    public void setDailyInvisible(){
        current_temp_textView.setText("-");
        aqis.setVisibility(View.INVISIBLE);
        weather.setVisibility(View.INVISIBLE);
        current_temp_textView.setVisibility(View.INVISIBLE);
        morning1.setVisibility(View.INVISIBLE);
        morning2.setVisibility(View.INVISIBLE);
        morning3.setVisibility(View.INVISIBLE);
        morning4.setVisibility(View.INVISIBLE);
        morning5.setVisibility(View.INVISIBLE);
        night1.setVisibility(View.INVISIBLE);
        night2.setVisibility(View.INVISIBLE);
        night3.setVisibility(View.INVISIBLE);
        night4.setVisibility(View.INVISIBLE);
        night5.setVisibility(View.INVISIBLE);
    }

    public void setDailyVisible(){
        aqis.setVisibility(View.VISIBLE);
        weather.setVisibility(View.VISIBLE);
        current_temp_textView.setVisibility(View.VISIBLE);
        morning1.setVisibility(View.VISIBLE);
        morning2.setVisibility(View.VISIBLE);
        morning3.setVisibility(View.VISIBLE);
        morning4.setVisibility(View.VISIBLE);
        morning5.setVisibility(View.VISIBLE);
        night1.setVisibility(View.VISIBLE);
        night2.setVisibility(View.VISIBLE);
        night3.setVisibility(View.VISIBLE);
        night4.setVisibility(View.VISIBLE);
        night5.setVisibility(View.VISIBLE);
    }

    public void setTextColor(int color){
        editText.setHintTextColor(color);
        editText.setTextColor(color);
        current_temp_textView.setTextColor(color);
        aqis.setTextColor(color);
        weather.setTextColor(color);
        errors.setTextColor(color);

        dayX.setTextColor(color);
        day1.setTextColor(color);
        day2.setTextColor(color);
        day3.setTextColor(color);
        day4.setTextColor(color);
        day5.setTextColor(color);

        morning1.setTextColor(color);
        morning2.setTextColor(color);
        morning3.setTextColor(color);
        morning4.setTextColor(color);
        morning5.setTextColor(color);

        night1.setTextColor(color);
        night2.setTextColor(color);
        night3.setTextColor(color);
        night4.setTextColor(color);
        night5.setTextColor(color);

    }
    public String GetDay(String datex){
        Long lon=Long.parseLong(datex);
        java.util.Date date=new java.util.Date((long)lon*1000);
        return date.toString().substring(0,3).toUpperCase();
    }
    public String GetDate(String datex){
        Long lon=Long.parseLong(datex);
        java.util.Date date=new java.util.Date((long)lon*1000);
        return date.toString();
    }
    public String getCeilTemp(String s){
        String temp=String.valueOf((int)Double.parseDouble(s))+"\u2103";
        return temp;
    }
    public void getResponse(final VolleyCallback callback,String urls){
        RequestQueue requestQueue;
        requestQueue=Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest current_json=new JsonObjectRequest(Request.Method.GET,urls,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        requestQueue.add(current_json);
    }

    public void onClick(View view) throws InterruptedException {

        button.performHapticFeedback(100);
        button.setClickable(false);

        // Set the button unclickable for a certain amount of time- {1.5 seconds}
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setClickable(true);
                    }
                });
            }
        }, ButtonHideTime);

        thermometer.animate().rotationBy(360f).setDuration(200);
        gridLayout.setAlpha(0f);
        gridLayoutTemp.setAlpha(0f);
        setDailyInvisible();
        String current_weather;

        current_weather="https://api.openweathermap.org/data/2.5/weather?q="+editText.getText()+"&units=metric&appid="+ API_KEY;

        Log.i("Firstget",current_weather);

        getResponse(new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                errors.setVisibility(View.INVISIBLE);
                weather_description=new Gson().fromJson(String.valueOf(response), JsonObject.class).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
                weather_id=new Gson().fromJson(String.valueOf(response), JsonObject.class).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsInt();
                longitude=new Gson().fromJson(String.valueOf(response), JsonObject.class).getAsJsonObject().get("coord").getAsJsonObject().get("lon").getAsString();
                latitude=new Gson().fromJson(String.valueOf(response), JsonObject.class).getAsJsonObject().get("coord").getAsJsonObject().get("lat").getAsString();
                temperature=new Gson().fromJson(String.valueOf(response), JsonObject.class).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString();
                int Temp=(int)Double.parseDouble(temperature);
                temperature=String.valueOf(Temp);
                temperature+=" \u2103";
                city=editText.getText().toString();
                Log.i("Temperature",temperature);

                String weekly_weather="https://api.openweathermap.org/data/2.5/onecall?lat="+ latitude +"&lon="+ longitude +"&exclude=hourly,current,minutely&units=metric&appid="+ API_KEY;
                Log.i("Weekly_Weather",weekly_weather);

                getResponse(new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        JsonArray json = new Gson().fromJson(String.valueOf(result), JsonObject.class).getAsJsonObject().get("daily").getAsJsonArray();
                        Vector<String> date = new Vector<>();
                        Vector<String> day = new Vector<>();
                        Vector<String> morning = new Vector<>();
                        Vector<String> night = new Vector<>();

                        for (int i = 0; i <= 5; i++) {
                            if(i==0){
                                String all_seven_days = new Gson().fromJson(String.valueOf(result), JsonObject.class).getAsJsonObject().get("daily").
                                        getAsJsonArray().get(i).getAsJsonObject().get("dt").toString();
                                Log.i("Current",GetDay(all_seven_days));
                                continue;
                            }
                            String all_seven_days = new Gson().fromJson(String.valueOf(result), JsonObject.class).getAsJsonObject().get("daily").
                                    getAsJsonArray().get(i).getAsJsonObject().get("dt").toString();
                            String temp_day=new Gson().fromJson(String.valueOf(result), JsonObject.class).getAsJsonObject().get("daily").
                                    getAsJsonArray().get(i).getAsJsonObject().get("temp").getAsJsonObject().get("day").toString();
                            String temp_night=new Gson().fromJson(String.valueOf(result), JsonObject.class).getAsJsonObject().get("daily").
                                    getAsJsonArray().get(i).getAsJsonObject().get("temp").getAsJsonObject().get("night").toString();
                            morning.add(temp_day);
                            night.add(temp_night);
                            date.add(GetDay(all_seven_days));
                        }
                        weather.setText(weather_description);
                        day1.setText(date.get(0));
                        day2.setText(date.get(1));
                        day3.setText(date.get(2));
                        day4.setText(date.get(3));
                        day5.setText(date.get(4));
                        morning1.setText(getCeilTemp(morning.get(0)));
                        morning2.setText(getCeilTemp(morning.get(1)));
                        morning3.setText(getCeilTemp(morning.get(2)));
                        morning4.setText(getCeilTemp(morning.get(3)));
                        morning5.setText(getCeilTemp(morning.get(4)));
                        night1.setText(getCeilTemp(night.get(0)));
                        night2.setText(getCeilTemp(night.get(1)));
                        night3.setText(getCeilTemp(night.get(2)));
                        night4.setText(getCeilTemp(night.get(3)));
                        night5.setText(getCeilTemp(night.get(4)));

                        if(Temp<=15){
                            thermometer.setImageResource(R.drawable.low_temperature);
                        }
                        else if(Temp>=30){
                            thermometer.setImageResource(R.drawable.high_temperature);
                        }
                        else{
                            thermometer.setImageResource(R.drawable.thermometer);
                        }
                        current_temp_textView.setText(temperature);

                        if (errors.getVisibility() == View.VISIBLE) {
                            errors.setVisibility(View.INVISIBLE);
                        }
                        String air_quality_index="https://api.openweathermap.org/data/2.5/air_pollution?lat="+latitude+"&lon="+longitude+"&appid="+ API_KEY;
                        getResponse(new VolleyCallback() {
                            @Override
                            public void onSuccess(String res) {
                                //gridLayoutTemp.setAlpha(0f);

                                if(weather_id>=200 & weather_id<300){
                                    // thuderstorm
                                    background.setBackgroundResource(R.drawable.thunderstorm);
                                    setTextColor(Color.WHITE);
                                }
                                else if(weather_id>=300 && weather_id<400 ){
                                    // Rain
                                    background.setBackgroundResource(R.drawable.rainy);
                                    setTextColor(Color.WHITE);
                                }
                                else if(weather_id>=500 && weather_id<600){
                                    // Rain
                                    background.setBackgroundResource(R.drawable.rainy);
                                    setTextColor(Color.WHITE);
                                }
                                else if(weather_id>=600 && weather_id<700){
                                    // Snow
                                    background.setBackgroundResource(R.drawable.snowy);
                                    setTextColor(Color.BLACK);
                                }
                                else if(weather_id>=700 && weather_id<800){
                                    // Fog
                                    background.setBackgroundResource(R.drawable.clear_morning);
                                    setTextColor(Color.WHITE);
                                    current_temp_textView.setTextColor(Color.BLACK);

                                }
                                else if(weather_id==800){
                                    // Clear
                                    background.setBackgroundResource(R.drawable.clear_morning);
                                    setTextColor(Color.WHITE);
                                    current_temp_textView.setTextColor(Color.BLACK);
                                }
                                else if(weather_id>800 && weather_id<900){
                                    // Cloudy
                                    background.setBackgroundResource(R.drawable.cloudy);
                                    setTextColor(Color.BLACK);
                                }

                                gridLayoutTemp.animate().rotationXBy(-720f).alpha(1f).setDuration(1000);
                                gridLayout.animate().rotationXBy(-720f).alpha(1f).setDuration(1000);

                                //animate();
                                setDailyVisible();
                                 String aqi = new Gson().fromJson(String.valueOf(res), JsonObject.class).getAsJsonObject().get("list").getAsJsonArray().get(0)
                                         .getAsJsonObject().get("main").getAsJsonObject().get("aqi").getAsString();
                                 aqis.setText("AQI: "+aqi);
                            }

                            @Override
                            public void onError(VolleyError volleyError) {
                                new CheckNetwork(getApplication()).execute();
                                errors.setVisibility(View.VISIBLE);
                                setDailyInvisible();
                                weather.setVisibility(View.INVISIBLE);
                                try{
                                    String errorText=String.valueOf(volleyError.networkResponse.statusCode)+": CITY NOT FOUND ERROR";
                                    Log.i("ErrorText",String.valueOf(volleyError.networkResponse.statusCode));
                                    errors.setText(errorText);
                                }
                                catch (Exception e){
                                    errors.setText("Network Error");
                                }

                            }
                        },air_quality_index);

                    }
                    @Override
                    public void onError(VolleyError volleyError) {
                        new CheckNetwork(getApplication()).execute();
                        errors.setVisibility(View.VISIBLE);
                        setDailyInvisible();
                        weather.setVisibility(View.INVISIBLE);
                        try{
                            String errorText=String.valueOf(volleyError.networkResponse.statusCode)+": CITY NOT FOUND ERROR";
                            Log.i("ErrorText",String.valueOf(volleyError.networkResponse.statusCode));
                            errors.setText(errorText);
                        }
                        catch (Exception e){
                            errors.setText("Network Error");
                        }

                    }
                },weekly_weather);

            }
            @Override
            public void onError(VolleyError volleyError) {
                setDailyInvisible();
                new CheckNetwork(getApplication()).execute();
                thermometer.setImageResource(R.drawable.thermometer);
                weather.setVisibility(View.INVISIBLE);
                errors.setVisibility(View.VISIBLE);

                try{
                    String errorText=String.valueOf(volleyError.networkResponse.statusCode)+": CITY NOT FOUND";
                    Log.i("ErrorText",String.valueOf(volleyError.networkResponse.statusCode));
                    errors.setText(errorText);
                }
                catch (Exception e){
                    errors.setText("Network Error");
                }
            }
        },current_weather);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayoutTemp=findViewById(R.id.gridLayoutTemp);
        dayX=findViewById(R.id.day);
        gridLayout=findViewById(R.id.gridLayout);
        thermometer=findViewById(R.id.thermometer);
        thermometer.setImageResource(R.drawable.thermometer);
        current_temp_textView=findViewById(R.id.current_temp);
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        errors=findViewById(R.id.errors);
        weather=findViewById(R.id.weather_desc);
        aqis=findViewById(R.id.aqi);
        background=findViewById(R.id.background);

        // Default Background
        background.setBackgroundResource(R.drawable.clear_morning);

        // Day textView
        day1=findViewById(R.id.day1);
        day2=findViewById(R.id.day2);
        day3=findViewById(R.id.day3);
        day4=findViewById(R.id.day4);
        day5=findViewById(R.id.day5);

        // Morning textView
        morning1=findViewById(R.id.morning1);
        morning2=findViewById(R.id.morning2);
        morning3=findViewById(R.id.morning3);
        morning4=findViewById(R.id.morning4);
        morning5=findViewById(R.id.morning5);
        // Night textView
        night1=findViewById(R.id.night1);
        night2=findViewById(R.id.night2);
        night3=findViewById(R.id.night3);
        night4=findViewById(R.id.night4);
        night5=findViewById(R.id.night5);

        setTextColor(Color.WHITE);
    }
}