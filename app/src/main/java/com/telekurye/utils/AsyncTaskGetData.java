package com.telekurye.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telekurye.mobileui.FeedBack;
import com.telekurye.tools.Tools;

public class AsyncTaskGetData extends AsyncTask<String, String, Boolean> {

	Activity				Act;
	private ProgressDialog	progressDialog;
	public static int		errorStatus	= 0;

	public AsyncTaskGetData(Activity activity) {
		Act = activity;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		try {
			progressDialog = new ProgressDialog(Act);
			// Resources res = Act.getResources();
			// Drawable draw = res.getDrawable(R.drawable.progressbar1);
			// progressDialog.setProgressDrawable(draw);
			// progressDialog.setMax(100);
			// progressDialog.setProgress(0);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setTitle("L�tfen Bekleyiniz...");
			progressDialog.setMessage("Veriler �ndiriliyor...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.show();
		}
		catch (Exception e) {
			Tools.saveErrors(e);
			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}

	}

	@Override
	protected Boolean doInBackground(String... districtid) {

		try {

			JsonToDatabase jto = new JsonToDatabase();

			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			HttpRequestForJson httpReq = new HttpRequestForJson();

			String json = httpReq.getJsonData(districtid[0]);

			jto.saveControlData(gson, json);

			return true;

		}
		catch (Exception e) {

			errorStatus = 1;

			Tools.saveErrors(e);

			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			return false;
		}

	}

	// public void doProgress(String... values) {
	// publishProgress(values);
	// }

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);

	}

	@Override
	protected void onPostExecute(Boolean aVoid) {

		// if (new ProcessStatuses().GetAllData().get(0).getStatusCode() == 200 && aVoid) {
		if (!aVoid) {
			try {
				if ((progressDialog != null) && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
			catch (final IllegalArgumentException e) {
				// Handle or log or ignore
			}
			catch (final Exception e) {
				// Handle or log or ignore
			}
			finally {
				progressDialog = null;
			}

			if (errorStatus == 1) {
				Tools.showLongCustomToast(Act, " Sunucuya ba�lan�lamad�, hata en k�sa s�rede giderilecektir. L�tfen beklemede kal�n�z. ");
			}
			else {
				Tools.showLongCustomToast(Act, " Giri� Ba�ar�s�z! \n\n Kullan�c� Ad� veya �ifre Hatal� ");
			}
		}
		else {
			Intent i1 = new Intent(Act, FeedBack.class);
			Act.startActivity(i1);
			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}
	}

}
