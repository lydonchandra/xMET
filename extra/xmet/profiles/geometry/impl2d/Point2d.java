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
package xmet.profiles.geometry.impl2d;

import xmet.profiles.geometry.Point;

/**
 * This is an implementation of a point reference. Implemented with a x and a y
 * coodrinate
 * @author Nahid Akbar
 */
public class Point2d
    implements
    Point {

    /** The x coordinate. */
    private double x;

    /** The y coordinate. */
    private double y;

    /**
     * Instantiates a new point2d.
     * @param aX the x
     * @param aY the y
     */
    public Point2d(
        final double aX,
        final double aY) {
        this.x = aX;
        this.y = aY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getX() {
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getY() {
        return y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getZ() {
        return null;
    }

    /**
     * Sets the x.
     * @param aX the new x
     */
    public void setX(
        final double aX) {
        this.x = aX;
    }

    /**
     * Sets the y.
     * @param aY the new y
     */
    public void setY(
        final double aY) {
        this.y = aY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInclusive() {
        /*
         * Note: The discussion I had with John Hockaday was that although its
         * allowed, we wont have point exclusions for all purposes. I also
         * concur as the points are defined in terms of real numbers and
         * limitation of precision aside, its theoretically impossible to have
         * exclusions of a point.
         */
        return true;
    }

    /** The name. */
    private String name = "Point";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param aName the new name
     */
    public void setName(
        final String aName) {
        this.name = aName;
    }

    /**
     * Sets the location.
     * @param aX the x
     * @param aY the y
     */
    public void setLocation(
        final double aX,
        final double aY) {
        this.x = aX;
        this.y = aY;
    }
}
