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
package xmet.ui.controls.spatialExtent;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

/**
 * A Tree Spatial Extent Editor Tree Cell Editor.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public abstract class TSEETreeCellEditor
    extends JPanel {

    /** The extent view. */
    private TreeSpatialExtentEditor extentView;

    /** The extent control. */
    private SpatialExtentControl extentControl;

    /**
     * Instantiates a new sE cell editor.
     * @param tsee the tsee
     * @param sec the sec
     */
    TSEETreeCellEditor(
        final TreeSpatialExtentEditor tsee,
        final SpatialExtentControl sec) {
        this.extentView = tsee;
        this.extentControl = sec;
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

    /**
     * Configure for.
     * @param value the value
     */
    public abstract void configureFor(
        Object value);

    /**
     * Commit.
     */
    public abstract void commit();

    /**
     * Gets the item.
     * @return the item
     */
    public abstract Object getItem();

    /**
     * Gets the last row.
     * @return the last row
     */
    public int getLastRow() {
        return lastRow;
    }

    /**
     * Sets the last row.
     * @param aLastRow the new last row
     */
    public void setLastRow(
        final int aLastRow) {
        this.lastRow = aLastRow;
    }

    /** The last row. */
    private int lastRow;

    /**
     * Gets the extent view.
     * @return the extent view
     */
    public TreeSpatialExtentEditor getExtentView() {
        return extentView;
    }

    /**
     * Sets the extent view.
     * @param aExtentView the new extent view
     */
    public void setExtentView(
        final TreeSpatialExtentEditor aExtentView) {
        this.extentView = aExtentView;
    }

    /**
     * Gets the extent control.
     * @return the extent control
     */
    public SpatialExtentControl getExtentControl() {
        return extentControl;
    }

    /**
     * Sets the extent control.
     * @param aExtentControl the new extent control
     */
    public void setExtentControl(
        final SpatialExtentControl aExtentControl) {
        this.extentControl = aExtentControl;
    }

    /**
     * Change notification.
     */
    protected void changeNotification() {
        extentView.changeNotification(lastRow);
    }

    /**
     * Soft change notification.
     */
    protected void softChangeNotification() {
        extentView.softChangeNotification(lastRow);
    }

}
