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

import n.ui.patterns.wysiwyg.DrawingContext;
import xmet.profiles.geometry.impl2d.Point2d;

/**
 * A position item.
 * @author Nahid Akbar
 */
public class EPosition
    extends EAbstractShape {

    /** The x. */
    private double x;

    /** The y. */
    private double y;

    /**
     * Instantiates a new e position.
     * @param aX the x
     * @param aY the y
     */
    public EPosition(
        final double aX,
        final double aY) {
        super(true);
        this.setX(aX);
        this.setY(aY);
    }

    /**
     * Instantiates a new e position.
     * @param position the position
     */
    public EPosition(
        final Point2d position) {
        super(true);
        setX(position.getX());
        setY(position.getY());
    }

    /**
     * Gets the position.
     * @return the position
     */
    Point2d getPosition() {
        return new Point2d(
            getX(),
            getY());
    }

    /**
     * Checks if is near point.
     * @param destX the dest x
     * @param destY the dest y
     * @param srcX the test x
     * @param srcY the test y
     * @param boundaryh the boundaryh
     * @param boundaryv the boundaryv
     * @return true, if is near point
     */
    public static boolean isNearPoint(
        final double destX,
        final double destY,
        final double srcX,
        final double srcY,
        final double boundaryh,
        final double boundaryv) {
        final double dx = Math.abs(srcX
            - destX);
        final double dy = Math.abs(srcY
            - destY);
        if (dx < boundaryh
            && dy < boundaryv) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
        final Point2D point,
        final DrawingContext dc) {
        return isNearPoint(
            getX(),
            getY(),
            point.getX(),
            point.getY(),
            dc.pixelsToLengthH(dc.getModifierPointDiameter()),
            dc.pixelsToLengthV(dc.getModifierPointDiameter()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2D getModifierPointAt(
        final Point2D point,
        final double modifierDiamH,
        final double modifierDiamV) {
        if (isNearPoint(
            getX(),
            getY(),
            point.getX(),
            point.getY(),
            modifierDiamH,
            modifierDiamV)) {
            return new Point2D.Double(
                getX(),
                getY());
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
        g.setColor(Color.RED);
        final Point2D point = dc.translatePointToCanvas(new Point2D.Double(
            getX(),
            getY()));
        dc.fillPoint(
            g,
            point.getX(),
            point.getY(),
            dc.getModifierPointDiameter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(
        final double dx,
        final double dy) {
        setX(getX()
            + dx);
        setY(getY()
            + dy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translateModifierPoint(
        final Point2D p,
        final double dx,
        final double dy) {
        setX(getX()
            + dx);
        setY(getY()
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
        final Point2D np = dc.translatePointToCanvas(new Point2D.Double(
            getX()
                + dx,
            getY()
                + dy));
        dc.drawModifierPoint(
            g,
            np.getX(),
            np.getY());
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
        final DrawingContext editor) {
        drawSelectedOverlay(
            g,
            dx,
            dy,
            editor);
    }

    /**
     * Gets the x.
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x.
     * @param aX the new x
     */
    public void setX(
        final double aX) {
        x = aX;
    }

    /**
     * Gets the y.
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y.
     * @param aY the new y
     */
    public void setY(
        final double aY) {
        y = aY;
    }

}
