package com.telekurye.maks2.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.telekurye.kml.buildingtypes;
import com.telekurye.maks2.MaksLocation;
import com.telekurye.kml.Polygon;
import com.telekurye.mobileui.Login;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class DatabaseHelperMap extends OrmLiteSqliteOpenHelper {

	private static final int			DATABASE_VERSION	= Info.DATABASE_VERSION;
	private static final String			DATABASE_NAME		= Info.MAP_DBNAME;
	private final Context				myContext;
	private static DatabaseHelperMap	dbHelper;

	private static final String			MAP_DATABASE_PATH	= "/data/data/com.telekurye.mobileui/databases/";
	private static Object				syncObject			= new Object();

	public DatabaseHelperMap(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		// this.getReadableDatabase();
		// this.getWritableDatabase();

	}

	public void copyDataBase() throws IOException {

		File dir = new File(MAP_DATABASE_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File dbfile = new File(MAP_DATABASE_PATH + DATABASE_NAME);

		if (!dbfile.exists()) {

			InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
			String outFileName = MAP_DATABASE_PATH + DATABASE_NAME;
			OutputStream myOutput = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);

			}

			myOutput.flush();
			myOutput.close();
			myInput.close();
		}

	}

	public static DatabaseHelperMap getDbHelper() {
		synchronized (syncObject) {
			if (dbHelper == null) {
				dbHelper = new DatabaseHelperMap(Login.AppContext);
			}
		}
		return dbHelper;
		// return new DatabaseHelper(Login.AppContext);
	}

	private Dao<Polygon, Integer>		PolygonDataHelper		= null;
	private Dao<buildingtypes, Integer>	buildingtypesDataHelper	= null;
	private Dao<MaksLocation, Integer>	MaksLocationDataHelper	= null;

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Polygon.class);
			TableUtils.createTable(connectionSource, MaksLocation.class);
		}
		catch (java.sql.SQLException e) {
			Tools.saveErrors(e);
		}
	}

	public void clearDatabase() {
		ConnectionSource connectionSource = getConnectionSource();
		try {
			TableUtils.clearTable(connectionSource, Polygon.class);
			TableUtils.clearTable(connectionSource, buildingtypes.class);
			TableUtils.clearTable(connectionSource, MaksLocation.class);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
	}

	public void deleteDatabase() {
		ConnectionSource connectionSource = getConnectionSource();
		try {
			TableUtils.dropTable(connectionSource, Polygon.class, true);
			TableUtils.dropTable(connectionSource, buildingtypes.class, true);
			TableUtils.dropTable(connectionSource, MaksLocation.class, true);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelperMap.class.getName(), "onUpgrade");

			TableUtils.dropTable(connectionSource, Polygon.class, true);
			TableUtils.dropTable(connectionSource, buildingtypes.class, true);
			TableUtils.dropTable(connectionSource, MaksLocation.class, true);

			onCreate(db, connectionSource);
		}
		catch (java.sql.SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public Dao<Polygon, Integer> getPolygonDataHelper() throws SQLException {
		if (PolygonDataHelper == null) {
			PolygonDataHelper = getDao(Polygon.class);
		}
		return PolygonDataHelper;
	}

	public Dao<buildingtypes, Integer> getbuildingtypesDataHelper() throws SQLException {
		if (buildingtypesDataHelper == null) {
			buildingtypesDataHelper = getDao(buildingtypes.class);
		}
		return buildingtypesDataHelper;
	}

	public Dao<MaksLocation, Integer> getMaksLocationDataHelper() throws SQLException {
		if (MaksLocationDataHelper == null) {
			MaksLocationDataHelper = getDao(MaksLocation.class);
		}
		return MaksLocationDataHelper;
	}

}
