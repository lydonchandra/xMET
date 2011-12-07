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

import java.util.ArrayList;

import xmet.profiles.geometry.Point;
import xmet.profiles.geometry.Surface;
import xmet.profiles.geometry.SurfaceHole;

/**
 * Implementation of a surface which can have interior holes.
 * @author Nahid Akbar
 */
public class Surface2d
    implements
    Surface {

    /** The exterior points. */
    private ArrayList<Point> exteriorPoints;

    /** The interior holes. */
    private ArrayList<SurfaceHole> interiorHoles;

    /** The inclusive. */
    private boolean inclusive;

    /**
     * Instantiates a new surface2d.
     * @param coordinates the coordinates
     * @param aInteriorHoles the interior holes
     * @param aInclusive the inclusive
     */
    public Surface2d(
        final ArrayList<Point> coordinates,
        final ArrayList<SurfaceHole> aInteriorHoles,
        final boolean aInclusive) {
        this.inclusive = aInclusive;
        exteriorPoints = coordinates;
        this.interiorHoles = aInteriorHoles;
        if (this.interiorHoles == null) {
            this.interiorHoles = new ArrayList<SurfaceHole>();
        }
        if (exteriorPoints == null) {
            exteriorPoints = new ArrayList<Point>();
        }
    }

    /**
     * Instantiates a new surface2d.
     */
    public Surface2d() {
        this(new ArrayList<Point>(), new ArrayList<SurfaceHole>(), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Point> getExteriorPoints() {
        return exteriorPoints;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<SurfaceHole> getInteriorHoles() {
        return interiorHoles;
    }

    /**
     * Checks for interior holes.
     * @return true, if successful
     */
    public boolean hasInteriorHoles() {
        return (interiorHoles != null)
            && (interiorHoles.size() > 0);
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
     * @param aInclusive the new inclusive
     */
    public void setInclusive(
        final boolean aInclusive) {
        this.inclusive = aInclusive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Surface";
    }
}
