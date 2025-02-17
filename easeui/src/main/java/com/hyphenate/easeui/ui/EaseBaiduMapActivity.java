/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hyphenate.easeui.R;

public class EaseBaiduMapActivity extends EaseBaseActivity {

	private final static String TAG = "map";
	FrameLayout mMapViewContainer = null;

	Button sendButton = null;

	EditText indexText = null;
	int index = 0;
	// LocationData locData = null;
	public static EaseBaiduMapActivity instance = null;
	ProgressDialog progressDialog;

	public class BaiduSDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			String st1 = getResources().getString(R.string.Network_error);

		}
	}

	private BaiduSDKReceiver mBaiduReceiver;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;

		sendButton = (Button) findViewById(R.id.btn_location_send);
		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra("latitude", 0);

		IntentFilter iFilter = new IntentFilter();
		mBaiduReceiver = new BaiduSDKReceiver();
		registerReceiver(mBaiduReceiver, iFilter);
	}

	private void showMap(double latitude, double longtitude, String address) {
		sendButton.setVisibility(View.GONE);

	}

	private void showMapWithLocationClient() {
		String str1 = getResources().getString(R.string.Making_sure_your_location);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(str1);

		progressDialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface arg0) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				Log.d("map", "cancel retrieve location");
				finish();
			}
		});

		progressDialog.show();

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onDestroy() {

		unregisterReceiver(mBaiduReceiver);
		super.onDestroy();
	}

	/**
	 * format new location to string and show on screen
	 */


	public void back(View v) {
		finish();
	}

	public void sendLocation(View view) {

		finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}

}
