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

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import n.reporting.Reporting;
import n.ui.SwingUtils;

/**
 * For editing digital size contents.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class DigitalContentSizeEditor
    extends GUIObject
    implements
    DocumentListener,
    ActionListener {

    /** The entry field. */
    private final JTextField entryField;

    /** The unit combo box. */
    private final JComboBox unitComboBox;

    // /** The value. */
    // private final long value = 0;

    /** The io metric. */
    private int ioMetric = 0;

    /** The Constant sizeUnitSuffix. */
    private static final String[] SIZE_UNIT_SUFFIX = {
    "B",
    "kB",
    "MB",
    "GB",
    "TB",
    "PB",
    "EB"
    };

    /** The Constant sizeUnitSuffixMultiplier. */
    private static final double[] SIZE_UNIT_SUFFIX_MULT = {
    1L,
    1000L,
    1000000L,
    1000000000L,
    1000000000000L,
    1000000000000000L,
    1000000000000000000L
    };

    /**
     * Instantiates a new digital content size editor.
     */
    public DigitalContentSizeEditor() {
        // CHECKSTYLE OFF: MagicNumber
        setLayout(new BorderLayout());
        entryField = new JTextField();
        unitComboBox = new JComboBox(
            SIZE_UNIT_SUFFIX);
        unitComboBox.setSelectedIndex(0);
        removeAll();
        add(entryField);
        add(
            unitComboBox,
            BorderLayout.EAST);
        entryField.getDocument().addDocumentListener(
            this);
        unitComboBox.addActionListener(this);

        SwingUtils.TEXTCOMPONENT.addSimpleUndoOperation(
            entryField,
            30);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String aValue) {
        super.disableNotifications();
        String varValue = aValue;
        if (varValue != null) {
            int sourceMetric = ioMetric;
            for (int i = SIZE_UNIT_SUFFIX.length - 1; i >= 0; i--) {
                if (varValue.endsWith(SIZE_UNIT_SUFFIX[i])) {
                    sourceMetric = i;
                    varValue = varValue.substring(
                        0,
                        varValue.length()
                            - SIZE_UNIT_SUFFIX[i].length());
                    break;
                }
            }
            if ((ioMetric < 0)
                || (ioMetric >= SIZE_UNIT_SUFFIX_MULT.length)) {
                ioMetric = sourceMetric;
            }

            try {
                if (varValue.indexOf(',') != -1) {
                    varValue = varValue.replaceAll(
                        ",",
                        "");
                }
                final Double dVal = Double.parseDouble(varValue);
                if (dVal != null) {
                    final long bytes =
                        (long) (dVal * SIZE_UNIT_SUFFIX_MULT[sourceMetric]);
                    int newSelected = 0;
                    for (int i = 1; i < SIZE_UNIT_SUFFIX_MULT.length; i++) {
                        if (bytes > SIZE_UNIT_SUFFIX_MULT[i]) {
                            newSelected = i;
                        }
                    }
                    unitComboBox.setSelectedIndex(newSelected);
                    final double ret = (bytes)
                        / SIZE_UNIT_SUFFIX_MULT[newSelected];
                    entryField.setText(Double.toString(ret));
                }
            } catch (final RuntimeException e) {
                entryField.setText(varValue);
                unitComboBox.setSelectedIndex(0);
            }
        } else {
            entryField.setText("");
            unitComboBox.setSelectedIndex(0);
        }
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        if ((ioMetric < 0)
            || (ioMetric >= SIZE_UNIT_SUFFIX_MULT.length)) {
            ioMetric = 0;
        }
        try {
            String val = entryField.getText();
            if (val.indexOf(',') != -1) {
                val = val.replaceAll(
                    ",",
                    "");
            }
            final Double dVal = Double.parseDouble(val);
            if (dVal != null) {
                final int selectedMetric = unitComboBox.getSelectedIndex();
                final long bytes =
                    (long) (dVal * SIZE_UNIT_SUFFIX_MULT[selectedMetric]);
                final double ret = (bytes)
                    / SIZE_UNIT_SUFFIX_MULT[ioMetric];
                return Double.toString(ret);
            }
        } catch (final Exception e) {
            Reporting.logUnexpected(e);
        }
        return entryField.getText();
    }

    /**
     * Sets the metric.
     * @param metric the new metric
     */
    public void setMetric(
        final String metric) {
        if (metric != null) {
            for (int i = SIZE_UNIT_SUFFIX.length - 1; i >= 0; i--) {
                if (metric.equals(SIZE_UNIT_SUFFIX[i])) {
                    ioMetric = i;
                    return;
                }
            }
        }
        Reporting.logUnexpected("invalid value");
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

    /**
     * Gets the size unit suffix.
     * @return the size unit suffix
     */
    public static final String[] getSizeUnitSuffix() {
        return SIZE_UNIT_SUFFIX.clone();
    }
}
