package com.example.weatherapp;

import android.app.Application;

import com.example.weatherapp.util.ConnectivityReceiver;

/**
 * Created by Ajin kumar on 30/05/18.
 */

public class WeatherApplication extends Application {

	private static WeatherApplication weatherApplication;

	public static synchronized WeatherApplication getInstance() {
		return weatherApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        weatherApplication = this;
	}

	public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
		ConnectivityReceiver.connectivityReceiverListener = listener;
	}
}

