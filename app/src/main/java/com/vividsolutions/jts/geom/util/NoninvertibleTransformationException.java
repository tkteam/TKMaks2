package com.vividsolutions.jts.geom.util;

/**
 * Indicates that an {@link com.vividsolutions.jts.geom.util.AffineTransformation}
 * is non-invertible.
 *
 * @author Martin Davis
 */
public class NoninvertibleTransformationException
	extends Exception
{
  public NoninvertibleTransformationException()
  {
    super();
  }
  public NoninvertibleTransformationException(String msg)
  {
    super(msg);
  }
}
