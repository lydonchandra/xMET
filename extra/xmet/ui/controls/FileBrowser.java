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

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import n.ui.JFileChooserUtils;
import n.ui.SwingUtils;

/**
 * For selecting a local file url.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class FileBrowser
    extends GUIObject
    implements
    ActionListener,
    DocumentListener {

    /** The borwse button. */
    private final JButton borwseButton;

    /** The text field. */
    private final JTextField textField;

    /**
     * Instantiates a new file browser.
     */
    public FileBrowser() {
        setLayout(new GridBagLayout());
        textField = new JTextField();

        SwingUtils.GridBag.add(
            this,
            textField,
            "w=rel;f=b;wx=1;wy=1");
        borwseButton = new JButton(
            "Browse");
        SwingUtils.GridBag.add(
            this,
            textField,
            "w=rem;f=b;wx=0;wy=1");

        /* textField.setEditable(false); */
        textField.getDocument().addDocumentListener(
            this);

        borwseButton.addActionListener(this);
        setToolTipText("Select a file by clicking on the browse button");
    }

    /* == GUIObject overrides == */
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
        super.setValue(textField.getText());
        super.enableNotifications();
    }

    /* == ActionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        final File file = JFileChooserUtils.getSingleOpenFile();
        if (file != null) {
            final URI uri = file.toURI();
            textField.setText(uri.toASCIIString());
            super.notifyObserversIfChanged();
        }
    }

    /* == DocumentListener implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpdate(
        final DocumentEvent e) {
        super.notifyObserversIfChanged();
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
