package com.bravo.bravoclient.async;

import java.util.logging.Logger;

import com.bravo.bravoclient.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

class BasicAsyncTask extends AsyncTask<String, Integer, String>{
	protected Context context;
	protected static String jsonResponse;
	protected static ProgressDialog progressDialog;
	protected static String progressDialogMsg;
	private static Logger logger = Logger.getLogger(BasicAsyncTask.class.getName());
	
	public BasicAsyncTask(Context context, String progressDialogMsg) {
		this.context = context;
		this.jsonResponse = ""; // Reset the login response each single time when this class is called.
		this.progressDialogMsg = progressDialogMsg;
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage(progressDialogMsg + " ......");
		progressDialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer...sec) {
		progressDialog.setProgress(sec[0]);
		if (sec[0] == 0) {
			progressDialog.show();
		} else if (sec[0] == -1) {
			progressDialog.dismiss();
		} else if (sec[0] == Integer.MAX_VALUE) {
			progressDialog.dismiss();
			Toast.makeText(context,  progressDialogMsg + " timeout", Toast.LENGTH_LONG).show();
		}
    }

	protected void postProgress() {
		int sec = 0;
		int timeout = Integer.valueOf(context.getString(R.string.network_timeout));
		while (jsonResponse == null || jsonResponse.equals("")) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.severe("Timer has been stopped");
			}
			publishProgress(sec);
			sec ++;
			if (sec >= timeout) {
				publishProgress(Integer.MAX_VALUE);
				break; // break the loop once timeout
			}
		}
		// if network response is got by api call, post -1 to onProgressUpdate
		if (jsonResponse != null && jsonResponse.equals("") == false) {
			publishProgress(-1);
		}
	}
	
	protected void setJSONResponse(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
	protected String getJSONResponse() {
		return jsonResponse;
	}
}
