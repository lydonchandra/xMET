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
package xmet.profiles.model;

import java.io.Serializable;

/**
 * Contains the minOccurs and maxOccurs elements of the Particle XSD Component.
 * @author Nahid Akbar
 */
public class MultiplicityConstraints
    implements
    Serializable {

    // CHECKSTYLE OFF: MagicNumber

    /** The Constant OPTIONAL. */
    public static final int OPTIONAL = 2;

    /** The Constant REPEATED. */
    public static final int REPEATED = 3;

    /** The Constant NORMAL. */
    public static final int NORMAL = 1;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The min occurs. */
    private int minOccurs = 1;

    /** The max occurs. */
    private int maxOccurs = 1;

    // CHECKSTYLE ON: MagicNumber

    /**
     * Gets the min occurs.
     * @return the min occurs
     */
    public int getMinOccurs() {
        return minOccurs;
    }

    /**
     * Sets the min occurs.
     * @param aMinOccurs the new min occurs
     */
    public void setMinOccurs(
        final int aMinOccurs) {
        this.minOccurs = aMinOccurs;
    }

    /**
     * Gets the max occurs.
     * @return the max occurs
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * Sets the max occurs.
     * @param aMaxOccurs the new max occurs
     */
    public void setMaxOccurs(
        final int aMaxOccurs) {
        this.maxOccurs = aMaxOccurs;
    }

    /**
     * Instantiates a new multiplicity constraints.
     * @param aMinOccurs the min occurs
     * @param aMaxOccurs the max occurs
     */
    public MultiplicityConstraints(
        final int aMinOccurs,
        final int aMaxOccurs) {
        super();
        this.setMinOccurs(aMinOccurs);
        this.setMaxOccurs(aMaxOccurs);
    }

    /**
     * Gets the new.
     * @param minOccurs2 the min occurs2
     * @param maxOccurs2 the max occurs2
     * @return the new
     */
    public static MultiplicityConstraints getNew(
        final int minOccurs2,
        final int maxOccurs2) {
        /* TODO: Implement caching to same memory */
        return new MultiplicityConstraints(
            minOccurs2,
            maxOccurs2);
    }

    /**
     * Gets the container type.
     * @return the container type
     */
    public int getContainerType() {
        // CHECKSTYLE OFF: MagicNumber

        if (getMaxOccurs() == 1) {
            if (getMinOccurs() == 0) {
                return OPTIONAL;
            } else if (getMinOccurs() == 1) {
                return NORMAL;
            }
        } else if (getMaxOccurs() > 0
            || getMaxOccurs() == -1) {
            return REPEATED;
        }
        return NORMAL;
        // CHECKSTYLE ON: MagicNumber
    }

}
