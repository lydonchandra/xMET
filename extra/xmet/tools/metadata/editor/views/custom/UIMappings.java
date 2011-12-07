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
package xmet.tools.metadata.editor.views.custom;

import java.util.Hashtable;

import xmet.ui.controls.CheckedList;
import xmet.ui.controls.DateTimePicker;
import xmet.ui.controls.DropDownList;
import xmet.ui.controls.Label;
import xmet.ui.controls.MultiLineText;
import xmet.ui.controls.SingleLineText;

/**
 * UIMappings.
 * @author Shaan
 */
public final class UIMappings {

    /**
     * Instantiates a new uI mappings.
     */
    private UIMappings() {

    }

    /** The hashtable. */
    private static Hashtable<String, Class<?>> hashtable =
        new Hashtable<String, Class<?>>();

    /**
     * Load ui mappings.
     * @param filename the filename
     */
    public static void loadUIMappings(
        final String filename) {
        hashtable.put(
            "Label",
            Label.class);
        hashtable.put(
            "SingleLineText",
            SingleLineText.class);
        hashtable.put(
            "MultiLineText",
            MultiLineText.class);
        hashtable.put(
            "DropDownList",
            DropDownList.class);
        hashtable.put(
            "CheckedList",
            CheckedList.class);
        hashtable.put(
            "DateTimePicker",
            DateTimePicker.class);
    }

    /**
     * Gets the.
     * @param key the key
     * @return the class
     */
    public static Class<?> get(
        final Object key) {
        return hashtable.get(key);
    }

}
