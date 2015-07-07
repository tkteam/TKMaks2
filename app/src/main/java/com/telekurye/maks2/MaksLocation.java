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

@DatabaseTable(tableName = "locationDatabase")
public class MaksLocation {

	@DatabaseField private int		LocationTypeId;
	@DatabaseField private int		CityId;
	@DatabaseField private String	CityName;
	@DatabaseField private int		CountyId;
	@DatabaseField private String	CountyName;
	@DatabaseField private int		DistrictId;
	@DatabaseField private String	DistrictName;

	public MaksLocation() {

	}

	public List<Items> GetCities() {

		List<Items> data = new ArrayList<>();

		try {

			Dao<MaksLocation, Integer> dao = DatabaseHelperMap.getDbHelper().getMaksLocationDataHelper();
			QueryBuilder<MaksLocation, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("LocationTypeId", 4);
			PreparedQuery<MaksLocation> pQuery = qBuilder.prepare();
			List<MaksLocation> maksLocation = dao.query(pQuery);

			for (MaksLocation mLoc : maksLocation) {
				Items i = new Items();
				i.setId(mLoc.getCityId());
				i.setName(mLoc.getCityName());
				i.setTypeId(mLoc.getLocationTypeId());
				data.add(i);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public List<Items> GetCounties(int CityId) {

		List<Items> data = new ArrayList<>();

		try {

			Dao<MaksLocation, Integer> dao = DatabaseHelperMap.getDbHelper().getMaksLocationDataHelper();
			QueryBuilder<MaksLocation, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("LocationTypeId", 5).and().eq("CityId", CityId);
			PreparedQuery<MaksLocation> pQuery = qBuilder.prepare();
			List<MaksLocation> maksLocation = dao.query(pQuery);

			for (MaksLocation mLoc : maksLocation) {
				Items i = new Items();
				i.setId(mLoc.getCountyId());
				i.setName(mLoc.getCountyName());
				i.setTypeId(mLoc.getLocationTypeId());
				data.add(i);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public List<Items> GetDistricts(int CountyId) {

		List<Items> data = new ArrayList<Items>();

		try {

			Dao<MaksLocation, Integer> dao = DatabaseHelperMap.getDbHelper().getMaksLocationDataHelper();
			QueryBuilder<MaksLocation, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("LocationTypeId", 8).and().eq("CountyId", CountyId);
			PreparedQuery<MaksLocation> pQuery = qBuilder.prepare();
			List<MaksLocation> maksLocation = dao.query(pQuery);

			for (MaksLocation mLoc : maksLocation) {
				Items i = new Items();
				i.setId(mLoc.getDistrictId());
				i.setName(mLoc.getDistrictName());
				i.setTypeId(mLoc.getLocationTypeId());
				data.add(i);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public int getLocationTypeId() {
		return LocationTypeId;
	}

	public void setLocationTypeId(int locationTypeId) {
		LocationTypeId = locationTypeId;
	}

	public int getCityId() {
		return CityId;
	}

	public void setCityId(int cityId) {
		CityId = cityId;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public int getCountyId() {
		return CountyId;
	}

	public void setCountyId(int countyId) {
		CountyId = countyId;
	}

	public String getCountyName() {
		return CountyName;
	}

	public void setCountyName(String countyName) {
		CountyName = countyName;
	}

	public int getDistrictId() {
		return DistrictId;
	}

	public void setDistrictId(int districtId) {
		DistrictId = districtId;
	}

	public String getDistrictName() {
		return DistrictName;
	}

	public void setDistrictName(String districtName) {
		DistrictName = districtName;
	}

}
