package com.naveed.landslide2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
	ImageButton sendReportBtn, cameraBtn;
	EditText extraMessage;
	String message = "";
	String uid = "";
	String pathToOurFile = "";
	TextView currLocation, currImage, status;
	ImageView ivPreview;
	String imageFileName = "";
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private static final String FOLDER_NAME = "/IDMS/";
	
	LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		
		makeDirectory();

		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();

		sendReportBtn = (ImageButton) findViewById(R.id.btnSendReport);
		extraMessage = (EditText) findViewById(R.id.textMsg);
		currImage = (TextView) findViewById(R.id.tvPictureName);
		cameraBtn = (ImageButton) findViewById(R.id.btnCamera);
		ivPreview = (ImageView) findViewById(R.id.ivPreview);
		status = (TextView) findViewById(R.id.tvStatus);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			uid = extras.getString("uid");
		}

		currLocation = (TextView) findViewById(R.id.tvCurrent);

		sendReportBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				File f = new File(pathToOurFile);
				if(f.exists()){
					new Thread(new Runnable() {
						public void run() {
							uploadFile();

						}
					}).start();
					
				}
				
				
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
						message, imageFileName) != null) {
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
		});

		cameraBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				char[] chars = "abcdefghijklmnopqrstuvwxyzABSDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
				Random r = new Random(System.currentTimeMillis());
				char[] id = new char[8];
				for (int i = 0;  i < 8;  i++) {
				    id[i] = chars[r.nextInt(chars.length)];
				}
				String uuid =  id.toString();
				imageFileName = JPEG_FILE_PREFIX + timeStamp + "_" + uid + "_" + uuid + JPEG_FILE_SUFFIX;
				File file = new File(Environment.getExternalStorageDirectory()
						.getPath() + FOLDER_NAME + imageFileName);
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, 0);
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		pathToOurFile = Environment.getExternalStorageDirectory()
				.getPath() + FOLDER_NAME + imageFileName;
		File f = new File(pathToOurFile);
		if(f.exists()){
			currImage.setText(imageFileName);
			setPic();
		}
		else{
			imageFileName = "";
		}
		

	}
	
	public void makeDirectory() {
		File dir = new File(Environment.getExternalStorageDirectory()
				+ FOLDER_NAME);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	private void setPic() {

		int targetW = ivPreview.getWidth();
		int targetH = ivPreview.getHeight();

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathToOurFile, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(pathToOurFile, bmOptions);

		ivPreview.setImageBitmap(bitmap);

	}

	protected void uploadFile() {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		// DataInputStream inputStream = null;
		pathToOurFile = Environment.getExternalStorageDirectory().getPath()
				+ FOLDER_NAME + imageFileName;
		String urlServer = "http://idmstest.ueuo.com/upload_api/uploadtoserver.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		Log.d("tester", pathToOurFile);
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ pathToOurFile + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);
			// Responses from the server (code and message)
			@SuppressWarnings("unused")
			int serverResponseCode = connection.getResponseCode();
			@SuppressWarnings("unused")
			String serverResponseMessage = connection.getResponseMessage();
			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage
					+ ": " + serverResponseCode);
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			Log.d("tester", pathToOurFile);
		} catch (Exception ex) {
			// Exception handling
			ex.printStackTrace();

			Log.e("Upload file to server Exception",
					"Exception : " + ex.getMessage(), ex);
		}
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
		Location lastLoc = mLocationClient.getLastLocation();
		String coords = lastLoc.getLatitude() + ", " + lastLoc.getLongitude();
		currLocation.setText(coords);
	}
}
