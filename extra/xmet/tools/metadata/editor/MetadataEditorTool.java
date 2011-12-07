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
package xmet.tools.metadata.editor;

import java.io.File;
import java.util.Map;

import n.reporting.Reporting;
import xmet.profiles.Profile;
import xmet.profiles.editorSheet.EditorSheetTemplate;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.tools.DefaultTool;
import xmet.tools.ToolCallback;
import xmet.tools.ToolInstance;
import xmet.tools.TransparentToolException;

/**
 * The metadata editor tool entry point.
 * @author Nahid Akbar
 */
public class MetadataEditorTool
    extends DefaultTool {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "metadata.editor";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolInstance invoke(
        final Map<String, Object> params)
        throws TransparentToolException {
        Profile profile = (Profile) params.get("profile");
        if (profile == null) {
            final String profileName = (String) params.get("profileName");
            if (profileName != null) {
                profile = getContext().getProfiles().getProfileByName(
                    profileName);
            }
        }

        ProfileEditorSheet editorSheet =
            (ProfileEditorSheet) params.get("editorSheet");

        final EditorSheetTemplate template =
            (EditorSheetTemplate) params.get("template");
        if (editorSheet == null) {
            final String editorSheetName =
                (String) params.get("editorSheetName");
            final File editorSheetFile = (File) params.get("editorSheetFile");
            if ((profile != null)
                && (editorSheetName != null)) {
                editorSheet = profile.getEditorSheetByName(editorSheetName);
            }
            if ((profile != null)
                && (editorSheetFile != null)) {
                editorSheet = profile.getEditorSheetByFile(editorSheetFile);
            }
        } else {
            if (profile == null) {
                profile = editorSheet.getProfile();
            }
        }

        /* if (template == null && editorSheet != null) { */
        /* template = editorSheet.getDefaultTemplate(); */
        // }

        EditableFile file = (EditableFile) params.get("file");

        if (file == null) {
            final String fileName = (String) params.get("fileName");
            if (fileName != null) {
                file = new LocalEditableFile(
                    new File(
                        fileName));
            }
        }

        final ToolCallback callback = (ToolCallback) params.get("callback");

        FilesSelectionUtil selectionUtil =
            (FilesSelectionUtil) params.get("filesSelection");

        if (selectionUtil == null) {
            selectionUtil =
                getContext()
                    .getServices()
                    .<FilesSelectionUtil>getServiceProvider(
                        FilesSelectionUtil.class);
        }
        try {
            if (profile != null) {
                return new MetadataEditorToolInstance(
                    this,
                    profile,
                    editorSheet,
                    file,
                    callback,
                    selectionUtil,
                    template);
            } else {
                Reporting.reportUnexpected("Profile Not specified");
                return null;
            }
        } catch (final TransparentToolException e) {
            throw e;
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleInstance() {
        return false;
    }

}
