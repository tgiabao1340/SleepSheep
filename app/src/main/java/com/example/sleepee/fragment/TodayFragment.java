package com.example.sleepee.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androdocs.httprequest.HttpRequest;
import com.example.sleepee.GPSTracker;
import com.example.sleepee.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodayFragment extends Fragment {
    private String API = "b5a75f6145f44beb9e3c6018e670b18d";
    private TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;
    private Button button_reuest_gps;
    private LinearLayout errorDisplay;
    private TextView errorText;
    private RelativeLayout mainCotainer;
    private ProgressBar loader;
    private Location location;

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
            mainCotainer.setVisibility(View.GONE);
            errorDisplay.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... args) {
            String response = "";
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&lang=vi&appid=%s", lat, lon, API);
                response = HttpRequest.excuteGet(url);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = getString(R.string.update_at) + " : " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = getString(R.string.min_temp) + " :\n" + main.getString("temp_min") + "°C";
                String tempMax = getString(R.string.max_temp) + " :\n" + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");
                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");
                String address = jsonObj.getString("name") + ", " + sys.getString("country");
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed + "m/s");
                pressureTxt.setText(pressure + "hpa");
                humidityTxt.setText(humidity + "%");
                loader.setVisibility(View.GONE);
                mainCotainer.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                loader.setVisibility(View.GONE);
                errorDisplay.setVisibility(View.VISIBLE);
                errorText.setVisibility(View.VISIBLE);
                button_reuest_gps.setVisibility(View.VISIBLE);
            }

        }
    }


    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        addressTxt = view.findViewById(R.id.address);
        updated_atTxt = view.findViewById(R.id.updated_at);
        statusTxt = view.findViewById(R.id.status);
        tempTxt = view.findViewById(R.id.temp);
        temp_minTxt = view.findViewById(R.id.temp_min);
        temp_maxTxt = view.findViewById(R.id.temp_max);
        sunriseTxt = view.findViewById(R.id.sunrise);
        sunsetTxt = view.findViewById(R.id.sunset);
        windTxt = view.findViewById(R.id.wind);
        pressureTxt = view.findViewById(R.id.pressure);
        humidityTxt = view.findViewById(R.id.humidity);
        //Status
        loader = view.findViewById(R.id.loader);
        mainCotainer = view.findViewById(R.id.mainContainer);
        errorDisplay = view.findViewById(R.id.errorDisplay);
        errorText = view.findViewById(R.id.errorText);
        button_reuest_gps = view.findViewById(R.id.button_request_gps);
        button_reuest_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadWeatherDisplay();
            }
        });
        GPSTracker gps = new GPSTracker(getActivity());
        location = gps.getLocation();
        new weatherTask().execute();
        return view;
    }

    public void reloadWeatherDisplay() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        GPSTracker gps = new GPSTracker(getActivity());
        location = gps.getLocation();
        new weatherTask().execute();
    }
}

