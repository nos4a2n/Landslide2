package com.naveed.landslide2.library;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ReportFunctions {

	JSONParser jsonParser;

	private static String reportUrl = "http://idmstest.ueuo.com/android_report_api/";
	private static String sendUrl = "http://idmstest.ueuo.com/idms-alert.php";
	
	
	
	private static String send_report_tag = "send";
	private static String view_report_tag = "view";
	private static String view_single_tag = "viewSingle";

	// constructor
	public ReportFunctions() {
		jsonParser = new JSONParser();
	}

	public JSONObject sendReport(String uID, String lat, String lng, String msg, String image_name) {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = sdf.format(date);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", send_report_tag));
		params.add(new BasicNameValuePair("uid", uID));
		params.add(new BasicNameValuePair("lat", lat));
		params.add(new BasicNameValuePair("lng", lng));
		params.add(new BasicNameValuePair("timestamp", formattedDate));
		params.add(new BasicNameValuePair("msg", msg));
		params.add(new BasicNameValuePair("image_name", image_name));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(sendUrl, params);
		// return json
		return json;
	}

	public JSONObject viewReport(String uID) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", view_report_tag));
		params.add(new BasicNameValuePair("uid", uID));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(reportUrl, params);
		// return json
		return json;
	}
	
	public JSONObject viewSingleReport(String rID) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", view_single_tag));
		params.add(new BasicNameValuePair("rid", rID));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(reportUrl, params);
		// return json
		return json;
	}

}
