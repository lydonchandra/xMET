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

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import n.ui.SwingUtils;
import xmet.profiles.geometry.impl2d.Point2d;

/**
 * For editing points.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
class PointTSEETCE
    extends TSEETreeCellEditor
    implements
    FocusListener {

    /** The point. */
    private Point2d point;

    /** The longitude editing field. */
    private JTextField longitudeEditingField;

    /** The latitude editing field. */
    private JTextField latitudeEditingField;
    {
        // CHECKSTYLE OFF: MagicNumber
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "<html><b>Point"),
            "w=rem;f=b;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Latitude:"),
            "w=rel;f=b;wx=1;wy=0;");
        latitudeEditingField = new JTextField(
            8);
        SwingUtils.GridBag.add(
            this,
            latitudeEditingField,
            "w=rem;f=b;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Longitude:"),
            "w=rel;f=b;wx=1;wy=0;");
        longitudeEditingField = new JTextField(
            8);
        SwingUtils.GridBag.add(
            this,
            longitudeEditingField,
            "w=rem;f=b;wx=1;wy=0;");
        latitudeEditingField.addFocusListener(this);
        longitudeEditingField.addFocusListener(this);
        setPreferredSize(new Dimension(
            200,
            70));
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Instantiates a new point tseetce.
     * @param tsee the tsee
     * @param sec the sec
     */
    PointTSEETCE(
        final TreeSpatialExtentEditor tsee,
        final SpatialExtentControl sec) {
        super(tsee, sec);
    }

    /* == TSEETreeCellEditor implemenation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureFor(
        final Object value) {
        point = (Point2d) value;
        longitudeEditingField.setText(Double.toString(point.getX()));
        latitudeEditingField.setText(Double.toString(point.getY()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        try {
            point.setX(Double.parseDouble(longitudeEditingField.getText()));
        } catch (final Exception e) {
            point.setX(0);
        }
        try {
            point.setY(Double.parseDouble(latitudeEditingField.getText()));
        } catch (final Exception e) {
            point.setY(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem() {
        return point;
    }

    /* == FocusListener implemenation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void focusGained(
        final FocusEvent e) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focusLost(
        final FocusEvent e) {
        commit();
        softChangeNotification();
    }

}
