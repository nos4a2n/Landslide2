package com.naveed.landslide2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.naveed.landslide2.library.ReportFunctions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ReportListActivity extends ListActivity {

	ReportFunctions reportFunctions;
	ArrayList<String> dataHolder = new ArrayList<String>();
	String recordText = "";
	String details = "";
	String uid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_list);
		reportFunctions = new ReportFunctions();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			uid = extras.getString("uid");
		}

		JSONObject data = reportFunctions.viewReport(uid);
		try {
			String success = data.getString("success");
			if (Integer.parseInt(success) == 1) {
				for (int i = 0; i < data.length() - 3; i++) {
					recordText = "";
					String x = "" + i;
					try {
						JSONObject record = data.getJSONObject(x);
						recordText = "Report = "
								+ record.getString("time").toString();
						dataHolder.add(recordText);
						Log.d("tester", recordText);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				setListAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, dataHolder));
			} else if (data.toString() == "n") {
				Toast.makeText(this, "No records found!", Toast.LENGTH_LONG)
						.show();
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "No records found!", Toast.LENGTH_LONG).show();
			e1.printStackTrace();

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		JSONObject data = reportFunctions.viewReport(uid);
		String x = "" + position;
		try {
			JSONObject record = data.getJSONObject(x);
			Spanned message = Html.fromHtml(record.getString("msg"));
			String msg = message.toString();
			recordText = "Report Details:\nReport ID = "
					+ record.getString("rid").toString() + "\nDate and Time = "
					+ record.getString("time").toString() + "\nLatitude = "
					+ record.getString("lat").toString() + "\nLongitude = "
					+ record.getString("lng").toString() + "\nMessage = "
					+ msg;
			Log.d("tester", recordText);

			AlertDialog.Builder builder = new AlertDialog.Builder(
					ReportListActivity.this);
			builder.setMessage(recordText);
			builder.setCancelable(false);
			builder.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});

			AlertDialog alert = builder.create();
			alert.show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
