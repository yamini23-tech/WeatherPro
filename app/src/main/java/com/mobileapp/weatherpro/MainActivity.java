package com.mobileapp.weatherpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.mobileapp.weatherpro.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements WeatherTask.WeatherTaskListener {

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ActivityMainBinding mActivityMainBinding;
    private String mCountry = Constants.City.MONTREAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mActivityMainBinding.drawerLayout, R.string.Open, R.string.Close);
        mActivityMainBinding.drawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.round_drawable));

        mActivityMainBinding.nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.c_india:
                        mCountry = Constants.City.INDIA;
                        break;
                    case R.id.c_london:
                        mCountry = Constants.City.LONDON;
                        break;
                    case R.id.c_snfrnc:
                        mCountry = Constants.City.SAN_FRAN;
                        break;
                    case R.id.c_dubai:
                        mCountry = Constants.City.DUBAI;
                        break;
                    case R.id.c_moscow:
                        mCountry = Constants.City.MOSCOW;
                        break;
                    case R.id.c_montreal:
                        mCountry = Constants.City.MONTREAL;
                        break;
                    default:
                        return true;
                }
                requestCountry(mCountry);
                mActivityMainBinding.drawerLayout.closeDrawers();
                return true;

            }
        });
        requestCountry(mCountry);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    public void requestCountry(String country) {
        WeatherTask task = new WeatherTask(this, MainActivity.this);
        task.execute("https://www.metaweather.com/api/location/" + country);
    }

    @Override
    public void onDataReceived(String json) {
        List<WeatherInfo> list = parseData(json);
        refreshUI(list);
    }

    private List<WeatherInfo> parseData(String json) {
        WeatherInfo info = null;
        List<WeatherInfo> list = null;

        try {
            list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            String weatherInfo = jsonObject.getString("consolidated_weather");
            JSONArray arr = new JSONArray(weatherInfo);

            for (int i = 0; i < arr.length(); i++) {
                info = new WeatherInfo();
                JSONObject object = arr.getJSONObject(i);
                info.setWeatherStateName(object.getString("weather_state_name"));
                info.setWeatherStateAbbr(object.getString("weather_state_abbr"));
                info.setDate(object.getString("applicable_date"));
                info.setMinTemp(object.getString("min_temp"));
                info.setActTemp(object.getString("the_temp"));
                info.setMaxTemp(object.getString("max_temp"));
                info.setHumidity(object.getString("humidity"));
                info.setPredictability(object.getString("predictability"));
                info.setCityName(jsonObject.getString("title"));
                list.add(info);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void refreshUI(final List<WeatherInfo> list) {
        mActivityMainBinding.content.cityName.setText(list.get(0).getCityName());
        mActivityMainBinding.content.valMinTemp.setText(String.format("%.2f", Float.valueOf(list.get(0).getMinTemp())));
        mActivityMainBinding.content.valMaxTemp.setText(String.format("%.2f", Float.valueOf(list.get(0).getMaxTemp())));
        mActivityMainBinding.content.valActualTemp.setText(String.format("%.2f", Float.valueOf(list.get(0).getActTemp())));
        mActivityMainBinding.content.humidity.setText(String.format(getString(R.string.humidity), list.get(0).getHumidity())+"%");
        mActivityMainBinding.content.predictability.setText(String.format(getString(R.string.predictability), list.get(0).getPredictability())+"%");
        mActivityMainBinding.content.ivIcon1.setImageResource(getImageDrawable(list.get(0).getWeatherStateAbbr()));
        mActivityMainBinding.content.ivIcon2.setImageResource(getImageDrawable(list.get(1).getWeatherStateAbbr()));
        mActivityMainBinding.content.ivIcon3.setImageResource(getImageDrawable(list.get(2).getWeatherStateAbbr()));
        mActivityMainBinding.content.ivIcon4.setImageResource(getImageDrawable(list.get(3).getWeatherStateAbbr()));
        mActivityMainBinding.content.ivIcon5.setImageResource(getImageDrawable(list.get(4).getWeatherStateAbbr()));
        mActivityMainBinding.content.ivIcon6.setImageResource(getImageDrawable(list.get(5).getWeatherStateAbbr()));
        mActivityMainBinding.content.tvDesc1.setText(String.format(getString(R.string.test), list.get(1).getWeatherStateName(), getDayString(list.get(1).getDate())));
        mActivityMainBinding.content.tvDesc2.setText(String.format(getString(R.string.test), list.get(2).getWeatherStateName(), getDayString(list.get(2).getDate())));
        mActivityMainBinding.content.tvDesc3.setText(String.format(getString(R.string.test), list.get(3).getWeatherStateName(), getDayString(list.get(3).getDate())));
        mActivityMainBinding.content.tvDesc4.setText(String.format(getString(R.string.test), list.get(4).getWeatherStateName(), getDayString(list.get(4).getDate())));
        mActivityMainBinding.content.tvDesc5.setText(String.format(getString(R.string.test), list.get(5).getWeatherStateName(), getDayString(list.get(5).getDate())));



        //set listener


        mActivityMainBinding.content.tvDesc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getDayString(list.get(1).getDate()).equals("Wed")) {

                    passAnIntent(1,list);

                }
            }
        });

        mActivityMainBinding.content.tvDesc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getDayString(list.get(2).getDate()).equals("Thu")) {

                    passAnIntent(2,list);

                }
            }
        });

        mActivityMainBinding.content.tvDesc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getDayString(list.get(3).getDate()).equals("Fri")) {

                    passAnIntent(3,list);

                }
            }
        });

        mActivityMainBinding.content.tvDesc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getDayString(list.get(4).getDate()).equals("Sat")) {

                    passAnIntent(4,list);

                }
            }
        });

        mActivityMainBinding.content.tvDesc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getDayString(list.get(5).getDate()).equals("Sun")) {

                    passAnIntent(5,list);

                }            }
        });




    }

    private void passAnIntent(int i, List<WeatherInfo> list) {

        String minTemp = String.format("%.2f", Float.valueOf(list.get(i).getMinTemp()));
        String maxTemp = String.format("%.2f", Float.valueOf(list.get(i).getMaxTemp()));
        String actTemp = String.format("%.2f", Float.valueOf(list.get(i).getActTemp()));
        String cityName = list.get(i).getCityName();
        Intent intent = new Intent(MainActivity.this,SubActivity.class);
        intent = new Intent(MainActivity.this, SubActivity.class);
        intent.putExtra("minTemp", minTemp);
        intent.putExtra("maxTemp", maxTemp);
        intent.putExtra("actTemp", actTemp);
        intent.putExtra("cityName", cityName);
        startActivity(intent);

    }

    @Override
    public void onDataError() {

    }

    private String getDayString(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date1 = simpleDateFormat.parse(date);
            calendar.setTimeInMillis(date1.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT_FORMAT, Locale.getDefault());
    }

    private int getImageDrawable(String abbr) {
        int resId;
        switch (abbr) {
            case Constants.WeatherStateAbbr.CLEAR:
                resId = R.drawable.ic_clear;
                break;
            case Constants.WeatherStateAbbr.HAIL:
                resId = R.drawable.ic_hail;
                break;
            case Constants.WeatherStateAbbr.HEAVY_CLOUD:
                resId = R.drawable.ic_heavycloud;
                break;
            case Constants.WeatherStateAbbr.HEAVY_RAIN:
                resId = R.drawable.ic_heavyrain;
                break;
            case Constants.WeatherStateAbbr.LIGHT_CLOUD:
                resId = R.drawable.ic_lightcloud;
                break;
            case Constants.WeatherStateAbbr.LIGHT_RAIN:
                resId = R.drawable.ic_lightrain;
                break;
            case Constants.WeatherStateAbbr.SHOWERS:
                resId = R.drawable.ic_showers;
                break;
            case Constants.WeatherStateAbbr.SLEET:
                resId = R.drawable.ic_sleet;
                break;
            case Constants.WeatherStateAbbr.SNOW:
                resId = R.drawable.ic_snow;
                break;
            case Constants.WeatherStateAbbr.THUNDERSTORM:
                resId = R.drawable.ic_thunderstorm;
                break;
            default:
                resId = R.drawable.ic_clear;
                break;
        }
        return resId;
    }
}