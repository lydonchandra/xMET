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
package xmet.ui.mapping.swingxws;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import n.reporting.Reporting;
import n.ui.patterns.wysiwyg.DrawingContext;
import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.SurfaceHole;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Surface2d;
import xmet.profiles.geometry.impl2d.SurfaceHole2d;

/**
 * A polygon surface item.
 * @author Nahid Akbar
 */
public class ESurface
    extends EAbstractShape {

    /** The points. */
    private final ArrayList<Point2D> points;

    /** The holes. */
    private final ArrayList<ESurfaceHole> holes;

    /**
     * Instantiates a new e surface.
     * @param aPoints the points
     * @param aHoles the holes
     * @param inclusive the inclusive
     */
    public ESurface(
        final ArrayList<Point2D> aPoints,
        final ArrayList<ESurfaceHole> aHoles,
        final boolean inclusive) {
        super(inclusive);
        this.points = aPoints;
        this.holes = aHoles;
    }

    /**
     * Instantiates a new e surface.
     * @param surface the surface
     */
    public ESurface(
        final Surface2d surface) {
        super(surface.isInclusive());
        points = new ArrayList<Point2D>();
        for (final Point point : surface.getExteriorPoints()) {
            final Point2d pt = (Point2d) point;
            points.add(new Point2D.Double(
                pt.getX(),
                pt.getY()));
        }

        holes = new ArrayList<ESurfaceHole>();
        for (final SurfaceHole hole : surface.getInteriorHoles()) {
            final SurfaceHole2d hl = (SurfaceHole2d) hole;
            holes.add(new ESurfaceHole(
                hl));
        }
    }

    /**
     * Gets the surface.
     * @return the surface
     */
    Surface2d getSurface() {
        final Surface2d surface = new Surface2d();
        surface.setInclusive(isInclusive());
        for (final Point2D point : points) {
            surface.getExteriorPoints().add(
                new Point2d(
                    point.getX(),
                    point.getY()));
        }
        for (final ESurfaceHole hole : holes) {
            surface.getInteriorHoles().add(
                hole.extractSurfaceHole());
        }
        return surface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
        final Point2D point,
        final DrawingContext dc) {
        final int[] xpoints = new int[points.size()];
        final int[] ypoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            final Point2D point2d = dc.translatePointToCanvas(points.get(i));
            xpoints[i] = (int) point2d.getX();
            ypoints[i] = (int) point2d.getY();
        }
        final Polygon cachedPolygon = new Polygon(
            xpoints,
            ypoints,
            xpoints.length);
        if (cachedPolygon.contains(dc.translatePointToCanvas(point))) {
            return true;
        }
        for (final ESurfaceHole hole : holes) {
            if (hole.contains(
                point,
                dc)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2D getModifierPointAt(
        final Point2D point,
        final double modifierDiamH,
        final double modifierDiamV) {
        for (final Point2D p : points) {
            if (EPosition.isNearPoint(
                p.getX(),
                p.getY(),
                point.getX(),
                point.getY(),
                modifierDiamH,
                modifierDiamV)) {
                return p;
            }
        }
        for (final ESurfaceHole hole : holes) {
            final Point2D p = hole.getModifierPointAt(
                point,
                modifierDiamH,
                modifierDiamV);
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawItem(
        final Graphics g,
        final DrawingContext dc) {
        g.setColor(getMainColor());
        Reporting.logExpected(
            "surface %1$s",
            getMainColor());
        final int[] xpoints = new int[points.size()];
        final int[] ypoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            final Point2D point = dc.translatePointToCanvas(points.get(i));
            xpoints[i] = (int) point.getX();
            ypoints[i] = (int) point.getY();
        }
        g.fillPolygon(
            xpoints,
            ypoints,
            xpoints.length);
        for (final ESurfaceHole hole : holes) {
            hole.setIncColor(getMainColor());
            hole.setExcColor(getAltColor());
            hole.drawItem(
                g,
                dc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(
        final double dx,
        final double dy) {
        for (final Point2D p : points) {
            p.setLocation(
                p.getX()
                    + dx,
                p.getY()
                    + dy);
        }
        for (final ESurfaceHole hole : holes) {
            hole.translate(
                dx,
                dy);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translateModifierPoint(
        final Point2D p,
        final double dx,
        final double dy) {
        p.setLocation(
            p.getX()
                + dx,
            p.getY()
                + dy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawSelectedOverlay(
        final Graphics g,
        final double dx,
        final double dy,
        final DrawingContext dc) {
        drawItem(
            g,
            dc);
        g.setColor(dc.getSelectionOverlayColor());
        final int[] xpoints = new int[points.size()];
        final int[] ypoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            final Point2D point = dc.translatePointToCanvas(new Point2D.Double(
                points.get(
                    i).getX()
                    + dx,
                points.get(
                    i).getY()
                    + dy));
            xpoints[i] = (int) point.getX();
            ypoints[i] = (int) point.getY();
        }
        g.fillPolygon(
            xpoints,
            ypoints,
            xpoints.length);
        for (int i = 0; i < points.size(); i++) {
            dc.drawModifierPoint(
                g,
                xpoints[i],
                ypoints[i]);
        }
        for (final ESurfaceHole hole : holes) {
            hole.setIncColor(getMainColor());
            hole.setExcColor(getAltColor());
            hole.drawSelectedOverlay(
                g,
                dx,
                dy,
                dc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawResizingOverlay(
        final Graphics g,
        final double dx,
        final double dy,
        final Point2D p,
        final DrawingContext dc) {
        drawItem(
            g,
            dc);
        g.setColor(dc.getSelectionOverlayColor());
        final int[] xpoints = new int[points.size()];
        final int[] ypoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            final Point2D pt = points.get(i);
            if (pt == p) {
                final Point2D point =
                    dc.translatePointToCanvas(new Point2D.Double(
                        pt.getX()
                            + dx,
                        pt.getY()
                            + dy));
                xpoints[i] = (int) point.getX();
                ypoints[i] = (int) point.getY();
            } else {
                final Point2D point =
                    dc.translatePointToCanvas(new Point2D.Double(
                        pt.getX(),
                        pt.getY()));
                xpoints[i] = (int) point.getX();
                ypoints[i] = (int) point.getY();
            }
        }
        g.fillPolygon(
            xpoints,
            ypoints,
            xpoints.length);
        for (int i = 0; i < points.size(); i++) {
            dc.drawModifierPoint(
                g,
                xpoints[i],
                ypoints[i]);
        }
        for (final ESurfaceHole hole : holes) {
            hole.setIncColor(getMainColor());
            hole.setExcColor(getAltColor());
            hole.drawResizingOverlay(
                g,
                dx,
                dy,
                p,
                dc);
        }
    }

    /**
     * Gets the holes.
     * @return the holes
     */
    public ArrayList<ESurfaceHole> getHoles() {
        return holes;
    }
}
