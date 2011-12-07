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
package xmet.tools.application;

import java.awt.EventQueue;
import java.util.Map;

import n.ui.JOptionPaneUtils;
import xmet.tools.DefaultTool;
import xmet.tools.ToolInstance;
import xmet.tools.TransparentToolException;

/**
 * Tool for Exiting the program.
 * @author Nahid Akbar
 */
public class ExitTool
    extends DefaultTool {

    /* == Tool Interface == */

    /** The Constant EXIT_PROMPT_MSG. */
    private static final String EXIT_PROMPT_MSG = "Exit the Program?";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "application.exit";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params)
        throws TransparentToolException {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (JOptionPaneUtils.getYesNoConfirmation(EXIT_PROMPT_MSG)) {
                    getContext().shutdown();
                }
                // else {
                // }
            }
        });
        throw new TransparentToolException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleInstance() {
        return true;
    }

}
