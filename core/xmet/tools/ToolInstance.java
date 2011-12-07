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
package xmet.tools;

import java.util.Map;

import javax.swing.JComponent;

/**
 * Instance of a tool.
 * @author Nahid Akbar
 */
public interface ToolInstance {

    /* == Basics == */
    /**
     * Gets the tool.
     * @return the associated tool
     */
    Tool getTool();

    /**
     * Gets the title.
     * @return the title of the application
     */
    String getTitle();

    /**
     * Gets the display panel.
     * @return the panel to display
     */
    JComponent getDisplayPanel();

    /* == Events == */
    /**
     * Before the tool instance is activated for the first time.
     */
    void onInstantiation();

    /**
     * Before the tool instance is being closed.
     */
    void onDisposal();

    /**
     * When user presses the close button or when the program is shutting down.
     * @param force true when it must shut down.
     * @return Must return true otherwise the tool will not be closed
     */
    boolean onClose(
        boolean force);

    /**
     * When single instance tool is re-focused while open.
     * @param params specified if its a single instance tool and an instance
     *            exists and re-invoke call contains paramaters
     */
    void onRefocus(
        Map<String, Object> params);

    /**
     * On event.
     * @param applicationEvent the application event
     */
    void onEvent(
        int applicationEvent);

}
