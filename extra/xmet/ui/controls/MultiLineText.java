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

import java.awt.KeyboardFocusManager;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import n.io.CSC;
import n.ui.SwingUtils;

/**
 * A MultiLine free text editing field.
 * @author Shaan
 */
@SuppressWarnings("serial")
@CSC("MultiLineText")
public class MultiLineText
    extends GUIObject
    implements
    DocumentListener,
    CompositeGUIObject<String> {

    /** The text area. */
    private final JTextArea textArea;

    /**
     * Instantiates a new multi line text.
     */
    public MultiLineText() {
        // CHECKSTYLE OFF: MagicNumber
        super();
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        /* http://tech.stolsvik.com/2010/02/jtextarea-vs-tab-focus-cycle.html */
        /* enables focus travasal with tab keys */
        /* only solution that seems to work */
        textArea.setFocusTraversalKeys(
            KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
            null);
        textArea.setFocusTraversalKeys(
            KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
            null);
        // ---------------------------------------------------

        textArea.getDocument().addDocumentListener(
            this);

        setLayout(new BoxLayout(
            this,
            BoxLayout.Y_AXIS));

        this.add(new JScrollPane(
            textArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));

        textArea.setToolTipText("Insert Text here");

        SwingUtils.TEXTCOMPONENT.addSimpleUndoOperation(
            textArea,
            30);

        // CHECKSTYLE ON: MagicNumber
    }

    /* == GUIObject Overrides == */

    /* == GUIObject overrides == */
    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return textArea.getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        super.disableNotifications();
        if (value != null) {
            textArea.setText(value);
        } else {
            textArea.setText("");
        }
        super.setValue(value);
        super.enableNotifications();
    }

    /* == CompositeGUIObject<String> implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void setValues(
        final String[] extent) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < extent.length; i++) {
            sb.append(extent[i]);
            sb.append('\n');
        }
        textArea.getDocument().removeDocumentListener(
            this);
        textArea.setText(sb.toString().trim());
        textArea.getDocument().addDocumentListener(
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getValues() {
        final Scanner s = new Scanner(
            textArea.getText());
        final ArrayList<String> values = new ArrayList<String>();
        while (s.hasNext()) {
            String content = s.nextLine();
            if (content != null
                && content.trim().length() != 0) {
                content = content.trim();
                values.add(content);
            }
        }
        return values.toArray(new String[values.size()]);
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
