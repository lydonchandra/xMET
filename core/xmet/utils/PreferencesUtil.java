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
package xmet.utils;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import n.io.xml.CSXMLSerializationCodec;

/**
 * Simple wrapper tool for working with preferences.
 * @author Nahid Akbar
 */
public class PreferencesUtil {

    /** The Constant NODE. */
    private static final String NODE = "/xmet";

    /** The prefs. */
    private final Preferences prefs;

    /**
     * Instantiates a new preferences util.
     * @param toolName the tool name
     */
    public PreferencesUtil(
        final String toolName) {
        super();
        String path = NODE;
        String toolNameTemp = toolName;
        if (toolNameTemp != null) {
            toolNameTemp = toolNameTemp.trim();
            if (toolNameTemp.length() != 0) {
                toolNameTemp = toolNameTemp.replace(
                    "/",
                    "_");
                toolNameTemp = toolNameTemp.replace(
                    "\\",
                    "_");
                path = path
                    + "/"
                    + toolNameTemp;
            }
        }
        prefs = Preferences.userRoot().node(
            path);
    }

    /**
     * Gets the.
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return the string
     */
    public final String get(
        final String arg0,
        final String arg1) {
        return prefs.get(
            arg0,
            arg1);
    }

    /**
     * Gets the boolean.
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return the boolean
     */
    public final boolean getBoolean(
        final String arg0,
        final boolean arg1) {
        return prefs.getBoolean(
            arg0,
            arg1);
    }

    /**
     * Gets the byte array.
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return the byte array
     */
    public final byte[] getByteArray(
        final String arg0,
        final byte[] arg1) {
        return prefs.getByteArray(
            arg0,
            arg1);
    }

    /**
     * Gets the double.
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return the double
     */
    public final double getDouble(
        final String arg0,
        final double arg1) {
        return prefs.getDouble(
            arg0,
            arg1);
    }

    /**
     * Gets the float.
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return the float
     */
    public final float getFloat(
        final String arg0,
        final float arg1) {
        return prefs.getFloat(
            arg0,
            arg1);
    }

    /**
     * Gets the int.
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return the int
     */
    public final int getInt(
        final String arg0,
        final int arg1) {
        return prefs.getInt(
            arg0,
            arg1);
    }

    /**
     * Gets the long.
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return the long
     */
    public final long getLong(
        final String arg0,
        final long arg1) {
        return prefs.getLong(
            arg0,
            arg1);
    }

    /**
     * Gets the config object.
     * @param string the string
     * @return the config object
     */
    public final Object getObject(
        final String string) {
        final String setting = get(
            string,
            null);
        if (setting != null) {
            return CSXMLSerializationCodec.decode(setting);
        }
        return null;
    }

    /**
     * Node.
     * @param arg0 the arg0
     * @return the preferences
     */
    public final Preferences node(
        final String arg0) {
        return prefs.node(arg0);
    }

    /**
     * Node exists.
     * @param arg0 the arg0
     * @return true, if successful
     * @throws BackingStoreException the backing store exception
     */
    public final boolean nodeExists(
        final String arg0)
        throws BackingStoreException {
        return prefs.nodeExists(arg0);
    }

    /**
     * Put.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public final void put(
        final String arg0,
        final String arg1) {
        prefs.put(
            arg0,
            arg1);
    }

    /**
     * Put boolean.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public final void putBoolean(
        final String arg0,
        final boolean arg1) {
        prefs.putBoolean(
            arg0,
            arg1);
    }

    /**
     * Put byte array.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public final void putByteArray(
        final String arg0,
        final byte[] arg1) {
        prefs.putByteArray(
            arg0,
            arg1);
    }

    /**
     * Put double.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public final void putDouble(
        final String arg0,
        final double arg1) {
        prefs.putDouble(
            arg0,
            arg1);
    }

    /**
     * Put float.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public final void putFloat(
        final String arg0,
        final float arg1) {
        prefs.putFloat(
            arg0,
            arg1);
    }

    /**
     * Put int.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public final void putInt(
        final String arg0,
        final int arg1) {
        prefs.putInt(
            arg0,
            arg1);
    }

    /**
     * Put long.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public final void putLong(
        final String arg0,
        final long arg1) {
        prefs.putLong(
            arg0,
            arg1);
    }

    /**
     * Put object.
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public final void putObject(
        final String arg0,
        final Object arg1) {
        final String setting = CSXMLSerializationCodec.encode(arg1);
        put(
            arg0,
            setting);
    }

    /**
     * Removes the.
     * @param arg0 the arg0
     */
    public final void remove(
        final String arg0) {
        prefs.remove(arg0);
    }

    /**
     * Removes the node.
     * @throws BackingStoreException the backing store exception
     */
    public final void removeNode()
        throws BackingStoreException {
        prefs.removeNode();
    }

}
