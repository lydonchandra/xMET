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
package xmet.ui.controls;

import java.math.BigInteger;
import java.security.SecureRandom;

import n.math.MathUtils;
import xmet.profiles.codecs.impl.StringSpatialExtentCodec;
import xmet.profiles.geometry.impl2d.ExtentId2d;
import xmet.profiles.geometry.impl2d.SpatialExtent2d;

/**
 * Test case for control.
 * @author Nahid Akbar
 */
public class SpatialExtentTestCase
    extends AbstractGUIObjectTestCase {

    /**
     * Instantiates a new spatial extent test case.
     */
    public SpatialExtentTestCase() {
        super(SpatialExtent.class);
    }

    /** The random. */
    private static SecureRandom random = new SecureRandom();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExampleValue() {
        // CHECKSTYLE OFF: MagicNumber
        final SpatialExtent2d se = new SpatialExtent2d();
        boolean varInclusive;
        if (MathUtils.getRandomInt(
            0,
            1) == 0) {
            varInclusive = true;
        } else {
            varInclusive = false;
        }
        se.getShapes().add(
            new ExtentId2d(
                (new BigInteger(
                    MathUtils.getRandomInt(
                        0,
                        1024),
                    random).toString(32)),
                (new BigInteger(
                    MathUtils.getRandomInt(
                        0,
                        1024),
                    random).toString(32)),
                varInclusive));
        return StringSpatialExtentCodec.encodeSpatialExtent(se);
        // CHECKSTYLE OFF: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getChangeValueMethods() {
        return new String[] {
        "Enter an inclusive Code|<extent><Code "
            + "codeSpace=\"\" inclusive=\"true\"></Code></extent>",
        "Enter anything except nothing",
        };
    }
}
