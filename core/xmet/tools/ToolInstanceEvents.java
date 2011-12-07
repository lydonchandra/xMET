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

/**
 * Simply contains a list of event constants. These constants are created
 * somewhere in the GUI and passed onto the tool Instance manager who determines
 * whether to pass it down to the individual tools or process it by itself. The
 * tool constants are separated into two groups. The generic ones which refer to
 * the xMET core and tool specific ones which is passed onto the tool instances
 * and have a different pre-defined range of constants.
 * @author Nahid Akbar
 */
public final class ToolInstanceEvents {

    /**
     * Instantiates a new tool instance events.
     */
    private ToolInstanceEvents() {

    }

    /* == generic events (values 128-1024) == */
    // CHECKSTYLE OFF: MagicNumber
    /**
     * start of generic event constants - these are processed by the tool
     * instance manager.
     */
    private static final int GENERIC_BASE = 128;

    /** Event when the user chooses to exit the application. */
    public static final int EXIT_APPLICATION = GENERIC_BASE + 1;

    /** Event when the user wants to see the next tool instance. */
    public static final int NEXT_TOOL_INSTANCE = GENERIC_BASE + 12;

    /** Event when the user wants to see the previous tool instance. */
    public static final int PREVIOUS_TOOL_INSTANCE = GENERIC_BASE + 13;

    /** Event when the user wants to close the active tool instance. */
    public static final int CLOSE_TOOL_INSTANCE = GENERIC_BASE + 14;
    /* == tool specific events (values 1024 - 2048) == */
    /** start of tool instance specific event constants. */
    private static final int TOOL_INSTANCE_SPECIFIC_BASE = 1024;

    /** Event when the user wants to see the next page. */
    public static final int NEXT_PAGE = TOOL_INSTANCE_SPECIFIC_BASE + 1;

    /** Event when the user wants to see the previous page. */
    public static final int PREVIOUS_PAGE = TOOL_INSTANCE_SPECIFIC_BASE + 2;

    /** Event when the user wants to save a document. */
    public static final int SAVE_DOCUMENT = TOOL_INSTANCE_SPECIFIC_BASE + 3;

    /** Event when the user wants to save a document as another document. */
    public static final int SAVE_DOCUMENT_AS = TOOL_INSTANCE_SPECIFIC_BASE + 4;

    /** Event when the user wants to make a new document. */
    public static final int NEW_DOCUMENT = TOOL_INSTANCE_SPECIFIC_BASE + 5;

    /** Event when the user wants to open a document. */
    public static final int OPEN_DOCUMENT = TOOL_INSTANCE_SPECIFIC_BASE + 6;

    /** Event when the user wants to close the current document. */
    public static final int CLOSE_DOCUMENT = TOOL_INSTANCE_SPECIFIC_BASE + 7;

    /**
     * Event when the user wants to see the help associated with the tool
     * instance.
     */
    public static final int HELP = TOOL_INSTANCE_SPECIFIC_BASE + 8;

    // CHECKSTYLE ON: MagicNumber

    /**
     * Checks if is generic event.
     * @param event the event
     * @return true, if is generic event
     */
    public static boolean isGenericEvent(
        final int event) {
        return (event > GENERIC_BASE)
            && (event < TOOL_INSTANCE_SPECIFIC_BASE);
    }

}
