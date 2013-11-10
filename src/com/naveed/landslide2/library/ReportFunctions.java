package com.naveed.landslide2.library;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ReportFunctions {

	// { user: "Jess", lat: 3.126547, lng: 101.657825, datetimestamp:
	// now.getTime() }

	JSONParser jsonParser;

	private static String reportUrl = "http://idmstest.ueuo.com/android_report_api/";

	private static String send_report_tag = "send";
	private static String view_report_tag = "view";

	// constructor
	public ReportFunctions() {
		jsonParser = new JSONParser();
	}

	public JSONObject sendReport(String uID, String lat, String lng, String msg) {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String formattedDate = sdf.format(date);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", send_report_tag));
		params.add(new BasicNameValuePair("uid", uID));
		params.add(new BasicNameValuePair("lat", lat));
		params.add(new BasicNameValuePair("lng", lng));
		params.add(new BasicNameValuePair("timestamp", formattedDate));
		params.add(new BasicNameValuePair("msg", msg));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(reportUrl, params);
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

}
