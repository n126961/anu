package com.covid.minus.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class GeoUtil {

	public static Geometry wktToGeometry(String wellKnownText) throws ParseException {

		return new WKTReader().read(wellKnownText);
	}
	
	public static Point getPoint(Double x, Double y) {
		GeometryFactory factory = new GeometryFactory();
		return factory.createPoint(new Coordinate(x, y));
	}

}
