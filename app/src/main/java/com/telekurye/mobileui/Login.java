package com.telekurye.mobileui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;
import com.telekurye.utils.AsyncTaskLogin;

public class Login extends Activity implements OnClickListener {

	public static Context		AppContext			= null;

	EditText					user_name;
	EditText					user_password;
	Button						login_button;
	Button						btnGetPassword;
	ProgressDialog				progressDialog;
	TextView					tvVersion;

	public static final String	PREFS_NAME			= "user_remember_log";
	public static final String	PREF_CHECK_STATUS	= "checkstatus";
	public static boolean		isDatabaseReset		= false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Tools.disableScreenLock(this);

		AppContext = this.getApplicationContext();

		user_name = (EditText) findViewById(R.id.user_name);
		user_password = (EditText) findViewById(R.id.user_password);
		login_button = (Button) findViewById(R.id.login_button);
		btnGetPassword = (Button) findViewById(R.id.BtnSifreIste);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("Versiyon : " + Info.CURRENT_VERSION);

		user_name.setSelection(1);

		login_button.setOnClickListener(this);
		btnGetPassword.setOnClickListener(this);

		user_name.setText("5551111111");
		user_password.setText("1");
		Info.SYNCPERIOD = 1000 * 30;

	}

	@Override
	public void onClick(View v) {

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode != ConnectionResult.SUCCESS) {

			GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000).show();
			}
			Tools.showLongCustomToast(Login.this, "Lütfen Google Play Hizmetlerini güncellemek için Sayýn Ali Bahadýr Kuþ (0533 601 4699) ile iletiþime geçiniz.");
			return;
		}

		if (v.getId() == R.id.login_button) {

			try {

				Info.USERNAME = user_name.getText().toString();
				Info.PASSWORD = user_password.getText().toString();

				Boolean isConnect = Tools.isConnectingToInternet(Login.this);

				loginuser(isConnect);

			} catch (Exception e) {
				Tools.saveErrors(e);
			}

		}

	}

	private void loginuser(Boolean isConnect) throws IOException, MalformedURLException, ClientProtocolException, FileNotFoundException {

		if (isConnect) {

			AsyncTaskLogin asyncTaskLogin = new AsyncTaskLogin(Login.this);
			asyncTaskLogin.execute();
		} else {
			Tools.showShortCustomToast(Login.this, "Ýnternet baðlantýsý bulunamadý!");
		}

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_UP) {

			switch (event.getKeyCode()) {

			case KeyEvent.KEYCODE_HOME:

				return true;
			}
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

}
