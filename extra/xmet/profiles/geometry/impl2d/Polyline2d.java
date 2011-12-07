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
import xmet.profiles.geometry.Polyline;

/**
 * Represents a set of one or more lines.
 * @author Nahid Akbar
 */
public class Polyline2d
    implements
    Polyline {

    /** The points. */
    private final ArrayList<Point> points;

    /**
     * Instantiates a new polyline2d.
     * @param aPoints the points
     */
    public Polyline2d(
        final ArrayList<Point> aPoints) {
        this.points = aPoints;
    }

    /**
     * Instantiates a new polyline2d.
     */
    public Polyline2d() {
        this(new ArrayList<Point>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInclusive() {
        /*
         * Same sort of argument as a point.
         */
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "PolyLine";
    }

}
