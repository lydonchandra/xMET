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

/**
 * An interface for making a callback invocation once a tool was closed. Was
 * originally sought as a way to do things like GN CSW Updates etc. It is
 * obsolete for that purpose but tools can still support and use this mechanism.
 * @author Nahid Akbar
 */
public interface ToolCallback {

    /**
     * Callback.
     * @param params the params
     */
    void callback(
        Map<String, Object> params);
}
