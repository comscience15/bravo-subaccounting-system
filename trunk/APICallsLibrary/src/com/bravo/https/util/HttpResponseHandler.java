package com.bravo.https.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class is going to handle the response from server
 * @author Daniel
 *
 */
public class HttpResponseHandler {
	
	/**
	 * This method can not be consumed for multiple times, can only used once
	 * @param response HttpResponse object from backend server
	 * @param parameterName The name of parameter inside of json-like string response
	 * @return return the value of json parameter
	 */
	public static String parseJson(HttpResponse httpResponse, String parameterName) {
		
		String jsonInput = toString(httpResponse);
		try {
			JSONObject jsonObj = new JSONObject(jsonInput);
			return jsonObj.getString(parameterName);
		} catch (JSONException e) {
			return null;
		}
		
	}
	
	/**
	 * This method is used to parse JSON value from JSON String input
	 * @param JSONString
	 * @param parameterName
	 * @return
	 */
	public static String parseJson(String JSONString, String parameterName) {
		try {
			JSONObject jsonObj = new JSONObject(JSONString);
			return jsonObj.getString(parameterName);
		} catch (JSONException e) {
			return null;
		}
	}
	
	/**
	 * Convert the response to String
	 * @param httpResponse
	 * @return
	 */
	public static String toString(HttpResponse httpResponse) {
		InputStream inputStream = null;
		try {
			inputStream = httpResponse.getEntity().getContent();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		try {
			while((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return sb.toString();
	}

}
