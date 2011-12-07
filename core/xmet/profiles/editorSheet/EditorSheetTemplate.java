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
package xmet.profiles.editorSheet;

import java.io.Serializable;
import java.nio.ByteBuffer;

import n.io.bin.Files;
import xmet.resources.ResourceManager;

/**
 * An EditorSheet Template file.
 * @author Nahid Akbar
 */
public class EditorSheetTemplate
    implements
    Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The editor sheet. */
    private final ProfileEditorSheet editorSheet;

    /** The name of the template. */
    private String name;

    /** The file name. */
    private String fileName;

    /**
     * Instantiates a new editor sheet template.
     * @param profileEditorSheet the profile editor sheet
     * @param aFileName the file name
     */
    public EditorSheetTemplate(
        final ProfileEditorSheet profileEditorSheet,
        final String aFileName) {
        editorSheet = profileEditorSheet;
        this.fileName = aFileName;
        name = EditorSheetTemplate.extractTemplateName(aFileName);
    }

    /* == Getters and setters == */
    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param aName the new name
     */
    public void setName(
        final String aName) {
        this.name = aName;
    }

    /**
     * Gets the file name.
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     * @param aFileName the new file name
     */
    public void setFileName(
        final String aFileName) {
        this.fileName = aFileName;
    }

    /**
     * Checks if is default.
     * @return true, if is default
     */
    public boolean isDefault() {
        return name.equals("default");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Gets the file contents.
     * @param resources the resources
     * @return the file contents
     */
    public ByteBuffer getFileContents(
        final ResourceManager resources) {
        return resources.getResourceContents(fileName);
    }

    /**
     * Sets the file contents.
     * @param resources the resources
     * @param contents the contents
     */
    public void setFileContents(
        final ResourceManager resources,
        final ByteBuffer contents) {
        resources.setResourceContents(
            fileName,
            contents);
    }

    /**
     * Delete template.
     * @param resources the resources
     */
    public void deleteTemplate(
        final ResourceManager resources) {
        Files.delete(resources.getResourceURI(
            fileName).getPath());
        editorSheet.unincludeTemplate(this);
    }

    /* == Misc helper methods == */
    /**
     * Extract template editor name.
     * @param fileName the file name
     * @return the string
     */
    public static String extractTemplateEditorName(
        final String fileName) {
        int i = fileName.lastIndexOf('/') + 1;
        String name = fileName.substring(i);
        i = fileName.lastIndexOf('\\') + 1;
        name = name.substring(i);
        name = name.substring(
            0,
            name.lastIndexOf("."));
        name = name.substring(
            0,
            name.lastIndexOf("."));
        return name;
    }

    /**
     * Extract template name.
     * @param fileName the file name
     * @return the string
     */
    public static String extractTemplateName(
        final String fileName) {
        int i = fileName.lastIndexOf('/') + 1;
        String name = fileName.substring(i);
        i = fileName.lastIndexOf('\\') + 1;
        name = name.substring(i);
        name = name.substring(
            0,
            name.lastIndexOf("."));
        name = name.substring(name.indexOf(".") + 1);
        return name;
    }

    /**
     * Extract editor name.
     * @param fileName the file name
     * @return the string
     */
    public static String extractEditorName(
        final String fileName) {
        int i = fileName.lastIndexOf('/') + 1;
        String name = fileName.substring(i);
        i = fileName.lastIndexOf('\\') + 1;
        name = name.substring(i);
        name = name.substring(
            0,
            name.lastIndexOf("."));
        return name;
    }

}
