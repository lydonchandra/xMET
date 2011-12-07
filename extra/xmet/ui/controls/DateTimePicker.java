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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import n.io.CSC;
import n.ui.SwingUtils;
import xmet.ClientContext;

/**
 * For picking both date and time.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
@CSC("DateTimePicker")
public class DateTimePicker
    extends GUIObject
    implements
    Observer {

    /** The date picker. */
    private final DatePicker datePicker;

    /** The time spinner. */
    private final JSpinner timeSpinner;

    /** The time editor. */
    private final JSpinner.DateEditor timeEditor;

    /** The enabled checkbox. */
    private JCheckBox enabledCheckbox;

    /**
     * Instantiates a new date time picker.
     * @param context the context
     */
    @SuppressWarnings("deprecation")
    public DateTimePicker(
        final ClientContext context) {
        super(context);
        datePicker = new DatePicker(
            context);
        datePicker.addObserver(this);
        timeSpinner = new JSpinner(
            new SpinnerDateModel());
        timeEditor = new JSpinner.DateEditor(
            timeSpinner,
            "HH:mm:ss");
        timeSpinner.setEditor(timeEditor);
        enabledCheckbox = new JCheckBox();
        setLayout(new GridBagLayout());
        SwingUtils.GridBag.add(
            this,
            datePicker,
            "x=0;y=0;f=b;wx=1;wy=1;");
        enabledCheckbox = new JCheckBox();
        SwingUtils.GridBag.add(
            this,
            enabledCheckbox,
            "x=1;y=0;f=b;wx=0;wy=1;");
        SwingUtils.GridBag.add(
            this,
            timeSpinner,
            "x=2;y=0;f=b;wx=0;wy=1;");
        timeSpinner.setValue(new Date(
            0,
            0,
            0,
            0,
            0,
            0));
        timeSpinner.setEnabled(false);
        enabledCheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent e) {
                if (isNotificationEnabled()) {
                    if (enabledCheckbox.isSelected()) {
                        timeSpinner.setEnabled(true);
                        timeSpinner.setValue(new Date());
                    } else {
                        timeSpinner.setEnabled(false);
                        timeSpinner.setValue(new Date(
                            0,
                            0,
                            0,
                            0,
                            0,
                            0));
                    }
                    /* notifyObservers(); */
                }
            }
        });
        timeSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(
                final ChangeEvent arg0) {
                notifyObserversIfChanged();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        if (enabledCheckbox.isSelected()) {
            return String.format(
                "%1$sT%2$TH:%2$TM:%2$TS",
                datePicker.getValue(),
                timeSpinner.getValue());
        } else {
            return datePicker.getValue();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        super.disableNotifications();
        /* Reporting.log("%1$s", value); */
        String varValue = value;
        try {
            if (varValue != null
                && varValue.trim().length() != 0) {
                varValue = varValue.trim();
                final int index = varValue.lastIndexOf("T") + 1;
                if (index >= 1) {
                    final String timeValue = varValue.substring(index);
                    if (timeValue.matches("(\\d+)(:\\d+(:\\d+)?)?$")) {
                        final String dateValue = varValue.substring(
                            0,
                            index - 1);
                        datePicker.setValue(dateValue);
                        try {
                            final SimpleDateFormat timeFormat =
                                new SimpleDateFormat(
                                    "HH:mm:ss");
                            final Date d = timeFormat.parse(timeValue);
                            timeSpinner.setValue(d);
                            enabledCheckbox.setSelected(true);
                            timeSpinner.setEnabled(true);
                            super.enableNotifications();
                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (!super.isNotificationEnabled()) {
            datePicker.setValue(varValue);
            enabledCheckbox.setSelected(false);
            timeSpinner.setEnabled(false);
        }
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(
        final Observable o,
        final Object arg) {
        notifyObserversIfChanged();
    }

}
