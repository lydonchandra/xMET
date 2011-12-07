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

/**
 * A drawing item for a polygon surface.
 * @author Nahid Akbar
 */
public class ESurfaceDT
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
        final int[] xpoints = new int[points.size() + 1];
        final int[] ypoints = new int[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            final Point2D point = dc.translatePointToCanvas(points.get(i));
            xpoints[i] = (int) point.getX();
            ypoints[i] = (int) point.getY();
        }
        final Point2D point = dc.translatePointToCanvas(latestPoint);
        xpoints[points.size()] = (int) point.getX();
        ypoints[points.size()] = (int) point.getY();
        g.setColor(dc.getDrawingFillColor());
        g.fillPolygon(
            xpoints,
            ypoints,
            xpoints.length);
        g.setColor(Color.WHITE);
        g.drawPolygon(
            xpoints,
            ypoints,
            xpoints.length);
        g.setColor(Color.BLACK);
        for (int i = 0; i < points.size(); i++) {
            xpoints[i]++;
            ypoints[i]++;
        }
        g.drawPolygon(
            xpoints,
            ypoints,
            xpoints.length);
        for (int i = 0; i < xpoints.length; i++) {
            dc.drawModifierPoint(
                g,
                xpoints[i] - 1,
                ypoints[i] - 1);
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
        return points.size() > 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item getDrawnItem() {
        return new ESurface(
            points,
            new ArrayList<ESurfaceHole>(),
            true);
    }

}
