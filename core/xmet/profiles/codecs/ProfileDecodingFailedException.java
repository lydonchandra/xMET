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
package xmet.profiles.codecs;

/**
 * Exception raised when a profile codec fails parsing a schema.
 * @author Nahid Akbar
 */
public class ProfileDecodingFailedException
    extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new profile decoding failed exception.
     */
    public ProfileDecodingFailedException() {
        super();
    }

    /**
     * Instantiates a new profile decoding failed exception.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public ProfileDecodingFailedException(
        final String arg0,
        final Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new profile decoding failed exception.
     * @param arg0 the arg0
     */
    public ProfileDecodingFailedException(
        final String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new profile decoding failed exception.
     * @param arg0 the arg0
     */
    public ProfileDecodingFailedException(
        final Throwable arg0) {
        super(arg0);
    }

}
