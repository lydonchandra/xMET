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
 * Exeption raised by DataCodecs when they fail loading a document.
 * @author Nahid Akbar
 */
public class ContentsCouldNotBeDecodedException
    extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new contents could not be decoded exception.
     */
    public ContentsCouldNotBeDecodedException() {
        super();
    }

    /**
     * Instantiates a new contents could not be decoded exception.
     * @param message the message
     * @param cause the cause
     */
    public ContentsCouldNotBeDecodedException(
        final String message,
        final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new contents could not be decoded exception.
     * @param message the message
     */
    public ContentsCouldNotBeDecodedException(
        final String message) {
        super(message);
    }

    /**
     * Instantiates a new contents could not be decoded exception.
     * @param cause the cause
     */
    public ContentsCouldNotBeDecodedException(
        final Throwable cause) {
        super(cause);
    }

}
