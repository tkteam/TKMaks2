package com.telekurye.maks2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.maks2.database.DatabaseHelper;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "MissionControl")
public class MissionControl {

	@DatabaseField(id = true) private int	UDMId;
	@DatabaseField private int				FloorCount;
	@DatabaseField private String			BuildingNumber;
	@DatabaseField private int				IndependentSectionCount;
	@DatabaseField private boolean			IsGreen;
	@DatabaseField private int				ShapeId;
	@DatabaseField private int				TypeId;
	@DatabaseField private String			BinaTipi;
	@DatabaseField private String			BagimsizBolumTipi;

	// @DatabaseField private String Address;

	// public void Insert() throws ClassNotFoundException {
	// try {
	//
	// Class<?> c = this.getClass();
	//
	// Dao<Class<?>, Integer> locationinsert = DatabaseHelper.getDbHelper().getDataHelper(c);
	//
	// Class<?> existenceCheck = locationinsert.queryForId(this.UDMId);
	//
	// if (existenceCheck != null) {
	// locationinsert.update(c);
	// }
	// else {
	// locationinsert.create(c);
	// }
	// }
	// catch (SQLException e) {
	// Tools.saveErrors(e);
	//
	// }
	// }
	//
	// public void Update() throws ClassNotFoundException {
	//
	// Class<?> c = this.getClass();
	//
	// try {
	// Dao<Class<?>, Integer> pickupOrderinsert = DatabaseHelper.getDbHelper().getDataHelper(c);
	//
	// pickupOrderinsert.update(c);
	// }
	// catch (SQLException e) {
	// Tools.saveErrors(e);
	//
	// }
	// }
	//
	// public List<Class<?>> GetMissionsByShapeId(int shapeId) throws ClassNotFoundException {
	//
	// Class<?> c = this.getClass();
	//
	// List<Class<?>> data = new ArrayList<Class<?>>();
	//
	// try {
	//
	// Dao<Class<?>, Integer> dao = DatabaseHelper.getDbHelper().getDataHelper(c);
	// QueryBuilder<Class<?>, Integer> qBuilder = dao.queryBuilder();
	// qBuilder.where().eq("IsDeleted", false).and().eq("IsCompleted", false).and().eq("ShapeId", shapeId);
	// PreparedQuery<Class<?>> pQuery = qBuilder.prepare();
	// data = dao.query(pQuery);
	//
	// }
	// catch (SQLException e) {
	// Tools.saveErrors(e);
	// }
	//
	// return data;
	// }

	public void Insert() {
		try {
			Dao<MissionControl, Integer> locationinsert = DatabaseHelper.getDbHelper().getMissionControlDataHelper();

			MissionControl existenceCheck = locationinsert.queryForId(this.UDMId);

			if (existenceCheck != null) {
				locationinsert.update(this);
			}
			else {
				locationinsert.create(this);
			}
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<MissionControl, Integer> pickupOrderinsert = (DatabaseHelper.getDbHelper()).getMissionControlDataHelper();

			pickupOrderinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public List<MissionControl> GetMissionsByShapeId(int shapeId) {

		List<MissionControl> data = new ArrayList<>();

		try {

			Dao<MissionControl, Integer> dao = DatabaseHelper.getDbHelper().getMissionControlDataHelper();
			QueryBuilder<MissionControl, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("ShapeId", shapeId);
			PreparedQuery<MissionControl> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public List<MissionControl> getGreenShapeList() {

		List<MissionControl> data = new ArrayList<>();

		try {

			Dao<MissionControl, Integer> dao = DatabaseHelper.getDbHelper().getMissionControlDataHelper();
			QueryBuilder<MissionControl, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsGreen", true);
			PreparedQuery<MissionControl> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	// public SyncRequest<List<MissionControl>> GetAllDataForSync() {
	//
	// SyncRequest<List<MissionControl>> sr = new SyncRequest<List<MissionControl>>();
	//
	// try {
	//
	// String startDateString = "1989-10-03 11:26:36";
	// sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
	// sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat
	//
	// Dao<MissionControl, Integer> dao = DatabaseHelper.getDbHelper().getMissionControlDataHelper();
	//
	// List<MissionControl> data = dao.queryForAll();
	//
	// sr.setTypedObjects(data);
	//
	// }
	// catch (SQLException e) {
	// Tools.saveErrors(e);
	//
	// }
	//
	// return sr;
	// }
	//
	// public int GetRowCount() {
	// int count = 0;
	//
	// try {
	// Dao<MissionControl, Integer> dao = DatabaseHelper.getDbHelper().getMissionControlDataHelper();
	// count = (int) dao.countOf();
	// }
	// catch (Exception e) {
	// Tools.saveErrors(e);
	//
	// }
	//
	// return count;
	// }
	//
	// public void DeleteRow(int deleteId) {
	// try {
	//
	// Dao<MissionControl, Integer> dao = DatabaseHelper.getDbHelper().getMissionControlDataHelper();
	// DeleteBuilder<MissionControl, Integer> deleteBuilder = dao.deleteBuilder();
	// deleteBuilder.where().eq("Id", deleteId);
	// deleteBuilder.delete();
	// }
	// catch (Exception e) {
	// Tools.saveErrors(e);
	//
	// }
	// }
	//
	// public List<MissionControl> getColumn(String ColumnName) throws SQLException {
	// Dao<MissionControl, Integer> dao = DatabaseHelper.getDbHelper().getMissionControlDataHelper();
	// List<MissionControl> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
	// return results;
	// }
	//
	// public MissionControl getRow(int id) {
	//
	// MissionControl dmfb = null;
	//
	// try {
	// Dao<MissionControl, Integer> dao = DatabaseHelper.getDbHelper().getMissionControlDataHelper();
	// dmfb = dao.queryForAll().get(id);
	// }
	// catch (Exception e) {
	// Tools.saveErrors(e);
	//
	// }
	//
	// return dmfb;
	// }

	public int getUDMId() {
		return UDMId;
	}

	public void setUDMId(int uDMId) {
		UDMId = uDMId;
	}

	public int getFloorCount() {
		return FloorCount;
	}

	public void setFloorCount(int floorCount) {
		FloorCount = floorCount;
	}

	public int getIndependentSectionCount() {
		return IndependentSectionCount;
	}

	public void setIndependentSectionCount(int independentSectionCount) {
		IndependentSectionCount = independentSectionCount;
	}

	public boolean isIsGreen() {
		return IsGreen;
	}

	public void setIsGreen(boolean isGreen) {
		IsGreen = isGreen;
	}

	public int getShapeId() {
		return ShapeId;
	}

	public void setShapeId(int shapeId) {
		ShapeId = shapeId;
	}

	public String getBuildingNumber() {
		return BuildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		BuildingNumber = buildingNumber;
	}

	public int getTypeId() {
		return TypeId;
	}

	public void setTypeId(int typeId) {
		TypeId = typeId;
	}

	public String getBinaTipi() {
		return BinaTipi;
	}

	public void setBinaTipi(String binaTipi) {
		BinaTipi = binaTipi;
	}

	public String getBagimsizBolumTipi() {
		return BagimsizBolumTipi;
	}

	public void setBagimsizBolumTipi(String bagimsizBolumTipi) {
		BagimsizBolumTipi = bagimsizBolumTipi;
	}
}
