package com.telekurye.maks2;

import java.util.ArrayList;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.telekurye.maphelpers.PolyUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Created by sefagurel on 26.06.2015.
 */
public class Shape {

	private int											shapeId;
	private ShapeType									shapeType;
	private PolygonOptions								polygonOptions;
	private PolylineOptions								polylineOptions;
	private boolean										isClickedShape	= false;
	private boolean										isSaved			= false;
	private com.google.android.gms.maps.model.Polygon	polygon;
	private Polyline									polyline;

	public Shape(ShapeTable shapeTable) {
		createShape cs = new createShape(shapeTable);
		this.shapeId = cs.getShapeId();
		this.shapeType = cs.getShapeType();
		this.polygonOptions = cs.getPolygonOptions();
		this.polylineOptions = cs.getPolylineOptions();
	}

	public int getShapeId() {
		return shapeId;
	}

	public void setShapeId(int shapeId) {
		this.shapeId = shapeId;
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

	public void setShapeType(ShapeType shapeType) {
		this.shapeType = shapeType;
	}

	public PolygonOptions getPolygonOptions() {
		return polygonOptions;
	}

	public void setPolygonOptions(PolygonOptions polygonOptions) {
		this.polygonOptions = polygonOptions;
	}

	public PolylineOptions getPolylineOptions() {
		return polylineOptions;
	}

	public void setPolylineOptions(PolylineOptions polylineOptions) {
		this.polylineOptions = polylineOptions;
	}

	public boolean isPressed(LatLng point) {

		if (polygon == null) {
			isClickedShape = false;
			return false;
		}

		if (PolyUtil.containsLocation(point, polygon.getPoints(), true)) {
			isClickedShape = true;
			return true;
		}
		else {
			isClickedShape = false;
			return false;
		}
	}

	public void fillGreenColor() {

		if (polygon != null && !isSaved) {
			polygon.setStrokeColor(Color.rgb(30, 30, 30));
			polygon.setStrokeWidth(1.5f);
			polygon.setFillColor(0x802EFE64);
		}
	}

	public void fillRedColor() {

		if (polygon != null && !isSaved) {
			polygon.setStrokeColor(Color.rgb(30, 30, 30));
			polygon.setStrokeWidth(1.5f);
			polygon.setFillColor(0x60FF0000);
		}

	}

	public boolean isClicked() {
		return isClickedShape;
	}

	public com.google.android.gms.maps.model.Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(com.google.android.gms.maps.model.Polygon polygon) {
		this.polygon = polygon;
	}

	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}
}

class createShape {

	private ShapeTable		shapeTable;
	private String			strGeometry;
	private ShapeType		shapeType;
	private GeometryFactory	geometryFactory;
	private WKTReader		reader;
	private Geometry		geometry	= null;
	private int				shapeId;

	public createShape(ShapeTable shapeTable) {
		this.shapeTable = shapeTable;
		this.strGeometry = shapeTable.Shape;
		this.shapeId = shapeTable.ShapeId;
		this.geometryFactory = new GeometryFactory();
		this.reader = new WKTReader(geometryFactory);

		try {
			if (strGeometry != null && !strGeometry.equals("") && strGeometry.length() > 10) {
				this.geometry = reader.read(strGeometry);
			}
			else {
				this.geometry = null;
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		if (geometry != null) {
			this.shapeType = findShapeType(geometry.getGeometryType());
		}
		else {
			this.shapeType = null;
		}

		// this.shapeType = findShapeType(geometry != null ? geometry.getGeometryType() : null);
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

	public int getShapeId() {
		return shapeId;
	}

	public PolygonOptions getPolygonOptions() {
		PolygonOptions polyOptions = null;
		Polygon polygon = null;
		Coordinate[] outerCoordinates = null;
		Coordinate[] innerCoordinates = null;
		ArrayList<LatLng> outer = null;
		ArrayList<LatLng> inner = null;

		if (geometry == null) {
			return null;
		}

		try {

			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				outer = new ArrayList<>();
				polyOptions = new PolygonOptions();
				polygon = (Polygon) geometry.getGeometryN(i);

				outerCoordinates = polygon.getExteriorRing().getCoordinates();
				for (Coordinate outerCoordinate : outerCoordinates) {
					outer.add(new LatLng(outerCoordinate.y, outerCoordinate.x));
				}
				polyOptions.addAll(outer);

				if (polygon.getNumInteriorRing() > 0) {

					for (int j = 0; j < polygon.getNumInteriorRing(); j++) {
						inner = new ArrayList<>();
						innerCoordinates = polygon.getInteriorRingN(j).getCoordinates();
						for (Coordinate innerCoordinate : innerCoordinates) {
							inner.add(new LatLng(innerCoordinate.y, innerCoordinate.x));
						}
						polyOptions.addHole(inner);
					}
				}

				polyOptions.strokeColor(Color.rgb(30, 30, 30));
				polyOptions.strokeWidth(1.5f);
				polyOptions.fillColor(0x60FF0000);

			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return polyOptions;
	}

	public PolylineOptions getPolylineOptions() {
		return null;
	}

	private ShapeType findShapeType(String shapeType) {

		if (shapeType.equals("Point")) {
			return ShapeType.Point;
		}
		else if (shapeType.equals("MultiPoint")) {
			return ShapeType.MultiPoint;
		}
		else if (shapeType.equals("LineString")) {
			return ShapeType.LineString;
		}
		else if (shapeType.equals("MultiLineString")) {
			return ShapeType.MultiLineString;
		}
		else if (shapeType.equals("Polygon")) {
			return ShapeType.Polygon;
		}
		else if (shapeType.equals("MultiPolygon")) {
			return ShapeType.MultiPolygon;
		}
		else if (shapeType.equals("GeometryCollection")) {
			return ShapeType.GeometryCollection;
		}
		else {
			return null;
		}
	}

	// private PolygonOptions test() {
	// PolygonOptions polyOptions = null;
	// Polygon polygon = null;
	// Coordinate[] outerCoordinates = null;
	// Coordinate[] innerCoordinates = null;
	// ArrayList<LatLng> outer = null;
	// ArrayList<LatLng> inner = null;
	//
	// try {
	//
	// // Gets each polygon of a geometry
	// for (int i = 0; i < geometry.getNumGeometries(); i++) {
	// outer = new ArrayList<>();
	// polyOptions = new PolygonOptions();
	//
	// System.out.println(geometry.getGeometryType());
	//
	// polygon = (Polygon) geometry.getGeometryN(i);
	//
	// outerCoordinates = polygon.getExteriorRing().getCoordinates();
	// for (Coordinate outerCoordinate : outerCoordinates) {
	// outer.add(new LatLng(outerCoordinate.y, outerCoordinate.x));
	// }
	// polyOptions.addAll(outer);
	//
	// if (polygon.getNumInteriorRing() > 0) {
	//
	// for (int j = 0; j < polygon.getNumInteriorRing(); j++) {
	// inner = new ArrayList<>();
	// innerCoordinates = polygon.getInteriorRingN(j).getCoordinates();
	// for (Coordinate innerCoordinate : innerCoordinates) {
	// inner.add(new LatLng(innerCoordinate.y, innerCoordinate.x));
	// }
	// polyOptions.addHole(inner);
	// }
	// }
	// polyOptions.strokeColor(Color.rgb(30, 30, 30));
	// polyOptions.strokeWidth(1.5f);
	// polyOptions.fillColor(0x60FF0000);
	//
	// }
	//
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	// return polyOptions;
	//
	// }

}