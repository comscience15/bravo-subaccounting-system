package com.bravo.https.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.cookie.Cookie;

import android.content.Context;

public class CookieHandler {
	private static Logger logger = Logger.getLogger(CookieHandler.class.getName());
	
	public static void setCookie(List<Cookie> cookies, Context androidContext) throws IOException {
		String cookieValue = cookies.get(0).getName()+ "=" + cookies.get(0).getValue();
		
		logger.log(Level.INFO, "Cookie has been updated, Cookie Value is: " + cookieValue);
		  
		// Created the private local file which contains the cookie from server side
		FileOutputStream cookieFile = androidContext.openFileOutput("Cookie", Context.MODE_PRIVATE);
		    
		cookieFile.write(cookieValue.getBytes());
		cookieFile.close();
	}
	
	public static String getCookie(Context androidContext) throws IOException {
		FileInputStream	getStoredCookie = androidContext.openFileInput("Cookie");
		int cookieSize = getStoredCookie.available();
		byte[] cookieByte = new byte[cookieSize];
		
		logger.log(Level.INFO, "Found cookie with size: " + cookieSize);
		
		getStoredCookie.read(cookieByte);
		
		return new String(cookieByte);
	}
}
