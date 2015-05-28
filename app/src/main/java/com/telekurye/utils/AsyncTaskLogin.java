package com.telekurye.utils;

import java.lang.reflect.Type;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.telekurye.customlist.SelectionActivity;
import com.telekurye.data.Person;
import com.telekurye.data.typetoken.SyncResult;
import com.telekurye.database.DatabaseHelperMap;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class AsyncTaskLogin extends AsyncTask<Void, String, Boolean> {

	Activity				Act;
	private ProgressDialog	progressDialog;
	public static int		errorStatus	= 0;

	public AsyncTaskLogin(Activity activity) {
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
			progressDialog.setTitle("Lütfen Bekleyiniz...");
			progressDialog.setMessage("Giriþ Yapýlýyor...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.show();
		} catch (Exception e) {
			Tools.saveErrors(e);
			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}

	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {

			publishProgress("Giriþ Yapýlýyor...");

			JsonToDatabase jto = new JsonToDatabase();

			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			HttpRequestForJson httpReq = new HttpRequestForJson(Info.tagLogin, Act);

			String json = httpReq.getJson();
			int HttpResponseCode = httpReq.getResponseCode();

			Type listType = new TypeToken<SyncResult<Person>>() {
			}.getType();
			SyncResult<Person> person = gson.fromJson(json, listType);

			if (HttpResponseCode != 200 || person.getProcessStatus() != 200) {
				return false;
			}

			jto.saveLogin(gson, json);

			publishProgress("Veriler Yükleniyor...");

			DatabaseHelperMap.getDbHelper().copyDataBase();

			return true;

		} catch (Exception e) {

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
		progressDialog.setMessage(values[0]);
	}

	@Override
	protected void onPostExecute(Boolean aVoid) {

		// if (new ProcessStatuses().GetAllData().get(0).getStatusCode() == 200 && aVoid) {
		if (!aVoid) {
			try {
				if ((progressDialog != null) && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			} catch (final IllegalArgumentException e) {
				// Handle or log or ignore
			} catch (final Exception e) {
				// Handle or log or ignore
			} finally {
				progressDialog = null;
			}

			if (errorStatus == 1) {
				Tools.showLongCustomToast(Act, " Sunucuya baðlanýlamadý, hata en kýsa sürede giderilecektir. Lütfen beklemede kalýnýz. ");
			} else {
				Tools.showLongCustomToast(Act, " Giriþ Baþarýsýz! \n\n Kullanýcý Adý veya Þifre Hatalý ");
			}
		} else {
			Intent i1 = new Intent(Act, SelectionActivity.class);
			Act.startActivity(i1);

			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}
	}
}
