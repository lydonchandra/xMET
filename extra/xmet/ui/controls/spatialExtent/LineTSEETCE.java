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
import n.ui.patterns.callback.Callback;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Polyline2d;

/**
 * For editing PolyLine.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class LineTSEETCE
    extends TSEETreeCellEditor {

    /** The line. */
    private Polyline2d line;
    {
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "<html><b>Line "),
            "w=rel;f=b;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "+",
                new Callback() {

                    @Override
                    public void callback() {
                        line.getPoints().add(
                            new Point2d(
                                0,
                                0));
                        changeNotification();
                    }
                }),
            "w=rem;f=b;wx=1;wy=0;");
    }

    /**
     * Instantiates a new line tseetce.
     * @param tsee the tsee
     * @param sec the sec
     */
    LineTSEETCE(
        final TreeSpatialExtentEditor tsee,
        final SpatialExtentControl sec) {
        super(tsee, sec);
    }

    /* == TSEETreeCellEditor Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void configureFor(
        final Object value) {
        line = (Polyline2d) value;
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
        return line;
    }

}
