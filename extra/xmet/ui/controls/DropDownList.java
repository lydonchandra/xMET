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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import n.io.CSC;
import n.reporting.Reporting;
import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;

/**
 * For picking between a list of items.
 * @author Shaan
 */
@SuppressWarnings("serial")
@CSC("DropDownList")
public class DropDownList
    extends GUIObject
    implements
    ActionListener,
    DocumentListener {

    /** Control. */
    private JComboBox comboBox;

    /** The labels. */
    private List<String> labels = new ArrayList<String>();

    /** The values. */
    private List<String> values = new ArrayList<String>();

    /** The editable. */
    private boolean editable;

    /**
     * Instantiates a new drop down list.
     */
    public DropDownList() {
        super();
        rebuild();
    }

    /**
     * Rebuild.
     */
    private synchronized void rebuild() {
        removeAll();
        JComboBox varJComboBox = null;
        if (labels != null) {
            varJComboBox = new JComboBox(
                labels.toArray());
        } else {
            varJComboBox = new JComboBox();
        }
        comboBox = varJComboBox;
        if (comboBox.getItemCount() == 0) {
            values.add("");
            labels.add("");
            comboBox.addItem("");
            comboBox.setSelectedIndex(0);
        }
        comboBox.addActionListener(this);
        comboBox.setToolTipText("Select an item from the list"
            + " by clicking on the arrow at the right.");
        setLayout(new BoxLayout(
            this,
            BoxLayout.Y_AXIS));
        this.add(comboBox);
        comboBox.setEditable(editable);
        if (comboBox.isEditable()) {
            installTextComponentListeners();
        }
    }

    /*
     * == Helper Method to do with the text editing component of the ComboBox
     * ================
     */

    /**
     * Install text component action and document listeners.
     */
    private void installTextComponentListeners() {
        comboBox.getEditor().addActionListener(
            this);
        try {
            final JTextComponent textComponent =
                (JTextComponent) comboBox.getEditor().getEditorComponent();
            if (textComponent != null) {
                textComponent.getDocument().addDocumentListener(
                    this);
            } else {
                Reporting.logUnexpected("Could not find base text"
                    + " component of JComboBox");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the text component text.
     * @return the text component text
     */
    private String getTextComponentText() {
        final JTextComponent textComponent =
            (JTextComponent) comboBox.getEditor().getEditorComponent();
        if (textComponent != null) {
            return textComponent.getText();
        } else {
            Reporting.logUnexpected("Could not find base"
                + " text component of JComboBox");
        }
        return null;
    }

    /**
     * Sets the text component text.
     * @param text the new text component text
     */
    private void setTextComponentText(
        final String text) {
        final JTextComponent textComponent =
            (JTextComponent) comboBox.getEditor().getEditorComponent();
        if (textComponent != null) {
            textComponent.setText(text);
        } else {
            Reporting.logUnexpected("Could not find base text"
                + " component of JComboBox");
        }
    }

    /* == GUIObject Overrides == */
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getValue() {
        String returnValue = null;
        if (comboBox.isEditable()) {
            try {
                returnValue = getTextComponentText();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            if ((comboBox.getSelectedIndex() > 0)
                && (comboBox.getSelectedIndex() <= values.size())) {
                returnValue = values.get(comboBox.getSelectedIndex());
            } else {
                returnValue = "";
            }
        }
        return returnValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setValue(
        final String value) {
        super.disableNotifications();
        /* Reporting.log("setValue(%1$s)", value); */
        if ((value != null)
            && (value.trim().length() > 0)) {
            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(
                        i).equals(
                        value)) {
                        comboBox.setSelectedIndex(i);
                        super.setValue(getValue());
                        super.enableNotifications();
                        return;
                    }
                }
                addValueLabelPair(
                    value,
                    null);
                rebuild();
                comboBox.setSelectedIndex(values.size() - 1);
            } else {
                values = new ArrayList<String>();
                labels = new ArrayList<String>();
                addValueLabelPair(
                    value,
                    null);
                rebuild();
                comboBox.setSelectedIndex(values.size() - 1);
            }
        } else {
            if (comboBox.getItemCount() > 0) {
                comboBox.setSelectedIndex(0);
                setTextComponentText("");
            } else {
                values.add("");
                labels.add("");
                comboBox.addItem("");
                comboBox.setSelectedIndex(0);
            }
        }
        super.setValue(getValue());
        super.enableNotifications();
    }

    /* == List Initialization methods == */
    /**
     * Sets the custom list.
     * @param customList the new custom list
     */
    public synchronized void setCustomList(
        final String customList) {
        super.disableNotifications();
        final String value = getValue();
        final String[] aValues = customList.split("\\|");
        /* comboBox.addItem(""); */
        this.values.clear();
        labels.clear();
        /* final List<String> valueList = new ArrayList<String>(); */
        addValueLabelPair(
            "",
            "");
        for (final String v : aValues) {
            /* comboBox.addItem(v); */
            addValueLabelPair(
                v.trim(),
                null);
        }
        /* labels = this.values = valueList; */
        rebuild();

        setValue(value);
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * Sets the code list.
     * @param codeList the new code list
     */
    public synchronized void setCodeList(
        final Codelist codeList) {
        super.disableNotifications();
        if (codeList != null) {
            final String value = getValue();
            /* comboBox.addItem(""); */
            values.clear();
            labels.clear();
            addValueLabelPair(
                "",
                "");
            for (final CodeItem c : codeList.getItems()) {
                addValueLabelPair(
                    c.getValue().trim(),
                    c.getLabel().trim());

                /* comboBox.addItem(c.getLabel().trim()); */
            }
            setValue(value);
        } else {
            Reporting.reportUnexpected("null codelist parameter");
        }
        rebuild();
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * Adds the value label pair.
     * @param value the value
     * @param label the label
     */
    private synchronized void addValueLabelPair(
        final String value,
        final String label) {
        values.add(value);
        if (label != null) {
            if (!value.equals(label)) {
                labels.add(value
                    + " ("
                    + label
                    + ")");
            } else {
                labels.add(label);
            }
        } else {
            labels.add(value);
        }
    }

    /**
     * Sets the editable.
     * @param value the new editable
     */
    public synchronized void setEditable(
        final String value) {
        editable = value.equals("true");
        rebuild();
    }

    /* == ActionListener Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent e) {
        notifyObserversIfChanged();
    }

    /* == DocumentListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void changedUpdate(
        final DocumentEvent arg0) {
        notifyObserversIfChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUpdate(
        final DocumentEvent arg0) {
        changedUpdate(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpdate(
        final DocumentEvent arg0) {
        changedUpdate(arg0);
    }

}
