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
package xmet.tools.metadata.editor.views.scv.view;

import java.awt.EventQueue;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JPanel;

import n.io.xml.SAXONUtils;
import n.reporting.Reporting;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;
import xmet.tools.metadata.editor.views.scv.utils.CodeExecutorUtil;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;

/**
 * DC for Sheets.
 * @author Nahid Akbar
 */
public class SheetDC
    extends DisplayContext
    implements
    Runnable {

    /** The Constant VALIDATION_CHECK_INTERVAL. */
    private static final int VALIDATION_CHECK_INTERVAL = 350;

    /** The display panel. */
    private final JPanel displayPanel;

    /**
     * The sheet.
     */
    private final Sheet sheet;

    /**
     * Instantiates a new sheet display context.
     * @param aSheet the sheet
     * @param panel the panel
     */
    public SheetDC(
        final Sheet aSheet,
        final JPanel panel) {
        this.sheet = aSheet;
        displayPanel = panel;
        startValidationUpdateThread();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildDisplayPanel() {
        new PanelBuilderUtil(
            getSheet());
        getSheet().notifyObservers();
    }

    /**
     * Next page.
     */
    public void nextPage() {

    }

    /**
     * Previous page.
     */
    public void previousPage() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getDisplayPanel() {
        return displayPanel;
    }

    /* == Validation Implemenation == */
    /** The validation queue. */
    private final Queue<ParentItem> validationQueue =
        new LinkedList<ParentItem>();
    /** The needs validation flag. */
    /* boolean needsValidationFlag = false; */

    /** The stop validation thread flag. */
    private boolean stopValidationThreadFlag;

    /**
     * Re validate.
     * @param parentPage the parent page
     */
    public synchronized void reValidate(
        final ParentItem parentPage) {
        /* needsValidationFlag = true; */
        if (!validationQueue.contains(parentPage)) {
            Reporting.logExpected(
                "reValidate %1$s",
                parentPage);
            validationQueue.offer(parentPage);
        }
    }

    /**
     * Update validation.
     */
    protected synchronized void updateValidation() {
        /* if (needsValidationFlag) { */
        // /*Reporting.log("validation update"); */
        /* new ValidationUpdateUtil(sheet); */
        /* needsValidationFlag = false; */
        // }
        if (validationQueue.size() > 0) {
            final ParentItem item = validationQueue.remove();
            new ValidationUpdateUtil(
                item);
        }
    }

    /** The validation update thread. */
    private Thread validationUpdateThread = null;

    /** The global sheet dc utilization counter. */
    private static int globalSheetDCUtilizationCounter = 0;

    /** The sheet dc no. */
    private final int sheetDCNo = globalSheetDCUtilizationCounter++;

    /**
     * Start validation update thread.
     */
    private void startValidationUpdateThread() {
        stopValidationUpdateThread();
        /* Reporting.log("startValidationUpdateThread(%1$d)", sheetDCNo); */
        validationUpdateThread = new Thread(
            new Runnable() {

                @Override
                public void run() {
                    /* Reporting.log("validation start %1$d", sheetDCNo); */
                    /* needsValidationFlag = true; */
                    reValidate(getSheet());
                    while (!stopValidationThreadFlag) {
                        try {
                            Thread.sleep(VALIDATION_CHECK_INTERVAL);
                            EventQueue.invokeLater(SheetDC.this);
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            },
            "SCV Validation Thread "
                + sheetDCNo);
        stopValidationThreadFlag = false;
        validationUpdateThread.start();
    }

    /**
     * Stop validation update thread.
     */
    public void stopValidationUpdateThread() {
        if (validationUpdateThread != null) {
            stopValidationThreadFlag = true;
            try {
                validationUpdateThread.interrupt();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            validationUpdateThread = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        updateValidation();
    }

    /**
     * Recache pages.
     */
    public void recachePages() {
        Reporting.logUnexpected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize()
        throws Throwable {
        stopValidationUpdateThread();
        super.finalize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshPanel() {
        getDisplayPanel().revalidate();
    }

    /**
     * Full revalidate.
     * @param metadataContents the metadata contents
     */
    public void fullRevalidate(
        final ByteBuffer metadataContents) {
        validationQueue.offer(getSheet());
        updateValidation();
        if (getSheet().getValidationXSL() != null
            && getSheet().getValidationXSL().trim().length() > 0) {
            getSheet().setValidationXSL(
                getSheet().getValidationXSL().trim());
            try {
                String results = SAXONUtils.transformString(
                    getSheet().getValidationXSL(),
                    new String(
                        metadataContents.array()));
                Reporting.logExpected(
                    "Results: %1$s",
                    results);
                if (results != null
                    && results.trim().length() > 0) {
                    results = results.trim();
                    final ModelItem item =
                        SCVUtils.modelItemFromString(results);
                    if (item != null) {
                        SCVUtils.accept(
                            item,
                            new CodeExecutorUtil(),
                            getSheet());
                    } else {
                        Reporting.logExpected(
                            "%1$s",
                            results);
                    }
                }
            } catch (final Exception e) {
                Reporting.reportUnexpected(e);
            }
        }
    }

    /**
     * Gets the sheet.
     * @return the sheet
     */
    public Sheet getSheet() {
        return sheet;
    }
}
