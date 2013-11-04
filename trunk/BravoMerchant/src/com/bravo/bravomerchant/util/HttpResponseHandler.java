package com.bravo.bravomerchant.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is going to handle the response from server
 * @author Daniel
 *
 */
public class HttpResponseHandler {
	
	/**
	 * @param response HttpResponse object from backend server
	 * @param parameterName The name of parameter inside of json-like string response
	 * @return return the value of json parameter
	 */
	public String parseJson(HttpResponse httpResponse, String parameterName) {
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
		
		String jsonInput = sb.toString();
		try {
			JSONObject jsonObj = new JSONObject(jsonInput);
			return jsonObj.getString(parameterName);
		} catch (JSONException e) {
			return null;
		}
		
	}

}
