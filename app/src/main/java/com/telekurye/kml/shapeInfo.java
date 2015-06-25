package com.telekurye.kml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sefagurel on 18.06.2015.
 */
public class shapeInfo {

	public int		bskod;
	public Long		basarId;
	public String	aciklama;
	public int		count;

	public shapeInfo() {

	}

	public shapeInfo(int bskod, Long basarId, String aciklama, int count) {
		this.bskod = bskod;
		this.basarId = basarId;
		this.aciklama = aciklama;
		this.count = count;
	}

	public List<shapeInfo> GetList(Polygon selectedPolygon) {

		basarId = selectedPolygon.polygonid;

		List<buildingtypes> bTypes = new buildingtypes().GetDataByBasarId(basarId);
		List<shapeInfo> sinfo = new ArrayList<>();

		for (int i = 0; i < bTypes.size(); i++) {
			if (bTypes.get(i).ACIKLAMA.equalsIgnoreCase("NULL")) {
				bTypes.remove(i);
			}
		}

		if (bTypes.size() > 0) {
			Map<Integer, String> bt = new HashMap<>();

			List<Integer> intlist = new ArrayList<>();

			for (buildingtypes bType : bTypes) {
				bt.put(bType.BSKOD, bType.ACIKLAMA);
				intlist.add(bType.BSKOD);
			}

			Set<Integer> unique = new HashSet<>(intlist);

			for (Integer key : unique) {
				sinfo.add(new shapeInfo(key, basarId, bt.get(key), Collections.frequency(intlist, key)));
			}
		}
		else {
			sinfo.add(new shapeInfo(0, basarId, "Bilgi Yok", 0));
		}

		return sinfo;
	}
}
