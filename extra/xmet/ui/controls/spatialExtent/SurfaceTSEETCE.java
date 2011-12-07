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

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import n.ui.SwingUtils;
import n.ui.patterns.callback.Callback;
import xmet.profiles.geometry.impl2d.Point2d;
import xmet.profiles.geometry.impl2d.Surface2d;
import xmet.profiles.geometry.impl2d.SurfaceHole2d;

/**
 * For editing surfaces.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
class SurfaceTSEETCE
    extends TSEETreeCellEditor {

    /** The surface. */
    private Surface2d surface;

    /** The inclusive. */
    private JCheckBox inclusive;
    {
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "<html><b>Polygon "),
            "x=1;f=b;wx=1;");
        inclusive = new JCheckBox(
            "inclusive",
            true);
        SwingUtils.GridBag.add(
            this,
            inclusive,
            "x=2;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "+",
                new Callback() {

                    @Override
                    public void callback() {
                        addPoint();
                    }
                }),
            "x=3;");
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "+ hole/patch",
                new Callback() {

                    @Override
                    public void callback() {
                        addPatch();
                    }
                }),
            "x=4;");
        inclusive.setOpaque(false);
    }

    /**
     * Instantiates a new surface tseetce.
     * @param tsee the tsee
     * @param sec the sec
     */
    SurfaceTSEETCE(
        final TreeSpatialExtentEditor tsee,
        final SpatialExtentControl sec) {
        super(tsee, sec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureFor(
        final Object value) {
        surface = (Surface2d) value;
        inclusive.setSelected(surface.isInclusive());
        revalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        surface.setInclusive(inclusive.isSelected());
    }

    /**
     * Adds the point.
     */
    public void addPoint() {
        surface.getExteriorPoints().add(
            new Point2d(
                0,
                0));
        changeNotification();
    }

    /**
     * Adds the patch.
     */
    public void addPatch() {
        surface.getInteriorHoles().add(
            new SurfaceHole2d());
        changeNotification();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem() {
        return surface;
    }

}
