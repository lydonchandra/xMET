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
package xmet.tools.metadata.editor.views.scv.impl;

import n.io.CS;
import n.io.CSC;

/**
 * Alerts the user with a message.
 * @author Nahid Akbar
 */
@CSC("alertUser")
public class AlertUserCode
    extends Code {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** the title. */
    @CS
    private String title = "Error";

    /** the message. */
    @CS
    private String message;

    /**
     * Instantiates a new alert user code.
     */
    public AlertUserCode() {
        super();
    }

    /**
     * Instantiates a new alert user code.
     * @param aTitle the title
     * @param aMessage the message
     */
    public AlertUserCode(
        final String aTitle,
        final String aMessage) {
        super();
        title = aTitle;
        message = aMessage;
    }

    /**
     * Gets the title.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     * @param aTitle the new title
     */
    public void setTitle(
        final String aTitle) {
        this.title = aTitle;
    }

    /**
     * Gets the message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     * @param aMessage the new message
     */
    public void setMessage(
        final String aMessage) {
        this.message = aMessage;
    }

}
