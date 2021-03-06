
/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 *
 * Copyright (C) 2001 Vivid Solutions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, contact:
 *
 *     Vivid Solutions
 *     Suite #1A
 *     2328 Government Street
 *     Victoria BC  V8T 5G5
 *     Canada
 *
 *     (250)385-6040
 *     www.com.vividsolutions.com
 */
package com.vividsolutions.jts.operation.distance;

import com.vividsolutions.jts.geom.*;

/**
 * Represents the location of a point on a Geometry.
 * Maintains both the actual point location (which of course
 * may not be exact) as well as information about the component
 * and segment index where the point occurs.
 * Locations inside area Geometrys will not have an associated segment index,
 * so in this case the segment index will have the sentinel value of INSIDE_AREA.
 *
 * @version 1.7
 */
public class GeometryLocation
{

  /**
   * Special value of segment-index for locations inside area geometries. These
   * locations do not have an associated segment index.
   */
  public static final int INSIDE_AREA = -1;

  private Geometry component = null;
  private int segIndex;
  private Coordinate pt = null;

  /**
   * Constructs a GeometryLocation specifying a point on a geometry, as well as the
   * segment that the point is on (or INSIDE_AREA if the point is not on a segment).
   */
  public GeometryLocation(Geometry component, int segIndex, Coordinate pt)
  {
    this.component = component;
    this.segIndex = segIndex;
    this.pt = pt;
  }

  /**
   * Constructs a GeometryLocation specifying a point inside an area geometry.
   */
  public GeometryLocation(Geometry component,Coordinate pt)
  {
    this(component, INSIDE_AREA, pt);
  }

  /**
   * Returns the geometry associated with this location.
   */
  public Geometry getGeometryComponent() { return component; }
  /**
   * Returns the segment index for this location. If the location is inside an
   * area, the index will have the value INSIDE_AREA;
   *
   * @return the segment index for the location, or INSIDE_AREA
   */
  public int getSegmentIndex() { return segIndex; }
  /**
   * Returns the location.
   */
  public Coordinate getCoordinate() { return pt; }
  /**
   * Returns whether this GeometryLocation represents a point inside an area geometry.
   */
  public boolean isInsideArea() { return segIndex == INSIDE_AREA; }
}
