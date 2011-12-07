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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import n.ui.patterns.wysiwyg.DrawingContext;
import n.ui.patterns.wysiwyg.Item;
import n.ui.patterns.wysiwyg.ItemDrawingTool;
import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Polyline2d;

/**
 * A drawing tool for line item.
 * @author Nahid Akbar
 */
public class ELineDT
    extends ItemDrawingTool {

    /** The points. */
    private final ArrayList<Point2D> points = new ArrayList<Point2D>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawInProgress(
        final Graphics g,
        final Point2D latestPoint,
        final DrawingContext dc) {
        final ArrayList<Point2D> aPoints = new ArrayList<Point2D>();

        for (final Point2D point2d : this.points) {
            aPoints.add(dc.translatePointToCanvas(point2d));
        }
        aPoints.add(dc.translatePointToCanvas(latestPoint));
        g.setColor(Color.WHITE);
        for (int i = 0; i < aPoints.size(); i++) {
            final Point2D point = aPoints.get(i);
            if (i != 0) {
                g.drawLine(
                    (int) (aPoints.get(i - 1).getX()),
                    (int) (aPoints.get(i - 1).getY()),
                    (int) (point.getX()),
                    (int) (point.getY()));
            }
        }
        g.setColor(Color.BLACK);
        for (int i = 0; i < aPoints.size(); i++) {
            final Point2D point = aPoints.get(i);
            if (i != 0) {
                g.drawLine(
                    (int) (aPoints.get(i - 1).getX()) + 1,
                    (int) (aPoints.get(i - 1).getY()) + 1,
                    (int) (point.getX()) + 1,
                    (int) (point.getY()) + 1);
            }
        }

        g.setColor(dc.getDrawingFillColor());
        for (int i = 0; i < aPoints.size(); i++) {
            final Point2D point = aPoints.get(i);
            dc.drawModifierPoint(
                g,
                point.getX(),
                point.getY());
            if (i != 0) {
                g.drawLine(
                    (int) (aPoints.get(i - 1).getX()),
                    (int) (aPoints.get(i - 1).getY()),
                    (int) (point.getX()),
                    (int) (point.getY()));
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addPoint(
        final Point2D point) {
        points.add(point);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean finishDrawingItem() {
        return points.size() > 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item getDrawnItem() {
        final ArrayList<Point> aPoints = new ArrayList<Point>();
        for (final Point2D point2d : this.points) {
            aPoints.add(new Point2d(
                point2d.getX(),
                point2d.getY()));
        }
        return new ELine(
            new Polyline2d(
                aPoints));
    }

}
