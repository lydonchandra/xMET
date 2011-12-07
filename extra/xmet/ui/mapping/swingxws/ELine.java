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

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import n.ui.patterns.wysiwyg.DrawingContext;
import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Polyline2d;

/**
 * A line item.
 * @author Nahid Akbar
 */
public class ELine
    extends EAbstractShape {

    /** The line. */
    private final Polyline2d line;

    /* ArrayList<Point2D> points; */

    /* public ELine(final ArrayList<Point2D> points) { */
    /* super(true); */
    /* this.points = points; */
    // }

    /**
     * Instantiates a new e line.
     * @param aLine the line
     */
    public ELine(
        final Polyline2d aLine) {
        super(true);
        this.line = aLine;
    }

    /**
     * Gets the line.
     * @return the line
     */
    public Polyline2d getLine() {
        return line;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
        final Point2D point,
        final DrawingContext dc) {
        for (int i = 1; i < line.getPoints().size(); i++) {
            final Point2d p = (Point2d) line.getPoints().get(
                i);
            final Point2d l = (Point2d) line.getPoints().get(
                i - 1);
            if (testLine(
                l.getX(),
                l.getY(),
                p.getX(),
                p.getY(),
                point.getX(),
                point.getY(),
                dc.pixelsToLengthH(dc.getModifierPointDiameter()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test line.
     * @param x1 the x1
     * @param y1 the y1
     * @param x2 the x2
     * @param y2 the y2
     * @param x the x
     * @param y the y
     * @param d the d
     * @return true, if successful
     */
    private boolean testLine(
        final double x1,
        final double y1,
        final double x2,
        final double y2,
        final double x,
        final double y,
        final double d) {
        final Line2D aLine = new Line2D.Double(
            x1,
            y1,
            x2,
            y2);
        return aLine.intersects(
            x
                - d,
            y
                - d,
            d,
            d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2D getModifierPointAt(
        final Point2D point,
        final double modifierDiamH,
        final double modifierDiamV) {
        for (final Point pt : line.getPoints()) {
            if (EPosition.isNearPoint(
                pt.getX(),
                pt.getY(),
                point.getX(),
                point.getY(),
                modifierDiamH,
                modifierDiamH)) {
                return new Point2D.Double(
                    pt.getX(),
                    pt.getY());
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
        final Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(
            2));
        g.setColor(getMainColor());
        final ArrayList<Point2D> points = new ArrayList<Point2D>(
            line.getPoints().size());
        for (int i = 0; i < line.getPoints().size(); i++) {
            final Point pt = line.getPoints().get(
                i);
            points.add(
                i,
                dc.translatePointToCanvas(new Point2D.Double(
                    pt.getX(),
                    pt.getY())));
        }
        for (int i = 1; i < points.size(); i++) {
            final Point2D p = points.get(i);
            final Point2D l = points.get(i - 1);
            g2d.drawLine(
                (int) l.getX(),
                (int) l.getY(),
                (int) p.getX(),
                (int) p.getY());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(
        final double dx,
        final double dy) {
        for (final Point p : line.getPoints()) {
            final Point2d pt = (Point2d) p;
            pt.setLocation(
                p.getX()
                    + dx,
                p.getY()
                    + dy);
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
        for (final Point point : line.getPoints()) {
            if ((point.getX() == p.getX())
                && (point.getY() == p.getY())) {
                ((Point2d) point).setLocation(
                    p.getX()
                        + dx,
                    p.getY()
                        + dy);
                break;
            }
        }
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

        final ArrayList<Point2D> points = new ArrayList<Point2D>(
            line.getPoints().size());
        for (int i = 0; i < line.getPoints().size(); i++) {
            final Point pt = line.getPoints().get(
                i);
            points.add(
                i,
                dc.translatePointToCanvas(new Point2D.Double(
                    pt.getX()
                        + dx,
                    pt.getY()
                        + dy)));
        }

        g.setColor(dc.getSelectionOverlayColor());
        for (int i = 0; i < points.size(); i++) {
            final Point2D p = points.get(i);
            dc.drawModifierPoint(
                g,
                p.getX(),
                p.getY());
            if (i != 0) {
                final Point2D l = points.get(i - 1);
                g.drawLine(
                    (int) l.getX(),
                    (int) l.getY(),
                    (int) p.getX(),
                    (int) p.getY());
            }
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
        final Point2D point,
        final DrawingContext dc) {

        drawItem(
            g,
            dc);

        final ArrayList<Point2D> points = new ArrayList<Point2D>(
            line.getPoints().size());
        // {
        boolean translated = false;
        for (int i = 0; i < line.getPoints().size(); i++) {
            final Point pt = line.getPoints().get(
                i);
            if (!translated
                && (pt.getX() == point.getX())
                && (pt.getY() == point.getY())) {
                points.add(
                    i,
                    dc.translatePointToCanvas(new Point2D.Double(
                        pt.getX()
                            + dx,
                        pt.getY()
                            + dy)));
                translated = true;
            } else {
                points.add(
                    i,
                    dc.translatePointToCanvas(new Point2D.Double(
                        pt.getX(),
                        pt.getY())));
            }
        }
        // }
        g.setColor(dc.getSelectionOverlayColor());
        for (int i = 0; i < points.size(); i++) {
            final Point2D p = points.get(i);
            dc.drawModifierPoint(
                g,
                p.getX(),
                p.getY());
            if (i != 0) {
                final Point2D l = points.get(i - 1);
                g.drawLine(
                    (int) l.getX(),
                    (int) l.getY(),
                    (int) p.getX(),
                    (int) p.getY());
            }
        }
    }
}
