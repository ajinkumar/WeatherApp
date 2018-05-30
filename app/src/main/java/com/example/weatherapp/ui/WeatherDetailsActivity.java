package com.example.weatherapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.model.Weather;
import com.example.weatherapp.util.UnitConverter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static com.example.weatherapp.ui.MainActivity.getWindDirectionString;

/**
 * Created by Ajin kumar on 30/5/18.
 */
public class WeatherDetailsActivity extends AppCompatActivity {

    public static Map<String, Integer> speedUnits = new HashMap<>(3);
    public static Map<String, Integer> pressUnits = new HashMap<>(3);
    TextView todayTemperature;
    TextView todayDescription;
    TextView todayWind;
    TextView todayPressure;
    TextView todayHumidity;
    TextView todayIcon;
    private Typeface weatherFont;
    Weather weather;

    public static String localize(SharedPreferences sp, Context context, String preferenceKey, String defaultValueKey) {
        String preferenceValue = sp.getString(preferenceKey, defaultValueKey);
        String result = preferenceValue;
        if ("speedUnit".equals(preferenceKey)) {
            if (speedUnits.containsKey(preferenceValue)) {
                result = context.getString(speedUnits.get(preferenceValue));
            }
        } else if ("pressureUnit".equals(preferenceKey)) {
            if (pressUnits.containsKey(preferenceValue)) {
                result = context.getString(pressUnits.get(preferenceValue));
            }
        }
        return result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_details);
        Intent intent = getIntent();
        weather = intent.getParcelableExtra("weatherData");

        todayTemperature = findViewById(R.id.todayTemperature);
        todayDescription = findViewById(R.id.todayDescription);
        todayWind = findViewById(R.id.todayWind);
        todayPressure = findViewById(R.id.todayPressure);
        todayHumidity = findViewById(R.id.todayHumidity);
        todayIcon = findViewById(R.id.todayIcon);
        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");
        todayIcon.setTypeface(weatherFont);

        updateTodayWeatherUI(weather);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                shareIntent();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareIntent() {
        Intent appIntent = new Intent(Intent.ACTION_SEND);
        appIntent.setType("text/plain");
        todayTemperature = findViewById(R.id.todayTemperature);
        todayDescription = findViewById(R.id.todayDescription);
        todayWind = findViewById(R.id.todayWind);
        todayPressure = findViewById(R.id.todayPressure);
        todayHumidity = findViewById(R.id.todayHumidity);
        String values = "Temperature "+todayTemperature.getText()+
                "\nDescription "+todayDescription.getText()+
                "\n"+todayWind.getText()+
                "\n"+todayPressure.getText()+
                "\n"+todayHumidity.getText();
        appIntent.putExtra(Intent.EXTRA_TEXT, values);
        try {
            startActivity(appIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(WeatherDetailsActivity.this,"Whatsapp have not been installed.",Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateTodayWeatherUI(Weather todayWeather) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WeatherDetailsActivity.this);

        // Temperature
        float temperature = UnitConverter.convertTemperature(Float.parseFloat(todayWeather.getTemperature()), sp);
        if (sp.getBoolean("temperatureInteger", false)) {
            temperature = Math.round(temperature);
        }

        // Rain
        double rain = Double.parseDouble(todayWeather.getRain());
        String rainString = UnitConverter.getRainString(rain, sp);

        // Wind
        double wind;
        try {
            wind = Double.parseDouble(todayWeather.getWind());
        } catch (Exception e) {
            e.printStackTrace();
            wind = 0;
        }
        wind = UnitConverter.convertWind(wind, sp);

        // Pressure
        double pressure = UnitConverter.convertPressure((float) Double.parseDouble(todayWeather.getPressure()), sp);

        todayTemperature.setText(new DecimalFormat("0.#").format(temperature) + " " + sp.getString("unit", "°C"));
        todayDescription.setText(todayWeather.getDescription().substring(0, 1).toUpperCase() +
                todayWeather.getDescription().substring(1) + rainString);
        if (sp.getString("speedUnit", "m/s").equals("bft")) {
            todayWind.setText(getString(R.string.wind) + ": " +
                    UnitConverter.getBeaufortName((int) wind) +
                    (todayWeather.isWindDirectionAvailable() ? " " + getWindDirectionString(sp, this, todayWeather) : ""));
        } else {
            todayWind.setText(getString(R.string.wind) + ": " + new DecimalFormat("0.0").format(wind) + " " +
                    localize(sp, "speedUnit", "m/s") +
                    (todayWeather.isWindDirectionAvailable() ? " " + getWindDirectionString(sp, this, todayWeather) : ""));
        }
        todayPressure.setText(getString(R.string.pressure) + ": " + new DecimalFormat("0.0").format(pressure) + " " +
                localize(sp, "pressureUnit", "hPa"));
        todayHumidity.setText(getString(R.string.humidity) + ": " + todayWeather.getHumidity() + " %");
        todayIcon.setText(todayWeather.getIcon());
    }

    public String localize(SharedPreferences sp, String preferenceKey, String defaultValueKey) {
        return localize(sp, this, preferenceKey, defaultValueKey);
    }

}
