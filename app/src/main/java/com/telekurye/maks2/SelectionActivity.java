package com.telekurye.maks2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.telekurye.mobileui.FeedBack;
import com.telekurye.mobileui.R;
import com.telekurye.tools.Info;

public class SelectionActivity extends Activity implements TextWatcher, OnItemClickListener {

	ListView			listView;
	TextView			tvHeader;
	List<Items>			items;
	List<Items>			filterArray		= new ArrayList<Items>();
	ArrayList<Item>		itemsSection	= new ArrayList<Item>();

	EditText			mySearch;
	String				searchString;
	AlertDialog			alert			= null;
	NamesAdapter		objAdapter		= null;

	private int			countyId		= 0;
	private int			level			= 0;
	private TextView	tvEmptyMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);

		tvHeader = (TextView) findViewById(R.id.tv_header);
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		mySearch = (EditText) findViewById(R.id.input_search_query);
		mySearch.addTextChangedListener(this);
		tvEmptyMessage = (TextView) findViewById(R.id.tvEmptyMessage2);

		if (isNetworkAvailable()) {
			new MyTask().execute(EType.City.getId());
		}
		else {
			showToast("Ýnternet Baðlantýsý Bulunamadý!");
			this.finish();
		}
	}

	// My AsyncTask start...

	class MyTask extends AsyncTask<Integer, Void, Void> {

		// ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// pDialog = new ProgressDialog(MainActivity.this);
			// pDialog.setMessage("Yükleniyor...");
			// pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Integer... value) {

			if (value[0] == EType.City.getId()) {
				level = 1;
				items = new MaksLocation().GetCities();
				// try {
				// items = new ShapeTable().getColumn("CityId");
				// }
				// catch (SQLException e) {
				// e.printStackTrace();
				// }
			}
			else if (value[0] == EType.County.getId()) {
				level = 2;
				items = new MaksLocation().GetCounties(value[1]);
			}
			else if (value[0] == EType.District.getId()) {
				level = 3;
				items = new MaksLocation().GetDistricts(value[1]);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// if (null != pDialog && pDialog.isShowing()) {
			// pDialog.dismiss();
			// }

			if (null == items || items.size() == 0) {
				showToast("Veri Bulunamadý!");
				SelectionActivity.this.finish();
			}
			else {

				if (level == 1) {
					tvHeader.setText("ÝL SEÇÝNÝZ");
				}
				else if (level == 2) {
					tvHeader.setText("ÝLÇE SEÇÝNÝZ");
				}
				else if (level == 3) {
					tvHeader.setText("MAHALLE SEÇÝNÝZ");
				}

				setAdapterToListview(items);
			}

			super.onPostExecute(result);
		}
	}

	// Textwatcher's ovveride methods

	@Override
	public void afterTextChanged(Editable s) {
		filterArray.clear();
		searchString = mySearch.getText().toString().trim().replaceAll("\\s", "");

		if (items != null && items.size() > 0 && searchString != null && searchString.length() > 0) {
			for (Items name : items) {
				if (name.getName().toLowerCase().startsWith(searchString.toLowerCase())) {
					filterArray.add(name);
				}
			}
			setAdapterToListview(filterArray);
		}
		else {
			filterArray.clear();
			setAdapterToListview(items);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	// Here Data is Filtered!!!
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	// setAdapter Here....

	public void setAdapterToListview(List<Items> listForAdapter) {

		itemsSection.clear();

		if (null != listForAdapter && listForAdapter.size() != 0) {

			Collections.sort(listForAdapter);

			char checkChar = ' ';

			for (int index = 0; index < listForAdapter.size(); index++) {

				Items objItem = (Items) listForAdapter.get(index);

				char firstChar = objItem.getName().charAt(0);

				if (' ' != checkChar) {
					if (checkChar != firstChar) {
						ItemsSections objSectionItem = new ItemsSections();
						objSectionItem.setSectionLetter(firstChar);
						itemsSection.add(objSectionItem);
					}
				}
				else {
					ItemsSections objSectionItem = new ItemsSections();
					objSectionItem.setSectionLetter(firstChar);
					itemsSection.add(objSectionItem);
				}

				checkChar = firstChar;

				itemsSection.add(objItem);
			}
		}
		else {

			tvEmptyMessage.setText("Sonuç Bulunamadý!");
			listView.setEmptyView(tvEmptyMessage);

			// showAlertView();
		}

		if (null == objAdapter) {
			objAdapter = new NamesAdapter(SelectionActivity.this, itemsSection);
			listView.setAdapter(objAdapter);
		}
		else {
			objAdapter.notifyDataSetChanged();
		}

	}

	// Toast is here...
	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	// OnListClick,Get Name...

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Item item = itemsSection.get(position);

		if (view.getTag().getClass().getSimpleName().equals("ViewHolderName")) {

			mySearch.setText("");

			Items items = (Items) item;

			if (items.getTypeId() == EType.City.getId()) {
				countyId = items.getId();
				Info.CityName = items.getName();
				new MyTask().execute(EType.County.getId(), items.getId());
			}
			else if (items.getTypeId() == EType.County.getId()) {
				Info.CountyName = items.getName();
				new MyTask().execute(EType.District.getId(), items.getId());
			}
			else if (items.getTypeId() == EType.District.getId()) {
				Info.DistrictName = items.getName();
				Info.DISTRICT_ID = Integer.toString(items.getId());

				if (Info.DISTRICT_ID != null && !Info.DISTRICT_ID.equals("")) {
					Intent i1 = new Intent(SelectionActivity.this, FeedBack.class);
					startActivity(i1);
				}

			}

			// showToast(objSchoolname.getName() + " " + objSchoolname.getId());
		}
		else {
			ItemsSections objSectionsName = (ItemsSections) item;
			// showToast("Section :: " +
			// String.valueOf(objSectionsName.getSectionLetter()));
		}

	}

	// Check Internet Connection!!!

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// OnBackPressed...

	@Override
	public void onBackPressed() {

		if (level == 1) {
			AlertDialog alert_back = new AlertDialog.Builder(this).create();
			alert_back.setTitle("Çýkýþ?");
			alert_back.setMessage("Çýkmak Ýstediðinize Emin misiniz?");

			alert_back.setButton("Hayýr", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			alert_back.setButton2("Evet", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					SelectionActivity.this.finish();
				}
			});
			alert_back.show();
		}
		else if (level == 2) {
			new MyTask().execute(EType.City.getId());
		}
		else if (level == 3) {
			new MyTask().execute(EType.County.getId(), countyId);
		}
	}

	// private void showAlertView() {
	//
	// if (null == alert)
	// alert = new AlertDialog.Builder(this).create();
	//
	// if (alert.isShowing()) {
	// return;
	// }
	//
	// alert.setTitle("Not Found!!!");
	// alert.setMessage("Can not find name Like '" + searchString + "'");
	// alert.setButton("Ok", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// alert.show();
	// }
}