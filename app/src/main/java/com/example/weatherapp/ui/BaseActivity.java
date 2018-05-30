package com.example.weatherapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.weatherapp.util.ConnectivityReceiver;
import com.example.weatherapp.WeatherApplication;

/**
 * Created by Ajin kumar on 28/12/17.
 */

public class BaseActivity extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener {

	protected Activity mActivity;
	protected String TAG;
	protected String userKey;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = this;

		initIntentExtra();
		initDataBinding();
		initializeView();
	}

	protected void initIntentExtra() {

	}

	protected void initializeView() {
	}

	protected void initDataBinding() {

	}

	private void checkConnection() {
		boolean isConnected = ConnectivityReceiver.isConnected();
	}

	@Override
	protected void onResume() {
		super.onResume();
		WeatherApplication.getInstance().setConnectivityListener(BaseActivity.this);
	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if (!isConnected) {
			Toast.makeText(mActivity,"Sorry! Not connected to internet", Toast.LENGTH_LONG).show();
		}
	}
}

