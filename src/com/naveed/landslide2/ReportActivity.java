package com.naveed.landslide2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.naveed.landslide2.library.ReportFunctions;
import com.naveed.landslide2.library.JSONParser;

public class ReportActivity extends Activity{
	
	ReportFunctions reportFunctions;
	Button viewReportBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		
		viewReportBtn = (Button) findViewById(R.id.button1);
		
		viewReportBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reportFunctions = new ReportFunctions();
				JSONObject json = reportFunctions.viewReport("2");
				try {
					JSONObject jsonReport = json.getJSONObject("1");
					Log.d("testing", jsonReport.getString("uid"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
}
