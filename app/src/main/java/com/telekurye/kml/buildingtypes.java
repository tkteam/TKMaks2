package com.telekurye.kml;

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

@DatabaseTable(tableName = "buildingtypes")
public class buildingtypes {

	@DatabaseField(id = true) public Integer	id;
	@DatabaseField public Integer				BSKOD;
	@DatabaseField public Long					BASARID;
	@DatabaseField public String				ACIKLAMA;

	public buildingtypes() {

	}

	public List<buildingtypes> GetDataByBasarId(Long basarId) {
		List<buildingtypes> data = new ArrayList<>();
		try {
			Dao<buildingtypes, Integer> dao = DatabaseHelperMap.getDbHelper().getbuildingtypesDataHelper();
			QueryBuilder<buildingtypes, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("BASARID", basarId);
			PreparedQuery<buildingtypes> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
		return data;
	}

}
