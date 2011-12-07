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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import n.io.CSC;
import n.ui.SwingUtils;

/**
 * A single line free text editing field.
 * @author Shaan
 */
@SuppressWarnings("serial")
@CSC("SingleLineText")
public class SingleLineText
    extends GUIObject
    implements
    DocumentListener {

    /** The text field. */
    private final JTextField textField;

    /**
     * Instantiates a new single line text.
     */
    public SingleLineText() {
        // CHECKSTYLE OFF: MagicNumber
        super();
        textField = new JTextField();
        textField.getDocument().addDocumentListener(
            this);
        SwingUtils.TEXTCOMPONENT.addSimpleUndoOperation(
            textField,
            30);
        setLayout(new BoxLayout(
            this,
            BoxLayout.Y_AXIS));
        this.add(textField);
        textField.setToolTipText("Insert text here");
        // CHECKSTYLE ON: MagicNumber
    }

    /* == GUIObject Overrides == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return textField.getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        super.disableNotifications();
        if (value != null) {
            textField.setText(value);
        } else {
            textField.setText("");
        }
        super.setValue(getValue());
        super.enableNotifications();
    }

    /* == DocumentListener implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpdate(
        final DocumentEvent e) {
        notifyObserversIfChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUpdate(
        final DocumentEvent e) {
        removeUpdate(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedUpdate(
        final DocumentEvent e) {
        removeUpdate(e);
    }

}
