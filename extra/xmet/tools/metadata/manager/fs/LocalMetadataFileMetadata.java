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
package xmet.tools.metadata.manager.fs;

/**
 * Represents the metadata of a local metadata file.
 * @author Nahid Akbar
 */
public class LocalMetadataFileMetadata {

    /** The title string. */
    private String titleString;

    /** The abstract string. */
    private String abstractString;

    /** The uuid string. */
    private String uuidString;

    /** The profile name. */
    private String profileName;

    /** The editor sheet name. */
    private String editorSheetName;

    /**
     * Gets the title.
     * @return the title
     */
    public String getTitle() {
        return getTitleString();
    }

    /**
     * Gets the abstract.
     * @return the abstract
     */
    public String getAbstract() {
        return getAbstractString();
    }

    /**
     * Gets the uuid.
     * @return the uuid
     */
    public String getUUID() {
        return getUuidString();
    }

    /**
     * Gets the profile name.
     * @return the profile name
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Gets the editor sheet name.
     * @return the editor sheet name
     */
    public String getEditorSheetName() {
        return editorSheetName;
    }

    /**
     * Instantiates a new local metadata file metadata.
     * @param aTitleString the title string
     * @param aAbstractString the abstract string
     * @param aUuidString the uuid string
     * @param aProfileName the profile name
     * @param aEditorSheetName the editor sheet name
     */
    public LocalMetadataFileMetadata(
        final String aTitleString,
        final String aAbstractString,
        final String aUuidString,
        final String aProfileName,
        final String aEditorSheetName) {
        super();
        this.setTitleString(aTitleString);
        this.setAbstractString(aAbstractString);
        this.setUuidString(aUuidString);
        this.setProfileName(aProfileName);
        this.setEditorSheetName(aEditorSheetName);
    }

    /**
     * Gets the title string.
     * @return the title string
     */
    public String getTitleString() {
        return titleString;
    }

    /**
     * Sets the title string.
     * @param aTitleString the new title string
     */
    public void setTitleString(
        final String aTitleString) {
        titleString = aTitleString;
    }

    /**
     * Gets the abstract string.
     * @return the abstract string
     */
    public String getAbstractString() {
        return abstractString;
    }

    /**
     * Sets the abstract string.
     * @param aAbstractString the new abstract string
     */
    public void setAbstractString(
        final String aAbstractString) {
        abstractString = aAbstractString;
    }

    /**
     * Gets the uuid string.
     * @return the uuid string
     */
    public String getUuidString() {
        return uuidString;
    }

    /**
     * Sets the uuid string.
     * @param aUuidString the new uuid string
     */
    public void setUuidString(
        final String aUuidString) {
        uuidString = aUuidString;
    }

    /**
     * Sets the profile name.
     * @param aProfileName the new profile name
     */
    public void setProfileName(
        final String aProfileName) {
        profileName = aProfileName;
    }

    /**
     * Sets the editor sheet name.
     * @param aEditorSheetName the new editor sheet name
     */
    public void setEditorSheetName(
        final String aEditorSheetName) {
        editorSheetName = aEditorSheetName;
    }
}
