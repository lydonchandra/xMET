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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import n.ui.patterns.wysiwyg.DrawingContext;
import n.ui.patterns.wysiwyg.Item;
import n.ui.patterns.wysiwyg.ItemDrawingTool;

/**
 * Bounding box item drawing tool.
 * @author Nahid Akbar
 */
public class EBoundingBoxDT
    extends ItemDrawingTool {

    /** The x1. */
    private double x1 = -1;

    /** The y1. */
    private double y1 = -1;

    /** The x2. */
    private double x2 = -1;

    /** The y2. */
    private double y2 = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawInProgress(
        final Graphics g,
        final Point2D latestPoint,
        final DrawingContext dc) {
        if ((x1 != -1)
            || (y1 != -1)) {
            final Point2D srcp = dc.translatePointToCanvas(new Point2D.Double(
                x1,
                y1));
            final Point2D pt = dc.translatePointToCanvas(latestPoint);
            final Line2D l2d = new Line2D.Double(
                srcp,
                pt);
            final Rectangle2D rect = l2d.getBounds2D();
            g.setColor(dc.getDrawingFillColor());
            g.fillRect(
                (int) rect.getX(),
                (int) rect.getY(),
                (int) rect.getWidth(),
                (int) rect.getHeight());
            g.setColor(Color.WHITE);
            g.drawRect(
                (int) rect.getX(),
                (int) rect.getY(),
                (int) rect.getWidth(),
                (int) rect.getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(
                (int) rect.getX() + 1,
                (int) rect.getY() + 1,
                (int) rect.getWidth() + 1,
                (int) rect.getHeight() + 1);
        } else {
            final Point2D pt = dc.translatePointToCanvas(latestPoint);
            dc.drawModifierPoint(
                g,
                pt.getX(),
                pt.getY());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addPoint(
        final Point2D point) {
        if ((x1 == -1)
            && (y1 == -1)) {
            x1 = point.getX();
            y1 = point.getY();
        } else {
            x2 = point.getX();
            y2 = point.getY();
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean finishDrawingItem() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item getDrawnItem() {
        return new EBoundingBox(
            x1,
            y1,
            x2
                - x1,
            y2
                - y1,
            true);
    }

}
