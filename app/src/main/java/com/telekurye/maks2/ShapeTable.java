package com.telekurye.maks2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.maks2.database.DatabaseHelperMap;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "geometries")
public class ShapeTable {

	@DatabaseField public int		CityId;
	@DatabaseField public String	CityName;
	@DatabaseField public int		CountyId;
	@DatabaseField public String	CountyName;
	@DatabaseField public int		DistrictId;
	@DatabaseField public String	DistrictName;
	@DatabaseField public int		StreetId;
	@DatabaseField public String	StreetName;
	@DatabaseField public int		BuildingId;
	@DatabaseField public String	BuildingNumber;
	@DatabaseField public String	Shape;
	@DatabaseField public String	Description;
	@DatabaseField public int		ShapeId;

	public ShapeTable() {

	}

	public static List<ShapeTable> GetBuildingShapeByDistrictId(String districtId) {

		List<ShapeTable> data = new ArrayList<>();

		try {

			Dao<ShapeTable, Integer> dao = DatabaseHelperMap.getDbHelper().getShapeTableDataHelper();
			QueryBuilder<ShapeTable, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("DistrictId", districtId);
			qBuilder.groupBy("Shape");
			PreparedQuery<ShapeTable> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public List<ShapeTable> GetDataByShapeId(int shapeId) {
		List<ShapeTable> data = new ArrayList<>();
		try {
			Dao<ShapeTable, Integer> dao = DatabaseHelperMap.getDbHelper().getShapeTableDataHelper();
			QueryBuilder<ShapeTable, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("ShapeId", shapeId);
			PreparedQuery<ShapeTable> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
		return data;
	}

	public List<Items> getColumn(String ColumnName) throws SQLException {
		Dao<ShapeTable, Integer> dao = DatabaseHelperMap.getDbHelper().getShapeTableDataHelper();
		List<ShapeTable> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();

		List<Items> data = new ArrayList<>();

		for (ShapeTable mLoc : results) {
			Items i = new Items();
			i.setId(mLoc.CityId);
			i.setName(mLoc.CityName);
			i.setTypeId(mLoc.CityId);
			data.add(i);
		}

		return data;
	}

	// public List<Items> GetCities() {
	//
	// List<Items> data = new ArrayList<>();
	//
	// try {
	//
	// Dao<ShapeTable, Integer> dao = DatabaseHelperMap.getDbHelper().getShapeTableDataHelper();
	// QueryBuilder<ShapeTable, Integer> qBuilder = dao.queryBuilder();
	// qBuilder.distinct().where()..groupBy("CityId");
	// PreparedQuery<ShapeTable> pQuery = qBuilder.prepare();
	// List<ShapeTable> maksLocation = dao.query(pQuery);
	//
	// for (ShapeTable mLoc : maksLocation) {
	// Items i = new Items();
	// i.setId(mLoc.CityId);
	// i.setName(mLoc.CityName);
	// i.setTypeId(mLoc.CityId);
	// data.add(i);
	// }
	//
	// }
	// catch (SQLException e) {
	// Tools.saveErrors(e);
	// }
	//
	// return data;
	// }

	// public List<Items> GetCounties(int CityId) {
	//
	// List<Items> data = new ArrayList<>();
	//
	// try {
	// Dao<ShapeTable, Integer> dao = DatabaseHelperMap.getDbHelper().getShapeTableDataHelper();
	// QueryBuilder<ShapeTable, Integer> qBuilder = dao.queryBuilder();
	// qBuilder.where().eq("LocationTypeId", 5).and().eq("CityId", CityId);
	// PreparedQuery<ShapeTable> pQuery = qBuilder.prepare();
	// List<ShapeTable> maksLocation = dao.query(pQuery);
	//
	// for (ShapeTable mLoc : maksLocation) {
	// Items i = new Items();
	// i.setId(mLoc.getCountyId());
	// i.setName(mLoc.getCountyName());
	// i.setTypeId(mLoc.getLocationTypeId());
	// data.add(i);
	// }
	//
	// }
	// catch (SQLException e) {
	// Tools.saveErrors(e);
	// }
	//
	// return data;
	// }
	//
	// public List<Items> GetDistricts(int CountyId) {
	//
	// List<Items> data = new ArrayList<Items>();
	//
	// try {
	//
	// Dao<ShapeTable, Integer> dao = DatabaseHelperMap.getDbHelper().getShapeTableDataHelper();
	// QueryBuilder<ShapeTable, Integer> qBuilder = dao.queryBuilder();
	// qBuilder.where().eq("LocationTypeId", 8).and().eq("CountyId", CountyId);
	// PreparedQuery<ShapeTable> pQuery = qBuilder.prepare();
	// List<ShapeTable> maksLocation = dao.query(pQuery);
	//
	// for (ShapeTable mLoc : maksLocation) {
	// Items i = new Items();
	// i.setId(mLoc.getDistrictId());
	// i.setName(mLoc.getDistrictName());
	// i.setTypeId(mLoc.getLocationTypeId());
	// data.add(i);
	// }
	//
	// }
	// catch (SQLException e) {
	// Tools.saveErrors(e);
	// }
	//
	// return data;
	// }

}
