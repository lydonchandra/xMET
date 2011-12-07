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
import java.awt.geom.Point2D;

import n.ui.patterns.wysiwyg.DrawingContext;
import n.ui.patterns.wysiwyg.Item;
import n.ui.patterns.wysiwyg.ItemDrawingTool;

/**
 * A drawing item for a position.
 * @author Nahid Akbar
 */
public class EPositionDT
    extends ItemDrawingTool {

    /** The pi. */
    private EPosition pi = new EPosition(
        -1,
        -1);

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawInProgress(
        final Graphics g,
        final Point2D latestPoint,
        final DrawingContext dc) {
        final Point2D pt = dc.translatePointToCanvas(latestPoint);
        dc.drawModifierPoint(
            g,
            pt.getX(),
            pt.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addPoint(
        final Point2D point) {
        pi.setX(point.getX());
        pi.setY(point.getY());
        return true;
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
        return pi;
    }

}
