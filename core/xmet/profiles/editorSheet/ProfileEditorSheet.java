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
import java.util.ArrayList;

import n.reporting.Reporting;
import xmet.profiles.Profile;
import xmet.resources.ResourceManager;

/**
 * A configuration file for an EditorView.
 * @author Nahid Akbar
 */
public class ProfileEditorSheet
    implements
    Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The profile. */
    private final Profile profile;

    /** The file name. */
    private final String fileName;

    /** The name. */
    private final String name;

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
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
     * Instantiates a new profile editor sheet.
     * @param aProfile the profile
     * @param aFileName the file name
     */
    public ProfileEditorSheet(
        final Profile aProfile,
        final String aFileName) {
        this.profile = aProfile;
        this.fileName = aFileName;
        name = EditorSheetTemplate.extractEditorName(aFileName);
    }

    /**
     * Gets the profile.
     * @return the profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        return getName();
    }

    /** The templates. */
    private ArrayList<EditorSheetTemplate> templates =
        new ArrayList<EditorSheetTemplate>();

    /**
     * Checks if is profile template.
     * @param aFileName the file name
     * @return true, if is profile template
     */
    public boolean isProfileTemplate(
        final String aFileName) {
        Reporting.logUnexpected();
        return false;
    }

    /**
     * Include profile template.
     * @param aFileName the file name
     */
    public void includeProfileTemplate(
        final String aFileName) {
        getTemplates().add(
            new EditorSheetTemplate(
                this,
                aFileName));
    }

    /**
     * Adds the profile template by name.
     * @param aName the name
     * @param contents the contents
     * @param resources the resources
     */
    public void addProfileTemplateByName(
        final String aName,
        final ByteBuffer contents,
        final ResourceManager resources) {
        final EditorSheetTemplate t = getProfileTemplateByName(aName);
        if (t != null) {
            t.setFileContents(
                resources,
                contents);
        } else {
            String templateFileName = getName()
                + "."
                + aName
                + ".xml";
            templateFileName = profile.getFolderPath()
                + "/templates/"
                + templateFileName;
            resources.setResourceContents(
                templateFileName,
                contents);
            getTemplates().add(
                new EditorSheetTemplate(
                    this,
                    templateFileName));
        }
    }

    /**
     * Gets the profile template by name.
     * @param aName the name
     * @return the profile template by name
     */
    public EditorSheetTemplate getProfileTemplateByName(
        final String aName) {
        for (int i = 0; i < getTemplates().size(); i++) {
            if (getTemplates().get(
                i).getName().equals(
                aName)) {
                return getTemplates().get(
                    i);
            }
        }
        return null;
    }

    /**
     * Removes the profile template by name.
     * @param aName the name
     * @param resources the resources
     */
    public void removeProfileTemplateByName(
        final String aName,
        final ResourceManager resources) {
        for (int i = 0; i < getTemplates().size(); i++) {
            if (getTemplates().get(
                i).getName().equals(
                aName)) {
                getTemplates().get(
                    i).deleteTemplate(
                    resources);
            }
        }
    }

    /**
     * Uninclude template.
     * @param template the template
     */
    public void unincludeTemplate(
        final EditorSheetTemplate template) {
        getTemplates().remove(
            template);
    }

    /**
     * Gets the templates.
     * @return the templates
     */
    public ArrayList<EditorSheetTemplate> getTemplates() {
        return templates;
    }

    /**
     * Gets the default template.
     * @return the default template
     */
    public EditorSheetTemplate getDefaultTemplate() {
        for (int i = 0; i < getTemplates().size(); i++) {
            if (getTemplates().get(
                i).isDefault()) {
                return getTemplates().get(
                    i);
            }
        }
        return null;
    }

    /**
     * Rename template.
     * @param template the template
     * @param newName the new name
     * @param resources the resources
     */
    public void renameTemplate(
        final EditorSheetTemplate template,
        final String newName,
        final ResourceManager resources) {
        if (!newName.equals(template.getName())) {
            final ByteBuffer contents = template.getFileContents(resources);
            template.deleteTemplate(resources);
            addProfileTemplateByName(
                newName,
                contents,
                resources);
        }
    }

    /**
     * Gets the file name.
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the templates.
     * @param aTemplates the new templates
     */
    public void setTemplates(
        final ArrayList<EditorSheetTemplate> aTemplates) {
        templates = aTemplates;
    }

}
