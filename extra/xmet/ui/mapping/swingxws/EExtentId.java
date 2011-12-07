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
import xmet.profiles.geometry.impl2d.ExtentId2d;

/**
 * A Extent Id Item.
 * @author Nahid Akbar
 */
public class EExtentId
    extends EAbstractShape {

    /** The code. */
    private final ExtentId2d extenrId;

    /**
     * Instantiates a new e code.
     * @param extentId the code
     */
    public EExtentId(
        final ExtentId2d extentId) {
        super(extentId.isInclusive());
        this.extenrId = extentId;
    }

    /**
     * Gets the code.
     * @return the code
     */
    public ExtentId2d getExtentId() {
        return extenrId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
        final Point2D point,
        final DrawingContext dc) {
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
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawItem(
        final Graphics g,
        final DrawingContext dc) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(
        final double dx,
        final double dy) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translateModifierPoint(
        final Point2D p,
        final double dx,
        final double dy) {
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
    }

}
