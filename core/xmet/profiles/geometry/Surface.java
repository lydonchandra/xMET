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
package xmet.profiles.geometry;

import java.util.ArrayList;

/**
 * A polygon surface. defined by a set of exterior points and surface patches
 * @author Nahid Akbar
 */
public interface Surface
    extends
    Shape {

    /**
     * Gets the exterior points.
     * @return the exterior points
     */
    ArrayList<Point> getExteriorPoints();

    /**
     * Gets the interior holes.
     * @return the interior holes
     */
    ArrayList<SurfaceHole> getInteriorHoles();
}
