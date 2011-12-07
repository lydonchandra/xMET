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

import xmet.profiles.geometry.ExtentId;

/**
 * Implementation of code.
 * @author Nahid Akbar
 */
public class ExtentId2d
    implements
    ExtentId {

    /** The inclusive. */
    private boolean inclusive;

    /** The code space. */
    private String codeSpace;

    /** The code. */
    private String code;

    /**
     * Instantiates a new code2d.
     * @param aCode the code
     * @param aCodeSpace the code space
     * @param aInclusive the inclusive
     */
    public ExtentId2d(
        final String aCode,
        final String aCodeSpace,
        final boolean aInclusive) {
        super();
        this.code = aCode;
        this.codeSpace = aCodeSpace;
        this.inclusive = aInclusive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInclusive() {
        return inclusive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSpace() {
        return codeSpace;
    }

    /**
     * Sets the code space.
     * @param string the new code space
     */
    public void setCodeSpace(
        final String string) {
        codeSpace = string;

    }

    /**
     * Sets the code.
     * @param string the new code
     */
    public void setCode(
        final String string) {
        code = string;

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
        return "Code";
    }
}
