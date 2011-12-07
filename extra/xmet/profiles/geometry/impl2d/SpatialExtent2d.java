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

import xmet.profiles.geometry.Shape;
import xmet.profiles.geometry.SpatialExtent;

/**
 * Implementation of spatial extent.
 * @author Nahid Akbar
 */
public class SpatialExtent2d
    implements
    SpatialExtent {

    /** The shapes inside this extent. */
    private final ArrayList<Shape> shapes = new ArrayList<Shape>();

    /**
     * Instantiates a new spatial extent2d.
     * @param shapes2 the shapes2
     */
    public SpatialExtent2d(
        final ArrayList<Shape> shapes2) {
        shapes.addAll(shapes2);
    }

    /**
     * Instantiates a new spatial extent2d.
     */
    public SpatialExtent2d() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Shape> getShapes() {
        return shapes;
    }

}
