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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/**
 * Single checkbox control which sets a value if an only if the user checks the
 * box.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class Checkbox
    extends GUIObject
    implements
    ActionListener {

    /** The check box control. */
    private final JCheckBox checkBox;

    /** The return value. */
    private String returnValue = "true";

    /**
     * Sets the return value.
     * @param aReturnValue the new return value
     */
    public void setReturnValue(
        final String aReturnValue) {
        this.returnValue = aReturnValue;
    }

    /**
     * Instantiates a new checkbox.
     */
    public Checkbox() {
        setLayout(new BorderLayout());
        checkBox = new JCheckBox();
        add(checkBox);
        checkBox.addActionListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        if (checkBox.isSelected()) {
            return returnValue;
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        super.disableNotifications();
        checkBox.setSelected((value != null)
            && (value.trim().length() > 0));
        super.setValue(value);
        super.enableNotifications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent e) {
        super.notifyObserversIfChanged();
    }
}
