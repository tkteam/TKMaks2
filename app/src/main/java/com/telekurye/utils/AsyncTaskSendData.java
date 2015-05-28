package com.telekurye.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.telekurye.data.MissionControl;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class AsyncTaskSendData extends AsyncTask<MissionControl, String, Boolean> {

	Activity				Act;
	private ProgressDialog	progressDialog;
	public static int		errorStatus	= 0;

	public AsyncTaskSendData(Activity activity) {
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
			progressDialog.setMessage("Bilgiler Gönderiliyor...");
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
	protected Boolean doInBackground(MissionControl... mControl) {

		try {

			String json = SendDistricts(mControl[0]);

			if (json.trim().equals(json)) {
				return true;
			}
			else {
				return false;
			}

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
				Tools.showLongCustomToast(Act, " Sunucuya baðlanýlamadý, hata en kýsa sürede giderilecektir. Lütfen beklemede kalýnýz. ");
			}
			else {
				Tools.showLongCustomToast(Act, " Giriþ Baþarýsýz! \n\n Kullanýcý Adý veya Þifre Hatalý ");
			}
		}
		else {
			App.bus.post(new EBus(200, "Bilgiler Kaydedildi."));

			if ((progressDialog != null) && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Tools.showShortCustomToast(Act, "Bilgiler Gönderildi...");
		}
	}

	public String SendDistricts(MissionControl mControl) {

		String json = "";

		try {

			HttpClient client = new DefaultHttpClient();

			String postURL = Info.DATA_URL + "?username=" + Info.USERNAME + "&password=" + Info.PASSWORD + "&function=SET&UDMId=" //
					+ mControl.getUDMId() + "&typeId=" + mControl.getTypeId() + "&floorCount=" + mControl.getFloorCount() + "&independentSectionCount=" + mControl.getIndependentSectionCount();

			HttpPost post = new HttpPost(postURL);

			// List<NameValuePair> params = new ArrayList<NameValuePair>();
			//
			// params.add(new BasicNameValuePair("function", "SET"));
			// params.add(new BasicNameValuePair("username", Info.USERNAME));
			// params.add(new BasicNameValuePair("password", Info.PASSWORD));
			// params.add(new BasicNameValuePair("i", Info.IMEI));
			//
			// UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			// post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);

			// String line = EntityUtils.toString(responsePOST.getEntity());

			BufferedReader rd = new BufferedReader(new InputStreamReader(responsePOST.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			json = result.toString();
			// System.out.println(result.toString());
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return json;
	}

}
