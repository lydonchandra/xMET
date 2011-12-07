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

import xmet.ClientContext;

/**
 * A Tool that is used in xMET.
 * @author Kyle
 */
public interface Tool {

    /* == Basics == */
    /**
     * Checks if is single instance.
     * @return whether the tool is single instance
     */
    boolean isSingleInstance();

    /**
     * Gets the name.
     * @return the name of the tool
     */
    String getName();

    /**
     * invokes the tool Returns a pointer to an instance.
     * @param params TODO
     * @return tool instance
     * @throws TransparentToolException the transparent tool exception
     */
    ToolInstance invoke(
        Map<String, Object> params)
        throws TransparentToolException;

    /* == Events == */

    /**
     * When the tool is loaded.
     */
    void onInitialize();

    /**
     * when the tool is unloaded, typically at shutdown.
     */
    void onDisposal();

    /**
     * Sets the client.
     * @param client the client to set
     */
    void setContext(
        ClientContext client);

    /**
     * Gets the client.
     * @return the client
     */
    ClientContext getContext();

}
