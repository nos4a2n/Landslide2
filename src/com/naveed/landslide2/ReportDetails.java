package com.naveed.landslide2;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.naveed.landslide2.library.ReportFunctions;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ReportDetails extends Activity{
	
	String reportID = "";
	String imageName = "";
	String url = "";
	
	TextView rID, timeStamp, coords, message;
	
	ReportFunctions reportFunctions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_details);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			reportID = extras.getString("rid");
		}
		Log.d("Tester", reportID);
		
		rID = (TextView) findViewById(R.id.tvRID);
		timeStamp = (TextView) findViewById(R.id.tvTime);
		coords = (TextView) findViewById(R.id.tvCoord);
		message = (TextView) findViewById(R.id.tvMsg);
		
		reportFunctions = new ReportFunctions();
		JSONObject details = reportFunctions.viewSingleReport(reportID);
		
		try {
			rID.setText(details.getString("rid").toString());
			timeStamp.setText(details.getString("time").toString());
			coords.setText(details.getString("lat").toString() + ", " + details.getString("lng").toString());
			String msg = details.getString("msg").toString();
			msg = msg.replace("&nbsp;", " ");
			message.setText(msg);
			imageName = details.getString("image").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(imageName != null){
			Log.d("TESTER", "http://idmstest.ueuo.com/upload_api/uploads/" + imageName);
			new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
	        .execute("http://idmstest.ueuo.com/upload_api/uploads/" + imageName);
		}
		
}

			
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	

}
