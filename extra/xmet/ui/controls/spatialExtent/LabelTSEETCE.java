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

import javax.swing.JLabel;

import n.ui.SwingUtils;

/**
 * This editing item simply displays a label.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
class LabelTSEETCE
    extends TSEETreeCellEditor {

    /** The label control. */
    private final JLabel labelControl = new JLabel();
    {
        SwingUtils.GridBag.add(
            this,
            labelControl,
            "f=b;w=rem;");
    }

    /**
     * Instantiates a new label tseetce.
     * @param lbl the lbl
     * @param tsee the tsee
     * @param sec the sec
     */
    LabelTSEETCE(
        final String lbl,
        final TreeSpatialExtentEditor tsee,
        final SpatialExtentControl sec) {
        super(tsee, sec);
        labelControl.setText("<html><b>"
            + lbl);

    }

    /* == TSEETCE Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void configureFor(
        final Object value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem() {
        return null;
    }
}
