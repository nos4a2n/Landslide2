package com.naveed.landslide2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naveed.landslide2.library.UserFunctions;

public class DashboardActivity extends Activity {
	UserFunctions userFunctions;
	Button btnLogout;
	ImageButton btnReport, btnReportList, btnMap;
	String uid = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Dashboard Screen for the application
		 * */
		// Check login status in database
		userFunctions = new UserFunctions();
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			// user already logged in show databoard
			setContentView(R.layout.dashboard);
			btnLogout = (Button) findViewById(R.id.btnLogout);
			btnReport = (ImageButton) findViewById(R.id.btnReport);
			btnReportList = (ImageButton) findViewById(R.id.btnReportList);
			btnMap = (ImageButton) findViewById(R.id.btnMap);

			btnLogout.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					userFunctions.logoutUser(getApplicationContext());
					Intent login = new Intent(getApplicationContext(),
							LoginActivity.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(login);
					// Closing dashboard screen
					finish();
				}
			});

			btnReport.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent report = new Intent(getApplicationContext(),
							ReportActivity.class);
					report.putExtra("uid", uid);
					startActivity(report);
				}
			});

			btnReportList.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent reportList = new Intent(getApplicationContext(),
							ReportListActivity.class);
					reportList.putExtra("uid", uid);
					startActivity(reportList);
				}
			});

			btnMap.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent map = new Intent(getApplicationContext(),
							MapActivity.class);
					startActivity(map);
				}
			});

			// This is where the rest of the stuff go

			String wlcm = "";
			TextView welcome;

			welcome = (TextView) findViewById(R.id.txtWelcome);
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				wlcm = extras.getString("name");
				uid = extras.getString("uid");
			}
			welcome.setText("Welcome " + wlcm);

		} else {
			// user is not logged in show login screen
			Intent login = new Intent(getApplicationContext(),
					LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
		}
	}
}