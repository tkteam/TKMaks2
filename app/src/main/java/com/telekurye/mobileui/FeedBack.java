package com.telekurye.mobileui;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.squareup.otto.Subscribe;
import com.telekurye.data.MissionControl;
import com.telekurye.data.Person;
import com.telekurye.data_send.VisitFeedBack;
import com.telekurye.maphelpers.PolyUtil;
import com.telekurye.tools.Info;
import com.telekurye.tools.LiveData;
import com.telekurye.tools.Tools;
import com.telekurye.utils.App;
import com.telekurye.utils.CameraHelper;
import com.telekurye.utils.CustomListAdapter;
import com.telekurye.utils.EBus;
import com.telekurye.utils.PhotoInfo;

public class FeedBack extends Activity implements android.location.GpsStatus.Listener, OnCameraChangeListener, OnMarkerDragListener, LocationListener, OnMapClickListener, OnMapLongClickListener,
		OnMarkerClickListener, OnClickListener {

	public static boolean					isCameraLock			= false;

	// --------- GUI -------
	private TextView						tv_info_welcome, tv_networkStatus, tv_info_time, tv_info_battery, tv_info_accuracy;

	private ProgressDialog					progressDialog;
	private TextView						tvEmptyMessage;

	// ------- MISSION FEEDBACK --------
	private double							GPSLat;
	private double							GPSLng;
	private double							GPSAlt;
	private double							GPSBearing;
	private double							GPSSpeed;
	private Date							GPSTime;

	// ------- KML & SHAPE --------
	private Boolean							isPolygonSelected		= false;
	private List<com.telekurye.kml.Polygon>	polygons;

	// private List<Polygon> polList = new ArrayList<Polygon>();
	// private List<Polyline> polylineList = new ArrayList<Polyline>();
	private Polygon							currentPolygon			= null;
	// private List<com.telekurye.kml.Polygon> BuildingShapeByDistrictIdList =
	// new ArrayList<com.telekurye.kml.Polygon>();
	// private List<com.telekurye.kml.Polygon> StreetShapeByStreetIdList = new
	// ArrayList<com.telekurye.kml.Polygon>();

	// ------ GOOGLE MAP -------
	private final int						RQS_GooglePlayServices	= 1;
	private GoogleMap						myMap;
	private LatLng							currentLocation;
	// private Marker mPositionMarker;
	// private Marker currentMarker;
	private Boolean							isMarkerSet				= false;
	private MapFragment						map;

	// ----- GPS -----
	protected LocationManager				locationManager;
	private long							mLastLocationMillis;
	private Location						mLastLocation;
	private Location						tempLocation			= null;
	private boolean							isGPSFix				= false;
	private float							UserAccuracy			= 5000;

	// ----------------------------------

	private CameraHelper					tp;

	private UiSettings						uiSettings;
	private Thread							uiUpdateThread;

	private ListView						listView;
	private CustomListAdapter				adapter;
	private int								basarShapeId;
	private List<MissionControl>			greenShapeList;
	private com.telekurye.kml.Polygon		selectedPolygon			= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		Tools.disableScreenLock(this);
		App.bus.register(this);
		// ---------- Components ------------

		tv_info_welcome = (TextView) findViewById(R.id.tv_info_name_surname);
		tv_info_time = (TextView) findViewById(R.id.tv_info_time);
		tv_info_battery = (TextView) findViewById(R.id.tv_info_battery);
		tv_info_accuracy = (TextView) findViewById(R.id.tv_info_accuracy);
		tv_networkStatus = (TextView) findViewById(R.id.tv_info_internet);

		tvEmptyMessage = (TextView) findViewById(R.id.tvEmptyMessage);

		listView = (ListView) findViewById(R.id.lv_control_list);
		// MissionListCreator mlc = new MissionListCreator(this, grupid,
		// childid, streettype);
		// mMissionForFeedback = mlc.getMissionList(true);
		// ms = mlc.getThisStreet();

		Person person = new Person();
		String namesurname = person.GetById(Info.UserId).getName() + " " + person.GetById(Info.UserId).getSurname();

		// tv_info_welcome.setTextSize(namesurname.length() < 30 ? 18f : 13f);

		tv_info_welcome.setText("Hoþgeldiniz : " + namesurname);
		// tv_info_version.setText("Versiyon : " + Info.CURRENT_VERSION);
		// tv_info_accuracy.setBackgroundColor(Color.RED);
		tv_info_accuracy.setText("Gps Yok  ");
		// tv_earnings.setText("Bilgi Yok");

		Boolean hasMapCreated = showMapOnActivity();

		if (!hasMapCreated) {
			Toast.makeText(this, "Lütfen Google Play Hizmetlerini güncellemek üzere Ali Bahadýr Kuþ ile iletiþime geçiniz.", Toast.LENGTH_LONG).show();
			return;
		}

		uiSettings = myMap.getUiSettings();

		// if (!Info.ISTEST) {
		//
		// uiSettings.setAllGesturesEnabled(true);
		// uiSettings.setZoomControlsEnabled(false);
		// uiSettings.setCompassEnabled(true);
		// uiSettings.setIndoorLevelPickerEnabled(true);
		// uiSettings.setMyLocationButtonEnabled(true);
		// uiSettings.setMapToolbarEnabled(false);
		//
		// u.setRotateGesturesEnabled(true);
		// u.setScrollGesturesEnabled(false);
		// u.setTiltGesturesEnabled(false);
		// u.setZoomGesturesEnabled(false);
		//
		// } else {
		// // myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
		// // LatLng(41.0756, 28.9744), Info.MAP_ZOOM_LEVEL));
		// myMap.animateCamera(CameraUpdateFactory.zoomTo(Info.MAP_ZOOM_LEVEL));
		// }

		// uiSettings.setAllGesturesEnabled(true); // *-*
		// uiSettings.setZoomControlsEnabled(true);// *-*
		// myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
		// LatLng(41.0756, 28.9744), Info.MAP_ZOOM_LEVEL));// *-*

		myMap.animateCamera(CameraUpdateFactory.zoomTo(Info.MAP_ZOOM_LEVEL));

		// tv_street_name.setText("<-- " +
		// mMissionForFeedback.get(MissionCounter).getName() + " SOKAK -->");

		LoadShapes();

		adapter = new CustomListAdapter(this, new ArrayList<MissionControl>());
		adapter.clear();
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);

		tvEmptyMessage.setText("Lütfen Þekillere Týklayýnýz!");
		listView.setEmptyView(tvEmptyMessage);

	}

	// ################################### BUTTON CLICK
	// ##############################################

	@Override
	public void onClick(View v) {

	}

	// ################################### GOOGLE MAP
	// ##############################################

	@Override
	public boolean onMarkerClick(Marker marker) {

		return false;
	}

	@Override
	public void onMapLongClick(LatLng point) {

	}

	@Override
	public void onMarkerDrag(Marker marker) {

	}

	@Override
	public void onMarkerDragEnd(Marker marker) {

		double SignedLat = marker.getPosition().latitude;
		double SignedLng = marker.getPosition().longitude;

	}

	@Override
	public void onMarkerDragStart(Marker marker) {

	}

	@Override
	public void onMapClick(LatLng point) {
		if (!isShape(point)) {
			return;
		}

		// if (!Info.ISTEST) {
		// if (LiveData.photoinfo.size() > 0) {
		// Tools.showShortCustomToast(this, "Fotoðraf çekildikten sonra konum güncellenemez!");
		// return;
		// }
		// } else {
		// UserAccuracy = 1;
		// }

		// if (UserAccuracy > Info.GPS_ACCURACY) {
		// Tools.showShortCustomToast(this, "GPS deðeri çok büyük!");
		// return;
		// }

		if (currentPolygon != null) {
			currentPolygon.remove();
			currentPolygon = null;
		}

		// if (currentMarker != null) {
		// currentMarker.remove();
		// currentMarker = null;
		// }

		showYellowShape(selectedPolygon);

		List<MissionControl> mList = new MissionControl().GetMissionsByShapeId(basarShapeId);
		adapter = new CustomListAdapter(this, mList);
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);

		if (mList.size() < 1) {
			tvEmptyMessage.setText("Veri Yok");
			listView.setEmptyView(tvEmptyMessage);
		}

		// MarkerOptions mo = new MarkerOptions();
		// mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		//
		// mo.position(point);
		// currentMarker = myMap.addMarker(mo);

		isMarkerSet = true;
	}

	private boolean isShape(LatLng point) {

		try {
			for (com.telekurye.kml.Polygon pol : polygons) {

				if (PolyUtil.containsLocation(point, pol.coors, true) && pol.type == 2) {

					basarShapeId = pol.polygonid.intValue();
					selectedPolygon = pol;

					return true;

				}
			}
		} catch (Exception e) {
			Tools.saveErrors(e);
		}

		// List<MissionControl> mList = new
		// MissionControl().GetMissionsByShapeId(basarShapeId);
		adapter = new CustomListAdapter(this, new ArrayList<MissionControl>());
		adapter.clear();
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);

		tvEmptyMessage.setText("Lütfen Þekillere Týklayýnýz!");
		listView.setEmptyView(tvEmptyMessage);

		return false;
	}

	private void showYellowShape(com.telekurye.kml.Polygon pol) {

		final PolygonOptions polygonOptions = new PolygonOptions();
		String[] coors = SplitUsingTokenizer(pol.coordinates, "||");
		for (String string : coors) {
			String[] coor = string.split(",");
			final LatLng p = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));
			polygonOptions.add(p);
		}

		polygonOptions.strokeColor(Color.YELLOW);
		polygonOptions.fillColor(0x80FFF700);
		polygonOptions.strokeWidth(3);

		currentPolygon = myMap.addPolygon(polygonOptions);

	}

	private void saveAsGreenShape(com.telekurye.kml.Polygon pol) {

		final PolygonOptions polygonOptions = new PolygonOptions();
		String[] coors = SplitUsingTokenizer(pol.coordinates, "||");
		for (String string : coors) {
			String[] coor = string.split(",");
			final LatLng p = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));
			polygonOptions.add(p);
		}

		polygonOptions.strokeColor(Color.GREEN);
		polygonOptions.fillColor(0x802EFE64);
		polygonOptions.strokeWidth(3);

		currentPolygon = myMap.addPolygon(polygonOptions);

	}

	private Boolean showMapOnActivity() {

		boolean isGPSEnabled = false;
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (isGPSEnabled) {

			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Info.GPS_UPDATE_TIME, Info.GPS_MIN_DISTANCE, this);
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			locationManager.addGpsStatusListener(this);

		}

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {

			if (map == null)
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

			if (myMap == null)
				myMap = map.getMap();
			myMap.clear();
			LiveData.streetMarkers.clear();

			myMap.setMyLocationEnabled(true);
			myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			myMap.setOnMapClickListener(this);
			myMap.setOnMarkerClickListener(this);
			myMap.setOnMarkerDragListener(this);
			myMap.setOnCameraChangeListener(this);

		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000).show();
			}
			return false;
		}
		return true;
	}

	private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
		int intersectCount = 0;
		for (int j = 0; j < vertices.size() - 1; j++) {
			if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
				intersectCount++;
			}
		}

		return ((intersectCount % 2) == 1);
	}

	private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

		double aY = vertA.latitude;
		double bY = vertB.latitude;
		double aX = vertA.longitude;
		double bX = vertB.longitude;
		double pY = tap.latitude;
		double pX = tap.longitude;

		if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
			return false;
		}

		double m = (aY - bY) / (aX - bX);
		double bee = (-aX) * m + aY;
		double x = (pY - bee) / m;

		return x > pX;
	}

	// ######################## CAMERA #############################

	private void takePhoto() {

		try {
			if (LiveData.photoinfo.size() < Info.PHOTO_COUNT) {
				tp = new CameraHelper(FeedBack.this);
				startActivityForResult(tp.startCamera(), tp.getRequestCode());
			} else {
				Tools.showShortCustomToast(FeedBack.this, "Fotoðraf ekleme limiti doldu!");
			}
		} catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	private void selectImage(final PhotoInfo pInfo) {
		final CharSequence[] items = { "Fotoðrafý Sil", "Vazgeç" };

		AlertDialog.Builder builder = new AlertDialog.Builder(FeedBack.this);
		builder.setTitle("Düzenle");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				try {
					if (items[item].equals("Fotoðrafý Sil")) {

						// llImages.removeView(pInfo.getImageview());
						// pInfo.getImageview().setImageResource(android.R.color.transparent);
						// photoStatus1 = false;
						File file = new File(Environment.getExternalStorageDirectory() + File.separator + Info.PHOTO_STORAGE_PATH + File.separator + pInfo.getName());
						file.delete();
						LiveData.photoinfo.remove(pInfo);
					}

					else if (items[item].equals("Vazgeç")) {
						dialog.dismiss();
					}
				} catch (Exception e) {
					Tools.saveErrors(e);

				}

			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			if (LiveData.photoinfo.size() < Info.PHOTO_COUNT && tp != null && resultCode == Activity.RESULT_OK) {
				ImageView iv = new ImageView(this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
				iv.setLayoutParams(layoutParams);
				// tp = new CameraHelper(FeedBack.this);
				final PhotoInfo info = tp.showImage(iv, 1, requestCode, resultCode);
				// llImages.addView(iv);

				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						selectImage(info);
					}
				});

				LiveData.photoinfo.add(info);

			}
		} catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	// ######################## GPS LOCATION
	// ###########################################

	@Override
	public void onLocationChanged(Location location) {

		if (!isCameraLock) {
			isCameraLock = true;
		} else {
			return;
		}

		currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

		// try {
		// if (mPositionMarker != null) {
		// mPositionMarker.remove();
		// mPositionMarker = null;
		// mPositionMarker = myMap.addMarker(getMarkerOptions(currentLocation));
		// }
		// else {
		// mPositionMarker = myMap.addMarker(getMarkerOptions(currentLocation));
		// }
		// }
		// catch (Exception e) {
		// Tools.saveErrors(e);
		// }

		// StatusBarValues();

		mLastLocationMillis = SystemClock.elapsedRealtime();
		mLastLocation = location;

		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, Info.MAP_ZOOM_LEVEL), 1000, new CancelableCallback() {

			@Override
			public void onFinish() {
				Projection projection = myMap.getProjection();
				android.graphics.Point point = projection.toScreenLocation(currentLocation);
				LatLng offsetPosition = projection.fromScreenLocation(point);
				myMap.animateCamera(CameraUpdateFactory.newLatLng(offsetPosition), 300, null);
			}

			@Override
			public void onCancel() {
			}
		});

		GPSLat = location.getLatitude();
		GPSLng = location.getLongitude();
		GPSAlt = location.getAltitude();

		GPSBearing = (double) location.getBearing();
		GPSSpeed = (double) location.getSpeed();
		GPSTime = new Date(location.getTime());

		UserAccuracy = location.getAccuracy();

		// try {
		// if (mPositionMarker == null) {
		// mPositionMarker = myMap.addMarker(getMarkerOptions(currentLocation));
		//
		// }
		// else {
		// mPositionMarker.setPosition(currentLocation);
		// }
		// }
		// catch (Exception e) {
		// Tools.saveErrors(e);
		// }

		// if (tempLocation == null || (tempLocation.getLatitude() != location.getLatitude() && tempLocation.getLongitude() != location.getLongitude())) {
		// try {
		// VisitFeedBack mVisitFeedBack = new VisitFeedBack();
		// mVisitFeedBack.setCourierId(Info.UserId);
		// mVisitFeedBack.setCreateDate(new Date());
		// mVisitFeedBack.setDeviceId(Integer.parseInt(Info.IMEI));
		// // mVisitFeedBack.setGPSAccuricy(UserAccuracy);
		// mVisitFeedBack.setGPSLat(GPSLat);
		// mVisitFeedBack.setGPSLng(GPSLng);
		// mVisitFeedBack.setGPSAltitude(GPSAlt);
		// mVisitFeedBack.setGPSBearing(GPSBearing);
		// mVisitFeedBack.setGPSSpeed(GPSSpeed);
		// mVisitFeedBack.setGPSTime(GPSTime);
		// mVisitFeedBack.setUserDailyMissionId(LiveData.userDailyMissionId);
		// mVisitFeedBack.setSimCardNo(Tools.getSimCardNumber(FeedBack.this));
		// mVisitFeedBack.Insert();
		// } catch (Exception e) {
		// Tools.saveErrors(e);
		// }
		//
		// tempLocation = location;
		// }

	}

	@Override
	public void onCameraChange(CameraPosition position) {

	}

	public void LoadShapes() {

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}

		greenShapeList = new MissionControl().getGreenShapeList();

		progressDialog = new ProgressDialog(FeedBack.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("Lütfen Bekleyiniz...");
		progressDialog.setMessage("Þekiller Yükleniyor...");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(false);
		progressDialog.show();

		final ArrayList<PolygonOptions> polygonOptionsList = new ArrayList<PolygonOptions>();

		new Thread(new Runnable() {

			public void run() {

				polygons = com.telekurye.kml.Polygon.GetBuildingShapeByDistrictId(Info.DISTRICT_ID);

				if (polygons == null || polygons.size() < 1) {
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
						progressDialog = null;
					}
					return;
				}

				for (com.telekurye.kml.Polygon polygon : polygons) {

					polygon.coors = new ArrayList<LatLng>();

					if (polygon.type == 2) {

						final PolygonOptions polygonOptions = new PolygonOptions();

						String[] coors = SplitUsingTokenizer(polygon.coordinates, "||");

						for (String string : coors) {
							String[] coor = string.split(",");

							final LatLng point = new LatLng(Float.valueOf(coor[0]), Float.valueOf(coor[1]));

							polygon.coors.add(point);
							polygonOptions.add(point);
						}

						polygonOptions.strokeColor(Color.RED);

						List<MissionControl> mList = new MissionControl().GetMissionsByShapeId(polygon.polygonid.intValue());

						if (mList == null || mList.size() < 1) {
							polygonOptions.fillColor(0x70FF0000);
						} else {
							polygonOptions.fillColor(Color.TRANSPARENT);
						}

						for (MissionControl missionItem : greenShapeList) {
							if (missionItem.getShapeId() == polygon.polygonid.longValue()) {
								polygonOptions.strokeColor(Color.GREEN);
								polygonOptions.fillColor(0x802EFE64);
							}
						}

						polygonOptions.strokeWidth(3);
						polygonOptionsList.add(polygonOptions);

					}
				}

				runOnUiThread(new Runnable() {
					public void run() {

						// for (PolygonOptions polygonOptions :
						// polygonOptionsList) {
						// myMap.addPolygon(polygonOptions);
						// }

						GoogleMap map = myMap;

						for (PolygonOptions polygonOptions : polygonOptionsList) {
							map.addPolygon(polygonOptions);
						}

						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
							progressDialog = null;
						}
					}
				});

			}

		}).start();
	}

	public float distance(float lat_a, float lng_a, float lat_b, float lng_b) { // iki
																				// koordinat
																				// arasý
																				// uzaklýk
																				// (metre
																				// cinsinden)
		double earthRadius = 3958.75;
		double latDiff = Math.toRadians(lat_b - lat_a);
		double lngDiff = Math.toRadians(lng_b - lng_a);
		double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) * Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;

		int meterConversion = 1609;

		return new Float(distance * meterConversion).floatValue();
	}

	public static String[] SplitUsingTokenizer(String subject, String delimiters) {
		StringTokenizer strTkn = new StringTokenizer(subject, delimiters);
		ArrayList<String> arrLis = new ArrayList<String>(subject.length());

		while (strTkn.hasMoreTokens())
			arrLis.add(strTkn.nextToken());

		return arrLis.toArray(new String[0]);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	// ###############################################################

	@Override
	public void onGpsStatusChanged(int event) {

		switch (event) {
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			if (mLastLocation != null)
				isGPSFix = (SystemClock.elapsedRealtime() - mLastLocationMillis) < 3000;

			// if (isGPSFix) { // A fix has been acquired.
			//
			// } else {
			// UserAccuracy = 5000;
			// }

			break;
		}

		isGPSFix = true;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (uiUpdateThread != null && uiUpdateThread.isAlive()) {
			uiUpdateThread.interrupt();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		uiUpdateThread = StatusBarThread();

		if (!uiUpdateThread.isAlive()) {
			uiUpdateThread.start();
		}

	}

	private Thread StatusBarThread() {

		final Handler mHandler = new Handler();

		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {

				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (!Thread.interrupted()) {
							StatusBarValues();
							mHandler.postDelayed(this, Info.SYNCPERIOD / 2);
						}
					}
				});

			}
		});

		return th;
	}

	private void StatusBarValues() {

		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		String mHour = null, mMinute = null;

		if (hour < 10) {
			mHour = "0" + hour;
		} else {
			mHour = "" + hour;
		}

		if (minute < 10) {
			mMinute = "0" + minute;
		} else {
			mMinute = "" + minute;
		}

		String time = "Saat : " + mHour + ":" + mMinute;
		String battery = "Pil : %" + Tools.getBatteryLevel(FeedBack.this);

		tv_info_time.setText(time);
		tv_info_battery.setText(battery);

		tv_networkStatus.setText(Tools.getNetworkType(FeedBack.this));

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			tv_networkStatus.setBackgroundColor(Color.RED);
		} else {
			tv_networkStatus.setBackgroundColor(Color.GREEN);
		}

		// if (isGPSFix && UserAccuracy != 0 && UserAccuracy < 100) {
		tv_info_accuracy.setText("GPS : " + UserAccuracy);
		// }
		// else {
		// tv_info_accuracy.setText("Gps Yok");
		// }

		// if (UserAccuracy < Info.GPS_ACCURACY) {
		// tv_info_accuracy.setBackgroundColor(Color.GREEN);
		// }
		// else {
		// tv_info_accuracy.setBackgroundColor(Color.RED);
		// }

	}

	private MarkerOptions getMarkerOptions(LatLng position) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.flat(true);
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrowred));
		markerOptions.anchor(0.5f, 0.5f);
		markerOptions.position(position);

		return markerOptions;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LiveData.photoinfo = new ArrayList<PhotoInfo>();
		App.bus.unregister(this);
	}

	@Subscribe
	public void messageReceived(EBus event) {

		if (selectedPolygon != null) {
			currentPolygon.setFillColor(0x802EFE64);
			currentPolygon.setStrokeColor(Color.GREEN);
			saveAsGreenShape(selectedPolygon);
			selectedPolygon = null;
		}

	}
}
