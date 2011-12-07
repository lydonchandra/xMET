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
import java.awt.geom.Point2D;

import n.ui.patterns.wysiwyg.Item;
import xmet.profiles.geometry.impl2d.Point2d;

/**
 * Represents an abstract WYSIWYG Item Shape which our stuff derives from.
 * @author Nahid Akbar
 */
public abstract class EAbstractShape
    implements
    Item {

    // CHECKSTYLE OFF: MagicNumber
    /** The inclusive color. */
    private static Color inclusiveColor = new Color(
        255,
        0,
        0,
        80);

    /** The exclusive color. */
    private static Color exclusiveColor = new Color(
        255,
        255,
        0,
        80);

    // CHECKSTYLE ON: MagicNumber
    /**
     * Gets the inclusive color.
     * @return the inclusive color
     */
    protected static Color getInclusiveColor() {
        return inclusiveColor;
    }

    /**
     * Gets the exclusive color.
     * @return the exclusive color
     */
    protected static Color getExclusiveColor() {
        return exclusiveColor;
    }

    /** The inc color. */
    private Color incColor = getInclusiveColor();

    /** The exc color. */
    private Color excColor = getExclusiveColor();

    /** The inclusive. */
    private boolean inclusive = true;

    /**
     * Checks if is inclusive.
     * @return true, if is inclusive
     */
    public boolean isInclusive() {
        return inclusive;
    }

    /**
     * Sets the inclusive.
     * @param aInclusive the new inclusive
     */
    protected void setInclusive(
        final boolean aInclusive) {
        this.inclusive = aInclusive;
    }

    /**
     * Gets the inc color.
     * @return the inc color
     */
    protected Color getIncColor() {
        return incColor;
    }

    /**
     * Sets the inc color.
     * @param aIncColor the new inc color
     */
    protected void setIncColor(
        final Color aIncColor) {
        this.incColor = aIncColor;
    }

    /**
     * Gets the exc color.
     * @return the exc color
     */
    protected Color getExcColor() {
        return excColor;
    }

    /**
     * Sets the exc color.
     * @param aExcColor the new exc color
     */
    protected void setExcColor(
        final Color aExcColor) {
        this.excColor = aExcColor;
    }

    /**
     * Gets the main color.
     * @return the main color
     */
    protected Color getMainColor() {
        if (inclusive) {
            return getIncColor();
        } else {
            return getExcColor();
        }
    }

    /**
     * Gets the alt color.
     * @return the alt color
     */
    protected Color getAltColor() {
        if (inclusive) {
            return getExcColor();
        } else {
            return getIncColor();
        }
    }

    /**
     * Instantiates a new e abstract shape.
     * @param aInclusive the inclusive
     */
    public EAbstractShape(
        final boolean aInclusive) {
        super();
        this.inclusive = aInclusive;
    }

    /**
     * Point2d to point2 d.
     * @param point the point
     * @return the point2 d
     */
    protected static Point2D point2dToPoint2D(
        final Point2d point) {
        return new Point2D.Double(
            point.getX(),
            point.getY());
    }

}
