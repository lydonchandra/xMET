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
package xmet.ui.controls;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import n.io.CSC;

/**
 * Simple label.
 * @author Shaan
 */
@SuppressWarnings("serial")
@CSC("Label")
public class Label
    extends GUIObject {

    /** The label. */
    private final JLabel label;

    /**
     * Creates a default label GUIObject.
     */
    public Label() {
        super();
        label = new JLabel(
            " ");
        setLayout(new BoxLayout(
            this,
            BoxLayout.Y_AXIS));
        /* label.setPreferredSize(new Dimension(0, 0)); */
        this.add(label);
    }

    /**
     * Creates a Label object displaying the specified text.
     * @param text the text
     */
    public Label(
        final String text) {
        this();
        setValue(text);
        label.setText(text);
    }

    /* == GUIObject Overrides == */
    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return super.getInternalValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String text) {
        String varValue = null;
        if (text != null) {
            varValue = text.trim();
        } else {
            varValue = "";
        }
        super.setValue(varValue);
        label.setText(getInternalValue());
    }

}
