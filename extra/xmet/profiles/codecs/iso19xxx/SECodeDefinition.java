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
package xmet.profiles.codecs.iso19xxx;

import org.jdom.Element;

import xmet.profiles.catalogs.model.SECodeItem;
import xmet.profiles.geometry.SpatialExtent;
import xmet.profiles.geometry.impl2d.BoundingBox2d;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.SpatialExtent2d;

/**
 * SE version of Code Definitions.
 * @author Nahid Akbar
 */
public class SECodeDefinition
    extends CodeDefinition
    implements
    SECodeItem {

    /** The se. */
    private SpatialExtent se = null;

    /**
     * Instantiates a new code definition.
     * @param codeDefinition the code definition
     */
    public SECodeDefinition(
        final Element codeDefinition) {
        // CHECKSTYLE OFF: MagicNumber
        super(codeDefinition);

        final String desc = getDescription();

        final String[] descriptions = desc.split("\\|");

        if (descriptions.length == 6) {
            setDescription(descriptions[0]);
            se = new SpatialExtent2d();
            final Point2d nw = new Point2d(
                Double.parseDouble(descriptions[4]),
                Double.parseDouble(descriptions[1]));
            final Point2d aSe = new Point2d(
                Double.parseDouble(descriptions[3]),
                Double.parseDouble(descriptions[2]));
            this.se.getShapes().add(
                new BoundingBox2d(
                    nw,
                    aSe,
                    true));
        }
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpatialExtent getExtent() {
        return se;
    }

}
