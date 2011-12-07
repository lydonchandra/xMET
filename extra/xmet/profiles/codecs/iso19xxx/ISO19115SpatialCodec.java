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
package xmet.profiles.codecs.iso19xxx;

import java.util.ArrayList;

import n.io.xml.XMLUtils;
import n.reporting.Reporting;
import xmet.profiles.codecs.SpatialExtentCodec;
import xmet.profiles.geometry.BoundingBox;
import xmet.profiles.geometry.ExtentId;
import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.Polyline;
import xmet.profiles.geometry.Shape;
import xmet.profiles.geometry.SpatialExtent;
import xmet.profiles.geometry.Surface;
import xmet.profiles.geometry.SurfaceHole;
import xmet.profiles.geometry.impl2d.ExtentId2d;
import xmet.profiles.geometry.impl2d.GeometryUtils;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Polyline2d;
import xmet.profiles.geometry.impl2d.SpatialExtent2d;
import xmet.profiles.geometry.impl2d.Surface2d;
import xmet.profiles.geometry.impl2d.SurfaceHole2d;
import xmet.profiles.model.ElementAttribute;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.Group;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.Settable;

/**
 * iso19115 implementation of Spatial Codec.
 * @author Nahid Akbar
 */
public class ISO19115SpatialCodec
    implements
    SpatialExtentCodec {

    /**
     * {@inheritDoc}
     */
    @Override
    public SpatialExtent extractSpatialExtent(
        final Entity node) {
        if (node.getQualifiedName().equals(
            "gmd:geographicElement")
            || node.getQualifiedName().equals(
                "gmd:spatialExtent")) {
            final Entity repeatedE = node.getParent();
            if (ModelUtils.isRepeated(repeatedE)) {
                return extractGmdSpatialElements(ModelUtils
                    .asRepeated(repeatedE));
            } else {
                Reporting.reportUnexpected(
                    "%1$s parent repeated entity not found",
                    node.getQualifiedName());
            }
        } else {
            Reporting.logUnexpected("only gmd:extent is supported");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertSpatialExtent(
        final SpatialExtent extent,
        final Entity node) {
        if (extent != null) {
            if (node.getQualifiedName().equals(
                "gmd:geographicElement")
                || node.getQualifiedName().equals(
                    "gmd:spatialExtent")) {
                final Entity repeatedE = node.getParent();
                if (ModelUtils.isRepeated(repeatedE)) {
                    insertSpatialExtentHelper(
                        extent,
                        ModelUtils.asRepeated(repeatedE));
                } else {
                    Reporting.reportUnexpected(
                        "%1$s parent repeated entity not found",
                        node.getQualifiedName());
                }
            } else {
                Reporting.logUnexpected("only gmd:extent is supported");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "iso19115";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedNodeNames() {
        return "gmd:geographicElement|gmd:spatialExtent";
    }

    /**
     * Insert spatial extent_.
     * @param extent the extent
     * @param gmdGeographicElementParent the gmd$geographic element parent
     */
    private void insertSpatialExtentHelper(
        final SpatialExtent extent,
        final Repeated gmdGeographicElementParent) {
        gmdGeographicElementParent.getEntities().clear();
        int i = 1;
        for (final Shape shape : extent.getShapes()) {
            final String gmdGeographicElementQualifiedName = String.format(
                "%2$s[%1$d]",
                i,
                gmdGeographicElementParent.getQualifiedName());
            final Entity gmdGeographicElement = ModelUtils.hardTraceXPath(
                gmdGeographicElementParent.getParent(),
                gmdGeographicElementQualifiedName);
            if (shape instanceof Surface) {
                insertSurface(
                    gmdGeographicElement,
                    (Surface) shape);
                i++;
            } else if (shape instanceof Polyline) {
                insertPolyline(
                    gmdGeographicElement,
                    (Polyline) shape);
                i++;
            } else if (shape instanceof BoundingBox) {
                insertBoundingBox(
                    gmdGeographicElement,
                    (BoundingBox) shape);
                i++;
            } else if (shape instanceof Point) {
                insertPoint(
                    gmdGeographicElement,
                    (Point) shape);
                i++;
            } else if (shape instanceof ExtentId) {
                insertCode(
                    gmdGeographicElement,
                    (ExtentId) shape);
                i++;
            } else {
                Reporting.logUnexpected();
            }
        }
    }

    /**
     * Insert code.
     * @param gmdGeographicElement the gmd$geographic element
     * @param shape the shape
     */
    private void insertCode(
        final Entity gmdGeographicElement,
        final ExtentId shape) {
        Reporting.logExpected("insert code "
            + shape.getCode());
        final Settable gmdCode =
            ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                "gmd:EX_GeographicDescription"
                    + "/gmd:geographicIdentifier"
                    + "/gmd:MD_Identifier"
                    + "/gmd:code"
                    + "/gco:CharacterString"));
        final Settable gmdCodeSpace =
            ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                "gmd:EX_GeographicDescription"
                    + "/gmd:geographicIdentifier"
                    + "/gmd:MD_Identifier"
                    + "/gmd:authority"
                    + "/gmd:CI_Citation"
                    + "/gmd:identifier"
                    + "/gmd:MD_Identifier"
                    + "/gmd:code"
                    + "/gco:CharacterString"));
        // {
        final Settable gcoNilReason1 =
            ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                "gmd:EX_GeographicDescription"
                    + "/gmd:geographicIdentifier"
                    + "/gmd:MD_Identifier"
                    + "/gmd:authority"
                    + "/gmd:CI_Citation"
                    + "/gmd:title"
                    + "/@gco:nilReason"));
        /* if (gco$nilReason != null) { */
        gcoNilReason1.setValue("missing");
        // }
        // }
        // {
        final Settable gcoNilReason2 =
            ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                "gmd:EX_GeographicDescription"
                    + "/gmd:geographicIdentifier"
                    + "/gmd:MD_Identifier"
                    + "/gmd:authority"
                    + "/gmd:CI_Citation"
                    + "/gmd:date"
                    + "/@gco:nilReason"));
        /* if (gco$nilReason != null) { */
        gcoNilReason2.setValue("missing");
        // }
        // }
        gmdCode.setValue(shape.getCode());
        gmdCodeSpace.setValue(shape.getCodeSpace());
        final Settable gmdInclusive =
            ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                "gmd:EX_GeographicDescription/gmd:extentTypeCode"));
        /* inclusive by default so set it to nothing */
        String varValue = null;
        if (shape.isInclusive()) {
            varValue = "";
        } else {
            varValue = "false";
        }
        gmdInclusive.setValue(varValue);
    }

    /**
     * Insert bounding box.
     * @param gmdGeographicElement the gmd$geographic element
     * @param shape the shape
     */
    private void insertBoundingBox(
        final Entity gmdGeographicElement,
        final BoundingBox shape) {
        try {
            ModelUtils.getSetable(
                ModelUtils.hardTraceXPath(
                    gmdGeographicElement,
                    "gmd:EX_GeographicBoundingBox"
                        + "/gmd:northBoundLatitude"
                        + "/gco:Decimal")).setValue(
                Double.toString(shape.getNorthWest().getY()));
            ModelUtils.getSetable(
                ModelUtils.hardTraceXPath(
                    gmdGeographicElement,
                    "gmd:EX_GeographicBoundingBox"
                        + "/gmd:eastBoundLongitude"
                        + "/gco:Decimal")).setValue(
                Double.toString(shape.getSouthEast().getX()));
            ModelUtils.getSetable(
                ModelUtils.hardTraceXPath(
                    gmdGeographicElement,
                    "gmd:EX_GeographicBoundingBox"
                        + "/gmd:southBoundLatitude"
                        + "/gco:Decimal")).setValue(
                Double.toString(shape.getSouthEast().getY()));
            ModelUtils.getSetable(
                ModelUtils.hardTraceXPath(
                    gmdGeographicElement,
                    "gmd:EX_GeographicBoundingBox"
                        + "/gmd:westBoundLongitude"
                        + "/gco:Decimal")).setValue(
                Double.toString(shape.getNorthWest().getX()));
            final Settable gmdInclusive =
                ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                    gmdGeographicElement,
                    "gmd:EX_GeographicBoundingBox/gmd:extentTypeCode"));
            /* inclusive by default so set it to nothing */
            String varValue = null;
            if (shape.isInclusive()) {
                varValue = "";
            } else {
                varValue = "false";
            }
            gmdInclusive.setValue(varValue);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
    }

    /**
     * Insert point.
     * @param gmdGeographicElement the gmd$geographic element
     * @param shape the shape
     */
    private void insertPoint(
        final Entity gmdGeographicElement,
        final Point shape) {
        final Settable setable =
            ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                "gmd:EX_BoundingPolygon/gmd:polygon/gml:Point/gml:pos"));
        setable.setValue(shape.getY()
            + " "
            + shape.getX());
        final Settable gmdInclusive =
            ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                "gmd:EX_BoundingPolygon/gmd:extentTypeCode"));
        /* inclusive by default so set it to nothing */
        gmdInclusive.setValue("");
        fixGmlId((ElementDeclaration) ModelUtils.softTraceXPath(
            gmdGeographicElement,
            "gmd:EX_BoundingPolygon/gmd:polygon/gml:Point"));
    }

    /**
     * Insert polyline.
     * @param gmdGeographicElement the gmd$geographic element
     * @param shape the shape
     */
    private void insertPolyline(
        final Entity gmdGeographicElement,
        final Polyline shape) {
        final StringBuilder sb = new StringBuilder();
        for (final Point exteriorPoint : shape.getPoints()) {
            sb.append(exteriorPoint.getY());
            sb.append(" ");
            sb.append(exteriorPoint.getX());
            sb.append(" ");
        }
        final String value = sb.toString().trim();
        if (value.length() > 0) {
            final Settable setable =
                ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                    gmdGeographicElement,
                    "gmd:EX_BoundingPolygon"
                        + "/gmd:polygon"
                        + "/gml:LineString"
                        + "/gml:posList"));
            setable.setValue(value);
            final Settable gmdInclusive =
                ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                    gmdGeographicElement,
                    "gmd:EX_BoundingPolygon/gmd:extentTypeCode"));
            /* inclusive by default so set it to nothing */
            gmdInclusive.setValue("");
            fixGmlId((ElementDeclaration) ModelUtils.softTraceXPath(
                gmdGeographicElement,
                "gmd:EX_BoundingPolygon/gmd:polygon/gml:LineString"));
        }
    }

    /**
     * Insert surface.
     * @param gmdGeographicElement the gmd$geographic element
     * @param shape the shape
     */
    private void insertSurface(
        final Entity gmdGeographicElement,
        final Surface shape) {

        final Entity gmlLinearRing = ModelUtils.hardTraceXPath(
            gmdGeographicElement,
            "gmd:EX_BoundingPolygon"
                + "/gmd:polygon"
                + "/gml:Polygon"
                + "/gml:exterior"
                + "/gml:LinearRing");
        int i = 1;
        for (final Point exteriorPoint : shape.getExteriorPoints()) {
            final Settable setable =
                ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                    gmlLinearRing,
                    String.format(
                        "gml:pos[%1$d]",
                        i++)));
            if (setable != null) {
                setable.setValue(exteriorPoint.getY()
                    + " "
                    + exteriorPoint.getX());
            }
            if (i == 2) {
                final Entity gmlPos = ModelUtils.hardTraceXPath(
                    gmlLinearRing,
                    String.format("gml:pos[1]"));
                if (gmlPos != null) {
                    final Repeated rep = (Repeated) gmlPos.getParent();
                    while (rep.getEntities().size() > 1) {
                        rep.removeEntityByIndex(1);
                    }
                }
            }
        }
        fixGmlId((ElementDeclaration) ModelUtils.softTraceXPath(
            gmdGeographicElement,
            "gmd:EX_BoundingPolygon/gmd:polygon/gml:Polygon"));
        int holeCounter = 1;
        for (final SurfaceHole hole : shape.getInteriorHoles()) {
            final Entity gmlLinearRing2 = ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                String.format(
                    "gmd:EX_BoundingPolygon"
                        + "/gmd:polygon"
                        + "/gml:Polygon"
                        + "/gml:interior[%1$d]"
                        + "/gml:LinearRing",
                    holeCounter));
            if (holeCounter == 1) {
                final Entity gmlInterior = ModelUtils.hardTraceXPath(
                    gmdGeographicElement,
                    String.format("gmd:EX_BoundingPolygon"
                        + "/gmd:polygon"
                        + "/gml:Polygon"
                        + "/gml:interior[1]"));
                if (gmlInterior != null) {
                    final Repeated rep = (Repeated) gmlInterior.getParent();
                    while (rep.getEntities().size() > 1) {
                        rep.removeEntityByIndex(1);
                    }
                }
            }
            int i2 = 1;
            for (final Point exteriorPoint : hole.getBoundaryPoints()) {
                final Settable setable =
                    ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                        gmlLinearRing2,
                        String.format(
                            "gml:pos[%1$d]",
                            i2++)));
                if (setable != null) {
                    setable.setValue(exteriorPoint.getY()
                        + " "
                        + exteriorPoint.getX());
                }
                if (i2 == 2) {
                    final Entity gmlPos = ModelUtils.hardTraceXPath(
                        gmlLinearRing2,
                        String.format("gml:pos[1]"));
                    if (gmlPos != null) {
                        final Repeated rep = (Repeated) gmlPos.getParent();
                        while (rep.getEntities().size() > 1) {
                            rep.removeEntityByIndex(1);
                        }
                    }
                }
            }
            holeCounter++;
        }
        final Settable gmdInclusive =
            ModelUtils.getSetable(ModelUtils.hardTraceXPath(
                gmdGeographicElement,
                "gmd:EX_BoundingPolygon/gmd:extentTypeCode"));
        /* inclusive by default so set it to nothing */
        String varValue = null;
        if (shape.isInclusive()) {
            varValue = "";
        } else {
            varValue = "false";
        }
        gmdInclusive.setValue(varValue);
    }

    /**
     * Extractgmd$spatial elements.
     * @param gmdGeographicElements the gmd$geographic elements
     * @return the spatial extent
     */
    private SpatialExtent extractGmdSpatialElements(
        final Repeated gmdGeographicElements) {
        final SpatialExtent spatialExtent = new SpatialExtent2d();
        for (final Entity gmdGeographicElemente : gmdGeographicElements
            .getEntities()) {
            final ElementDeclaration gmdGeographicElement =
                ModelUtils.asElementDeclaration(gmdGeographicElemente);
            if (gmdGeographicElement != null) {
                if (ModelUtils.softHasChildren(
                    gmdGeographicElement,
                    "gmd:EX_GeographicBoundingBox")) {
                    final Shape extractBoundingBox =
                        extractBoundingBox(ModelUtils
                            .asElementDeclaration(ModelUtils
                                .softGetEntityChild(
                                    gmdGeographicElement,
                                    "gmd:EX_GeographicBoundingBox")));
                    if (extractBoundingBox != null) {
                        spatialExtent.getShapes().add(
                            extractBoundingBox);
                    }
                } else if (ModelUtils.softHasChildren(
                    gmdGeographicElement,
                    "gmd:EX_GeographicDescription")) {
                    final Shape extractCode =
                        extractCode(ModelUtils.asElementDeclaration(ModelUtils
                            .softGetEntityChild(
                                gmdGeographicElement,
                                "gmd:EX_GeographicDescription")));
                    if (extractCode != null) {
                        spatialExtent.getShapes().add(
                            extractCode);
                    }
                } else if (ModelUtils.softHasChildren(
                    gmdGeographicElement,
                    "gmd:EX_BoundingPolygon")) {
                    final Shape extractGMLSurface =
                        extractGMLSurface(ModelUtils
                            .asElementDeclaration(ModelUtils
                                .softGetEntityChild(
                                    gmdGeographicElement,
                                    "gmd:EX_BoundingPolygon")));
                    if (extractGMLSurface != null) {
                        spatialExtent.getShapes().add(
                            extractGMLSurface);
                    }
                } else {
                    Reporting.logUnexpected();
                }
            }
        }
        return spatialExtent;
    }

    /**
     * Extract code.
     * @param gmdExGeographicDescription the gmd$ e x_ geographic description
     * @return the shape
     */
    private Shape extractCode(
        final ElementDeclaration gmdExGeographicDescription) {
        final Settable code = ModelUtils.softTraceSetableXpath(
            gmdExGeographicDescription,
            "gmd:geographicIdentifier"
                + "/gmd:MD_Identifier"
                + "/gmd:code"
                + "/gco:CharacterString");
        final Settable codeSpace = ModelUtils.softTraceSetableXpath(
            gmdExGeographicDescription,
            "gmd:geographicIdentifier"
                + "/gmd:MD_Identifier"
                + "/gmd:authority"
                + "/gmd:CI_Citation"
                + "/gmd:identifier"
                + "/gmd:MD_Identifier"
                + "/gmd:code"
                + "/gco:CharacterString");
        if (code != null) {
            Reporting.logExpected("extract code "
                + code.getValue());
            if (code.getValue().equals(
                "Aus4_1")) {
                Reporting.logExpected("->>>"
                    + code.getValue());
            }
            String varCodeSpace = null;
            if (codeSpace != null) {
                varCodeSpace = codeSpace.getValue();
            } else {
                varCodeSpace = "";
            }
            return new ExtentId2d(
                code.getValue(),
                varCodeSpace,
                isInclusive(gmdExGeographicDescription));
        } else {
            return null;
        }
    }

    /**
     * Extract gml surface.
     * @param gmdExBPoly the gmd$ e x_ bounding polygon
     * @return the shape
     */
    private Shape extractGMLSurface(
        final ElementDeclaration gmdExBPoly) {
        if (gmdExBPoly != null) {
            ElementDeclaration gmdPolygon =
                ModelUtils.asElementDeclaration(ModelUtils.softGetEntityChild(
                    gmdExBPoly,
                    "gmd:polygon"));
            if (gmdPolygon != null) {
                final Repeated gmdPolygons =
                    ModelUtils.asRepeated(gmdPolygon.getParent());
                if (gmdPolygons != null) {
                    for (final Entity gmdPolygono : gmdPolygons) {
                        gmdPolygon =
                            ModelUtils.asElementDeclaration(gmdPolygono);
                        if (gmdPolygon != null) {
                            final Group group =
                                ModelUtils.extractEntityGroup(gmdPolygon);
                            if (group != null) {
                                if (group.getChildren().size() > 1) {
                                    Reporting.logUnexpected();
                                }
                                if (group.getChildren().size() > 0) {
                                    for (final Entity solids : group
                                        .getChildren()) {
                                        final ElementDeclaration element =
                                            ModelUtils
                                                .asElementDeclaration(solids);
                                        if (element.getQualifiedName().equals(
                                            "gml:Point")) {
                                            return extractPoint(element);
                                        } else if (element
                                            .getQualifiedName()
                                            .equals(
                                                "gml:LineString")) {
                                            return extractPolyline(element);
                                        } else if (element
                                            .getQualifiedName()
                                            .equals(
                                                "gml:Polygon")) {
                                            return extractSurface(
                                                element,
                                                isInclusive(gmdExBPoly));
                                        } else {
                                            Reporting.logUnexpected(
                                                "enhandled gml shape %1$s",
                                                element.getQualifiedName());
                                        }
                                    }
                                }
                            } else {
                                Reporting.logUnexpected();
                            }
                        } else {
                            Reporting.logUnexpected();
                        }
                    }
                } else {
                    Reporting.logUnexpected("gmd:polygon parent repeated"
                        + " entity could not be traced");
                }
            }
            // else {
            // /* Reporting.unimplemented("gmd:polygon could not be traced");
            // */
            // /* gmdPolygon = ModelUtils.asElementDeclaration(ModelUtils */
            // // .softGetEntityChild(gmdExBoundingPolygon,
            // // "gmd:polygon"));
            // }
        }
        return null;
    }

    /**
     * Checks if is inclusive.
     * @param gmdExBoundingPolygon the gmd$ e x_ bounding polygon
     * @return true, if is inclusive
     */
    private boolean isInclusive(
        final ElementDeclaration gmdExBoundingPolygon) {
        final Settable inclusive = ModelUtils.softTraceSetableXpath(
            gmdExBoundingPolygon,
            "gmd:extentTypeCode");

        return (inclusive == null)
            || (inclusive.getValue() == null)
            || inclusive.getValue().equals(
                "1")
            || inclusive.getValue().toLowerCase().equals(
                "true")
            || inclusive.getValue().toLowerCase().equals(
                "");
    }

    /**
     * Extract point.
     * @param gmlPoint the gml$ point
     * @return the point
     */
    private Point extractPoint(
        final ElementDeclaration gmlPoint) {
        if (gmlPoint != null) {
            try {
                final String point = ModelUtils.getSetable(
                    ModelUtils.softTraceXPath(
                        gmlPoint,
                        "gml:pos")).getValue();
                if ((point != null)
                    && (point.length() > 0)) {
                    final String[] points = point.split(" ");
                    if (points.length >= 2) {
                        return new Point2d(
                            Double.parseDouble(points[1]),
                            Double.parseDouble(points[0]));
                    }
                } else {
                    Reporting.logUnexpected();
                }
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }
        return null;
    }

    /**
     * Extract polyline.
     * @param gmlLineString the gml$ line string
     * @return the shape
     */
    private Shape extractPolyline(
        final ElementDeclaration gmlLineString) {
        try {
            final Settable setable =
                ModelUtils.getSetable(ModelUtils.softTraceXPath(
                    gmlLineString,
                    "gml:posList"));
            final String value = setable.getValue();
            final String[] points = value.split("\\s+");
            final ArrayList<Point> nPoints = new ArrayList<Point>();
            for (int i = 0; i < points.length; i += 2) {
                nPoints.add(new Point2d(
                    Double.parseDouble(points[i + 1]),
                    Double.parseDouble(points[i])));
            }
            return new Polyline2d(
                nPoints);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
        return null;
    }

    /**
     * Extract surface.
     * @param gmlPolygon the gml$ polygon
     * @param inclusive the inclusive
     * @return the shape
     */
    private Shape extractSurface(
        final ElementDeclaration gmlPolygon,
        final boolean inclusive) {
        try {
            final Surface2d surface = new Surface2d();
            surface.setInclusive(inclusive);
            final ElementDeclaration gmlExterior =
                ModelUtils.asElementDeclaration(ModelUtils.softTraceXPath(
                    gmlPolygon,
                    "gml:exterior"));
            if (gmlExterior != null) {
                final ArrayList<Point2d> points =
                    extractLinearRingPoints(ModelUtils.softTraceXPath(
                        gmlExterior,
                        "gml:LinearRing"));
                surface.getExteriorPoints().addAll(
                    points);
                /* String gmlPosList = ModelUtils.getSetable( */
                // (ModelUtils.hardTraceXPath(gmlExterior,
                // "gml:LinearRing/gml:posList"))).getValue();
                /* String[] points2 = gmlPosList.split("\\s+"); */
                /* for (int i = 0; i < points2.length; i += 2) { */
                /* surface.getExteriorPoints().add( */
                /* new Point2d(Double.parseDouble(points2[i]), Double */
                // .parseDouble(points2[i + 1])));
                // }
            }
            final Group parentGroup = ModelUtils.extractEntityGroup(gmlPolygon);
            if (parentGroup != null) {
                for (final Entity e : parentGroup.getChildren()) {
                    if (e.getQualifiedName().equals(
                        "gml:interior")) {
                        final Repeated gmlInteriors = ModelUtils.asRepeated(e);
                        if (gmlInteriors != null) {
                            for (final Entity gmlInteriore : gmlInteriors
                                .getEntities()) {
                                final ElementDeclaration gmlInterior =
                                    ModelUtils
                                        .asElementDeclaration(gmlInteriore);
                                if (gmlInterior != null) {
                                    final ArrayList<Point2d> points =
                                        extractLinearRingPoints(ModelUtils
                                            .softTraceXPath(
                                                gmlInterior,
                                                "gml:LinearRing"));

                                    if ((points != null)
                                        && (points.size() > 1)) {
                                        try {
                                            final SurfaceHole2d hole =
                                                new SurfaceHole2d();
                                            hole.getBoundaryPoints().addAll(
                                                points);
                                            surface.getInteriorHoles().add(
                                                hole);
                                        } catch (final Exception ex) {
                                            Reporting.reportUnexpected(ex);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Reporting.logUnexpected();
            }
            if ((surface.getExteriorPoints().size() > 0)
                || (surface.getInteriorHoles().size() > 0)) {
                return surface;
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
        return null;
    }

    /**
     * Extract linear ring points.
     * @param gmlLinearRing the gml$ linear ring
     * @return the array list
     */
    private ArrayList<Point2d> extractLinearRingPoints(
        final Entity gmlLinearRing) {
        final ArrayList<Point2d> points = new ArrayList<Point2d>();

        final Entity gmlPosListEntity = ModelUtils.softTraceXPath(
            gmlLinearRing,
            "gml:posList");
        if (gmlPosListEntity != null) {
            final String gmlPosList = ModelUtils.getSetable(
                (gmlPosListEntity)).getValue();
            if (gmlPosList != null) {
                try {
                    final String[] pts = gmlPosList.split("\\s+");
                    for (int i = 0; i < pts.length; i += 2) {
                        points.add(new Point2d(
                            Double.parseDouble(pts[i + 1]),
                            Double.parseDouble(pts[i])));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            final Entity gmlPosEntity = ModelUtils.softTraceXPath(
                gmlLinearRing,
                "gml:pos");
            if ((gmlPosEntity != null)
                && ModelUtils.isRepeated(gmlPosEntity.getParent())) {
                final Repeated repeated = (Repeated) gmlPosEntity.getParent();
                for (final Entity e : repeated) {
                    try {
                        if (e.getQualifiedName().equals(
                            "gml:pos")) {
                            final Settable setable = ModelUtils.getSetable(e);
                            final String[] pts = setable.getValue().split(
                                "\\s+");
                            points.add(new Point2d(
                                Double.parseDouble(pts[1]),
                                Double.parseDouble(pts[0])));
                        } else {
                            Reporting.logUnexpected(e.getQualifiedName());
                        }
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                Reporting.logUnexpected();
            }
        }
        return points;
    }

    /**
     * Extract bounding box.
     * @param gmdExGeographicBoundingBox the gmd$ e x_ geographic bounding box
     * @return the shape
     */
    private Shape extractBoundingBox(
        final ElementDeclaration gmdExGeographicBoundingBox) {
        try {
            final double east = Double.parseDouble(ModelUtils.getSetable(
                ModelUtils.softTraceXPath(
                    gmdExGeographicBoundingBox,
                    "gmd:eastBoundLongitude/gco:Decimal")).getValue());
            final double west = Double.parseDouble(ModelUtils.getSetable(
                ModelUtils.softTraceXPath(
                    gmdExGeographicBoundingBox,
                    "gmd:westBoundLongitude/gco:Decimal")).getValue());
            final double north = Double.parseDouble(ModelUtils.getSetable(
                ModelUtils.softTraceXPath(
                    gmdExGeographicBoundingBox,
                    "gmd:northBoundLatitude/gco:Decimal")).getValue());
            final double south = Double.parseDouble(ModelUtils.getSetable(
                ModelUtils.softTraceXPath(
                    gmdExGeographicBoundingBox,
                    "gmd:southBoundLatitude/gco:Decimal")).getValue());
            return GeometryUtils.create2DBoundingBoxSurface(
                north,
                east,
                south,
                west,
                isInclusive(gmdExGeographicBoundingBox));
        } catch (final Exception e) {
            e.printStackTrace(); /* Reporting.exception(e); */
        }
        return null;
    }

    /**
     * FixgmlId.
     * @param gmlElement the gml$ element
     */
    public static void fixGmlId(
        final ElementDeclaration gmlElement) {
        if (gmlElement != null) {
            if (gmlElement.getAttributes() != null) {
                final ElementAttribute gmlId =
                    gmlElement.getAttributes().getAttributeByName(
                        "gml:id");
                if (gmlId != null) {
                    if (gmlId.getValue() == null
                        || gmlId.getValue().trim().length() == 0) {
                        gmlId.setValue(XMLUtils.MISC
                            .generateRandomNCName(gmlElement
                                .getQualifiedName()
                                .substring(
                                    gmlElement.getQualifiedName().indexOf(
                                        ':') + 1)));
                    }
                }
            }
        }
    }

}
