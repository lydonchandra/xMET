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
import xmet.profiles.geometry.impl2d.SurfaceHole2d;

/**
 * For editing surface holes.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class SurfaceHoleTSEETCE
    extends TSEETreeCellEditor {

    /** The hole. */
    private SurfaceHole2d hole;
    {
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "<html><b>Surface patch/hole "),
            "x=1;f=b;wx=1;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "+",
                new Callback() {

                    @Override
                    public void callback() {
                        hole.getBoundaryPoints().add(
                            new Point2d(
                                0,
                                0));
                        changeNotification();
                    }
                }),
            "x=2;");
    }

    /**
     * Instantiates a new surface hole tseetce.
     * @param tsee the tsee
     * @param sec the sec
     */
    SurfaceHoleTSEETCE(
        final TreeSpatialExtentEditor tsee,
        final SpatialExtentControl sec) {
        super(tsee, sec);
    }

    /* == SurfaceHoleTSEETCE Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void configureFor(
        final Object value) {
        hole = (SurfaceHole2d) value;
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
        return hole;
    }
}
