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

import xmet.profiles.geometry.BoundingBox;
import xmet.profiles.geometry.Point;

/**
 * Implementation of bounding box. Modelled by the top right and bottom left
 * corner.
 * @author Nahid Akbar
 */
public class BoundingBox2d
    implements
    BoundingBox {

    /** The north west. */
    private final Point2d northWest;

    /** The south east. */
    private final Point2d southEast;

    /** The inclusive. */
    private boolean inclusive;

    /**
     * Instantiates a new bounding box2 d.
     * @param nw the nw
     * @param se the se
     * @param aInclusive the inclusive
     */
    public BoundingBox2d(
        final Point2d nw,
        final Point2d se,
        final boolean aInclusive) {
        northWest = nw;
        southEast = se;
        northWest.setName("North West");
        southEast.setName("South East");
        this.inclusive = aInclusive;
    }

    /**
     * Instantiates a new bounding box2 d.
     */
    public BoundingBox2d() {
        this(new Point2d(
            0,
            0), new Point2d(
            0,
            0), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getNorthWest() {
        return northWest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getSouthEast() {
        return southEast;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInclusive() {
        return inclusive;
    }

    /**
     * Sets the inclusive.
     * @param value the new inclusive
     */
    public void setInclusive(
        final boolean value) {
        inclusive = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Bounding Box";
    }

}
