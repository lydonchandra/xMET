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
package xmet.profiles;

/**
 * Exception raised when it fails loading profile for some reason.
 * @author Nahid Akbar
 */
public class ProfileLoadingFailedException
    extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new profile loading failed exception.
     */
    public ProfileLoadingFailedException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Instantiates a new profile loading failed exception.
     * @param message the message
     * @param cause the cause
     */
    public ProfileLoadingFailedException(
        final String message,
        final Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Instantiates a new profile loading failed exception.
     * @param message the message
     */
    public ProfileLoadingFailedException(
        final String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Instantiates a new profile loading failed exception.
     * @param cause the cause
     */
    public ProfileLoadingFailedException(
        final Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
