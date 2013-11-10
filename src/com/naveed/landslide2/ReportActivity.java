package com.naveed.landslide2;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.naveed.landslide2.library.ReportFunctions;
import com.naveed.landslide2.library.JSONParser;

public class ReportActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {

	ReportFunctions reportFunctions;
	ImageButton sendReportBtn;
	EditText extraMessage;
	String message = "";
	String uid = "";

	LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);

		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();

		sendReportBtn = (ImageButton) findViewById(R.id.btnSendReport);
		extraMessage = (EditText) findViewById(R.id.textMsg);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			uid = extras.getString("uid");
		}

		sendReportBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Location lastLoc = mLocationClient.getLastLocation();

				// create LatLng
				double lat = lastLoc.getLatitude();
				double lng = lastLoc.getLongitude();
				DecimalFormat df = new DecimalFormat("###.######");
				String latitude = df.format(lat).toString();
				String longitude = df.format(lng).toString();
				message = extraMessage.getText().toString();
				ReportFunctions reportFunctions = new ReportFunctions();

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ReportActivity.this);
				builder.setMessage("Report has been sent!");
				builder.setCancelable(false);
				builder.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								ReportActivity.this.finish();
							}
						});
				if (reportFunctions.sendReport(uid, latitude, longitude,
						message) != null) {
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
		});

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(30000);
		request.setFastestInterval(0);
		mLocationClient.requestLocationUpdates(request, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// String msg = "Location: " + location.getLatitude() + ", " +
		// location.getLongitude();
		// Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
