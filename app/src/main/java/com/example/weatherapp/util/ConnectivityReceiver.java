package com.example.weatherapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.weatherapp.WeatherApplication;


/**
 * Created by Ajin kumar on 30/1/18.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

	public static ConnectivityReceiverListener connectivityReceiverListener;

	public ConnectivityReceiver() {
		super();
	}

	public static boolean isConnected() {
		ConnectivityManager
			cm = (ConnectivityManager) WeatherApplication.getInstance().getApplicationContext()
			.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null
			&& activeNetwork.isConnectedOrConnecting();
	}

	@Override
	public void onReceive(Context context, Intent arg1) {
		ConnectivityManager cm = (ConnectivityManager) context
			.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm. getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
			&& activeNetwork.isConnectedOrConnecting();

		if (connectivityReceiverListener != null) {
			connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
		}
	}

	public interface ConnectivityReceiverListener {
		void onNetworkConnectionChanged(boolean isConnected);
	}
}
