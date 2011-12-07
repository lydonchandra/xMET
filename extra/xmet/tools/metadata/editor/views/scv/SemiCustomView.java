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
package xmet.tools.metadata.editor.views.scv;

import java.nio.ByteBuffer;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.editorSheet.ProfileEditorSheet;
import xmet.profiles.model.Entity;
import xmet.tools.metadata.editor.EditorView;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;
import xmet.utils.BusyScreenUtil;

/**
 * Implementation of Semi-Custom view.
 * @author Nahid Akbar
 */
public class SemiCustomView
    extends EditorView
    implements
    Observer {

    /** Default filename extension as handelled by the system. */
    public static final String FILENAME_EXTENSION = "editorgui";

    /** Display panel - built and maintained. */
    private JComponent panel;

    /** The sheet. */
    private Sheet sheet;

    /**
     * Instantiates a new semi custom view.
     * @param model the model
     * @param profile the profile
     * @param client the client
     * @param editorSheet the editor stylesheet
     */
    public SemiCustomView(
        final Entity model,
        final Profile profile,
        final ClientContext client,
        final ProfileEditorSheet editorSheet) {
        super(model, profile, client);

        BusyScreenUtil.startBusy("Creating Interface");
        try {
            /* load sheet */
            sheet = SCVUtils.loadSheetFromContents(new String(
                editorSheet.getFileContents(
                    getClient().getResources()).array()));

            if (sheet != null) {
                /* initialize sheet */
                SCVUtils.initializeSheet(
                    sheet,
                    getRoot(),
                    profile,
                    client);

                /* add observer for updating to any changes */
                sheet.addObserver(this);

                /* get display panel */
                panel = SCVUtils.getSheetPanel(sheet);
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getEditorPanel() {
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postLoadCallback() {
        BusyScreenUtil.startBusy("Loading Data");
        try {
            SCVUtils.loadDataOntoUI(sheet);
            BusyScreenUtil.tickBusy();
            SCVUtils.setSheetModified(
                sheet,
                false);
            BusyScreenUtil.tickBusy();
            SCVUtils.buildPanel(sheet);
            BusyScreenUtil.tickBusy();
            panel = SCVUtils.getSheetPanel(sheet);
            BusyScreenUtil.tickBusy();
            setChanged();
            notifyObservers();
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postLoadTemplateCallback() {
        BusyScreenUtil.startBusy("Loading Template");
        try {
            SCVUtils.loadDataOntoUI(sheet);
            /* BusyScreenUtil.tickBusy(); */
            /* SCVUtils.setSheetModified(sheet, false); */
            BusyScreenUtil.tickBusy();
            SCVUtils.buildPanel(sheet);
            BusyScreenUtil.tickBusy();
            panel = SCVUtils.getSheetPanel(sheet);
            BusyScreenUtil.tickBusy();
            setChanged();
            notifyObservers();
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postSaveCallback() {
        SCVUtils.setSheetModified(
            sheet,
            false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextPageCallback() {
        SCVUtils.nextPage(sheet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void previousPageCallback() {
        SCVUtils.previousPage(sheet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isModelModified() {
        return SCVUtils.isSheetModified(sheet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeCallback() {
        BusyScreenUtil.startBusy("Closing Sheet");
        try {
            SCVUtils.uninitializeSheet(sheet);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        } finally {
            BusyScreenUtil.endBusy();
        }
        // BusyScreenUtil.startBusy("Closing Sheet");
        // try {
        //
        // } catch (final Exception e) {
        // Reporting.exception(e);
        // } finally {
        // BusyScreenUtil.endBusy();
        // }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateRawMetadata(
        final ByteBuffer metadataContents) {
        SCVUtils.validationCallback(
            metadataContents,
            sheet);
    }

    /* == Observer implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(
        final Observable o,
        final Object arg) {
        /* in case the sheet is modified in any way */
        panel = SCVUtils.getSheetPanel(sheet);
        setChanged();
        notifyObservers();
    }

}
