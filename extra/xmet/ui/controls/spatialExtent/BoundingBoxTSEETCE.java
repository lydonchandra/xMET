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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import n.ui.SwingUtils;
import xmet.profiles.geometry.impl2d.BoundingBox2d;
import xmet.profiles.geometry.impl2d.Point2d;

/**
 * Editor for Bounding Boxes Simple NSEW editing fields with an inclusive
 * checkbox.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class BoundingBoxTSEETCE
    extends TSEETreeCellEditor
    implements
    FocusListener {

    /** The bounding box. */
    private BoundingBox2d boundingBox;

    /** The north text field. */
    private JTextField northTextField;

    /** The east text field. */
    private JTextField eastTextField;

    /** The south text field. */
    private JTextField southTextField;

    /** The west text field. */
    private JTextField westTextField;

    /** The inclusive check box. */
    private JCheckBox inclusiveCheckBox;
    {
        // CHECKSTYLE OFF: MagicNumber
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "<html><b>Bounding Box"),
            "x=1;y=0;f=b;w=2;wx=1;wy=0;");
        inclusiveCheckBox = new JCheckBox(
            "inclusive",
            true);
        SwingUtils.GridBag.add(
            this,
            inclusiveCheckBox,
            "x=3;y=0;f=b;w=3;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "North:"),
            "x=3;y=1;f=b;wx=0;wy=0;");
        northTextField = new JTextField(
            8);
        SwingUtils.GridBag.add(
            this,
            northTextField,
            "x=4;y=1;f=b;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "East:"),
            "x=5;y=2;f=b;wx=0;wy=0;");
        eastTextField = new JTextField(
            8);
        SwingUtils.GridBag.add(
            this,
            eastTextField,
            "x=6;y=2;f=b;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "South:"),
            "x=3;y=3;f=b;wx=0;wy=0;");
        southTextField = new JTextField(
            8);
        SwingUtils.GridBag.add(
            this,
            southTextField,
            "x=4;y=3;f=b;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "West:"),
            "x=1;y=2;f=b;wx=0;wy=0;");
        westTextField = new JTextField(
            8);
        SwingUtils.GridBag.add(
            this,
            westTextField,
            "x=2;y=2;f=b;wx=1;wy=0;");
        inclusiveCheckBox.setOpaque(false);
        setPreferredSize(new Dimension(
            400,
            90));
        northTextField.addFocusListener(this);
        eastTextField.addFocusListener(this);
        southTextField.addFocusListener(this);
        westTextField.addFocusListener(this);
        inclusiveCheckBox.addFocusListener(this);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Instantiates a new bounding box tseetce.
     * @param tsee the tsee
     * @param sec the sec
     */
    BoundingBoxTSEETCE(
        final TreeSpatialExtentEditor tsee,
        final SpatialExtentControl sec) {
        super(tsee, sec);
    }

    /* == TSEETreeCellEditor implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void configureFor(
        final Object value) {
        boundingBox = (BoundingBox2d) value;
        eastTextField.setText(Double
            .toString(boundingBox.getSouthEast().getX()));
        northTextField.setText(Double.toString(boundingBox
            .getNorthWest()
            .getY()));
        southTextField.setText(Double.toString(boundingBox
            .getSouthEast()
            .getY()));
        westTextField.setText(Double
            .toString(boundingBox.getNorthWest().getX()));
        inclusiveCheckBox.setSelected(boundingBox.isInclusive());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        try {
            ((Point2d) boundingBox.getSouthEast()).setX(Double
                .parseDouble(eastTextField.getText()));
        } catch (final Exception e) {
            ((Point2d) boundingBox.getSouthEast()).setX(0);
        }
        try {
            ((Point2d) boundingBox.getNorthWest()).setY(Double
                .parseDouble(northTextField.getText()));
        } catch (final Exception e) {
            ((Point2d) boundingBox.getNorthWest()).setY(0);
        }
        try {
            ((Point2d) boundingBox.getSouthEast()).setY(Double
                .parseDouble(southTextField.getText()));
        } catch (final Exception e) {
            ((Point2d) boundingBox.getSouthEast()).setY(0);
        }
        try {
            ((Point2d) boundingBox.getNorthWest()).setX(Double
                .parseDouble(westTextField.getText()));
        } catch (final Exception e) {
            ((Point2d) boundingBox.getNorthWest()).setX(0);
        }
        try {
            boundingBox.setInclusive(inclusiveCheckBox.isSelected());
        } catch (final Exception e) {
            boundingBox.setInclusive(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem() {
        return boundingBox;
    }

    /* == FocusListener implementation == */
    /*
     * Using Focus Listener instead of document listener. Less taxing and works.
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void focusGained(
        final FocusEvent arg0) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focusLost(
        final FocusEvent arg0) {
        commit();
        softChangeNotification();
    }

}
