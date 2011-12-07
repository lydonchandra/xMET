/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br>
 * <br>
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br>
 * <br>
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br>
 * <br>
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br>
 * <br>
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.profiles.codecs.impl;

import java.util.ArrayList;
import java.util.List;

import n.io.xml.JDOMXmlUtils;
import n.reporting.Reporting;

import org.jdom.Element;

import xmet.profiles.codecs.DefaultCodec;
import xmet.profiles.codecs.SpatialExtentCodec;
import xmet.profiles.geometry.BoundingBox;
import xmet.profiles.geometry.ExtentId;
import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.Polyline;
import xmet.profiles.geometry.Shape;
import xmet.profiles.geometry.SpatialExtent;
import xmet.profiles.geometry.Surface;
import xmet.profiles.geometry.SurfaceHole;
import xmet.profiles.geometry.impl2d.BoundingBox2d;
import xmet.profiles.geometry.impl2d.ExtentId2d;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Polyline2d;
import xmet.profiles.geometry.impl2d.SpatialExtent2d;
import xmet.profiles.geometry.impl2d.Surface2d;
import xmet.profiles.geometry.impl2d.SurfaceHole2d;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Settable;

/**
 * A simple xml encoding codec. This version converts spatial extent to and from
 * xml string and sets it as specified element's setable value; i.e. saves and
 * loads from string
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class StringSpatialExtentCodec
    implements
    SpatialExtentCodec,
    DefaultCodec {

    /**
     * {@inheritDoc}
     */
    @Override
    public SpatialExtent extractSpatialExtent(
        final Entity profileModelNode) {
        final Settable setable = ModelUtils.getSetable(profileModelNode);
        final String text = setable.getValue();
        if ((text != null)
            && (text.length() > 0)) {
            return extractSpatialExtent(text);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertSpatialExtent(
        final SpatialExtent extent,
        final Entity profileModelNode) {
        final Settable setable = ModelUtils.getSetable(profileModelNode);
        if (setable != null) {
            setable.setValue(encodeSpatialExtent(extent));
        }
    }

    /* == Encoding Methods ==== */

    /**
     * Encode spatial extent.
     * @param extent the extent
     * @return the string
     */
    public static String encodeSpatialExtent(
        final SpatialExtent extent) {
        if (extent != null) {
            final Element root = new Element(
                "extent");
            if (extent != null) {
                final ArrayList<Shape> shapes = extent.getShapes();
                for (final Shape shape : shapes) {
                    if (shape instanceof Surface) {
                        root.addContent(encodeSurface((Surface) shape));
                    } else if (shape instanceof Polyline) {
                        root.addContent(encodePolyline((Polyline) shape));
                    } else if (shape instanceof Point) {
                        root.addContent(encodePoint((Point) shape));
                    } else if (shape instanceof BoundingBox) {
                        root.addContent(encodeBoundingBox((BoundingBox) shape));
                    } else if (shape instanceof ExtentId) {
                        root.addContent(encodeExtentId((ExtentId) shape));
                    } else {
                        Reporting.logUnexpected();
                    }
                }
            }
            if (root.getChildren().size() > 0) {
                return JDOMXmlUtils.xmlFromElement(root);
            } else {
                return "";
            }
        }
        return null;
    }

    /**
     * Encode point.
     * @param point the point
     * @return the element
     */
    private static Element encodePoint(
        final Point point) {
        final Element element = new Element(
            "Point");
        String varString = null;
        if (point.getZ() != null) {
            varString = point.getZ().toString();
        } else {
            varString = "";
        }
        final String value = String.format(
            "%1$f %2$f %3$s",
            point.getX(),
            point.getY(),
            varString).trim();

        element.setText(value.trim());
        return element;
    }

    /**
     * Encode polyline.
     * @param polyline the polyline
     * @return the element
     */
    private static Element encodePolyline(
        final Polyline polyline) {
        final Element element = new Element(
            "Polyline");
        for (final Point point : polyline.getPoints()) {
            element.addContent(encodePoint(point));
        }
        return element;
    }

    /**
     * Encode surface.
     * @param surface the surface
     * @return the element
     */
    private static Element encodeSurface(
        final Surface surface) {
        final Element element = new Element(
            "Surface");
        element.setAttribute(
            "inclusive",
            Boolean.toString(surface.isInclusive()));
        for (final Point point : surface.getExteriorPoints()) {
            element.addContent(encodePoint(point));
        }
        for (final SurfaceHole hole : surface.getInteriorHoles()) {
            element.addContent(encodeHole(hole));
        }
        return element;
    }

    /**
     * Encode hole.
     * @param hole the hole
     * @return the element
     */
    private static Element encodeHole(
        final SurfaceHole hole) {
        final Element element = new Element(
            "SurfaceHole");
        for (final Point point : hole.getBoundaryPoints()) {
            element.addContent(encodePoint(point));
        }
        return element;
    }

    /**
     * Encode bounding box.
     * @param bbox the bbox
     * @return the element
     */
    private static Element encodeBoundingBox(
        final BoundingBox bbox) {
        final Element element = new Element(
            "BoundingBox");
        element.setAttribute(
            "inclusive",
            Boolean.toString(bbox.isInclusive()));
        element.addContent(encodePoint(bbox.getNorthWest()));
        element.addContent(encodePoint(bbox.getSouthEast()));
        return element;
    }

    /**
     * Encode extent id.
     * @param extentId the extent id
     * @return the element
     */
    private static Element encodeExtentId(
        final ExtentId extentId) {
        final Element element = new Element(
            "Code");
        element.setText(extentId.getCode());
        element.setAttribute(
            "codeSpace",
            extentId.getCodeSpace());
        element.setAttribute(
            "inclusive",
            Boolean.toString(extentId.isInclusive()));
        return element;
    }

    /* == Decoding Methods ==== */

    /**
     * Extract spatial extent.
     * @param encodedString the encoded string
     * @return the spatial extent
     */
    public static SpatialExtent extractSpatialExtent(
        final String encodedString) {
        final Element element = JDOMXmlUtils.elementFromXml(encodedString);
        if ((element != null)
            && element.getName().equals(
                "extent")) {
            final SpatialExtent se = new SpatialExtent2d();
            final List children = element.getChildren();
            for (final Object childo : children) {
                final Element child = (Element) childo;
                if (child.getName().equals(
                    "Surface")) {
                    se.getShapes().add(
                        extractSurface(child));
                } else if (child.getName().equals(
                    "Polyline")) {
                    se.getShapes().add(
                        extractPolyline(child));
                } else if (child.getName().equals(
                    "Point")) {
                    se.getShapes().add(
                        extractPoint(child));
                } else if (child.getName().equals(
                    "BoundingBox")) {
                    se.getShapes().add(
                        extractBoundingBox(child));
                } else if (child.getName().equals(
                    "Code")) {
                    se.getShapes().add(
                        extractExtentId(child));
                } else {
                    Reporting.logUnexpected();
                }
            }
            return se;
        }
        return new SpatialExtent2d();
    }

    /**
     * Extract point.
     * @param pointElement the point element
     * @return the point2d
     */
    private static Point2d extractPoint(
        final Element pointElement) {
        final String points = pointElement.getTextTrim();
        if ((points != null)
            && (points.length() > 0)) {
            final String[] parts = points.split(
                " ",
                2);
            if (parts.length == 2) {
                return new Point2d(
                    Double.parseDouble(parts[0]),
                    Double.parseDouble(parts[1]));
            }
        }
        return null;
    }

    /**
     * Extract polyline.
     * @param polylineElement the polyline element
     * @return the polyline
     */
    private static Polyline extractPolyline(
        final Element polylineElement) {
        final ArrayList<Point> points = new ArrayList<Point>();
        final List children = polylineElement.getChildren();
        for (final Object childo : children) {
            final Element child = (Element) childo;
            points.add(extractPoint(child));
        }
        return new Polyline2d(
            points);
    }

    /**
     * Extract surface.
     * @param surfaceElement the surface element
     * @return the surface
     */
    private static Surface extractSurface(
        final Element surfaceElement) {
        final ArrayList<Point> points = new ArrayList<Point>();
        final ArrayList<SurfaceHole> holes = new ArrayList<SurfaceHole>();
        final List children = surfaceElement.getChildren();
        for (final Object childo : children) {
            final Element child = (Element) childo;
            if (child.getName().equals(
                "Point")) {
                points.add(extractPoint(child));
            } else if (child.getName().equals(
                "SurfaceHole")) {
                holes.add(extractHole(child));
            } else {
                Reporting.logUnexpected();
            }
        }
        return new Surface2d(
            points,
            holes,
            extractIsInlclusiveProperty(surfaceElement));
    }

    /**
     * Extract hole.
     * @param holeElement the hole element
     * @return the surface hole
     */
    private static SurfaceHole extractHole(
        final Element holeElement) {
        final ArrayList<Point> points = new ArrayList<Point>();
        final List children = holeElement.getChildren();
        for (final Object childo : children) {
            final Element child = (Element) childo;
            if (child.getName().equals(
                "Point")) {
                points.add(extractPoint(child));
            } else {
                Reporting.logUnexpected();
            }
        }
        return new SurfaceHole2d(
            points);
    }

    /**
     * Extract bounding box.
     * @param bboxElement the bbox element
     * @return the shape
     */
    private static Shape extractBoundingBox(
        final Element bboxElement) {
        final List children = bboxElement.getChildren();
        assert (children.size() == 2);
        if (children.size() == 2) {
            return new BoundingBox2d(
                extractPoint((Element) children.get(0)),
                extractPoint((Element) children.get(1)),
                extractIsInlclusiveProperty(bboxElement));
        }
        return null;
    }

    /**
     * Extract extent id.
     * @param extentIdElement the extent id element
     * @return the shape
     */
    private static Shape extractExtentId(
        final Element extentIdElement) {
        final String code = extentIdElement.getTextTrim();
        final String codeSpace = extentIdElement.getAttributeValue("codeSpace");
        return new ExtentId2d(
            code,
            codeSpace,
            extractIsInlclusiveProperty(extentIdElement));
    }

    /**
     * Extract is inlclusive property.
     * @param shapeElement the shape element
     * @return true, if successful
     */
    private static boolean extractIsInlclusiveProperty(
        final Element shapeElement) {
        try {
            return Boolean.parseBoolean(shapeElement
                .getAttributeValue("inclusive"));
        } catch (final Exception e) {
            return false;
        }
    }

    /* == Other methods ==== */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "string";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedNodeNames() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPriority() {
        return 0;
    }
}
