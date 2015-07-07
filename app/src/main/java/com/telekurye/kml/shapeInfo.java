package com.telekurye.kml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telekurye.maks2.ShapeTable;

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

	public List<shapeInfo> GetList(int shapeId) {

		// basarId = selectedPolygon.polygonid;

		List<ShapeTable> bTypes = new ShapeTable().GetDataByShapeId(shapeId);
		List<shapeInfo> sinfo = new ArrayList<>();

		for (int i = 0; i < bTypes.size(); i++) {
			if (bTypes.get(i).Description.equalsIgnoreCase("NULL")) {
				bTypes.remove(i);
			}
		}

		if (bTypes.size() > 0) {
			Map<Integer, String> bt = new HashMap<>();

			List<Integer> intlist = new ArrayList<>();

			for (ShapeTable bType : bTypes) {

				char[] ascii = bType.Description.toCharArray();

				int i = 0;

				for (char c : ascii) {
					i += (int) c;
				}

				bt.put(i, bType.Description);
				intlist.add(i);
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
