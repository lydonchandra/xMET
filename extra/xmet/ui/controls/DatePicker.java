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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import n.io.CSC;
import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;

import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;

import xmet.ClientContext;

/**
 * Control for picking up dates.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
@CSC("DatePicker")
public class DatePicker
    extends GUIObject
    implements
    DocumentListener {

    /** The gco date format. - default format */
    private final SimpleDateFormat defaultFormat = new SimpleDateFormat(
        "yyyy-MM-dd");

    /** The date picker. */
    private final JTextField datePicker = new JTextField();

    /**
     * Instantiates a new date time picker.
     * @param context the context
     */
    public DatePicker(
        final ClientContext context) {
        // CHECKSTYLE OFF: MagicNumber
        super(context);

        setLayout(new BorderLayout());
        add(datePicker);
        if (getContext() == null) {
            final Object[] params = {};
            add(
                SwingUtils.BUTTON.getNew(
                    "set",
                    new ClassMethodCallback(
                        this,
                        "showDateSelectionDialog",
                        params)),
                BorderLayout.EAST);
        } else {
            final Object[] params = {};
            add(
                SwingUtils.BUTTON.getNew(
                    getContext().getResources().getImageIconResource(
                        "images/control.datePicker.png"),
                    new ClassMethodCallback(
                        this,
                        "showDateSelectionDialog",
                        params)),
                BorderLayout.EAST);
        }
        datePicker.setEditable(true);
        datePicker.getDocument().addDocumentListener(
            this);
        /* datePicker.setFormats(defaultFormat); */
        datePicker.setToolTipText("<html><p>"
            + "Type in a date in the example format or use"
            + " then date picker<br />by clicking the down"
            + " arrow at the right");

        SwingUtils.TEXTCOMPONENT.addSimpleUndoOperation(
            datePicker,
            30);
        /* datePicker.getEditor().setText(""); */
        // CHECKSTYLE ON: MagicNumber
    }

    /* == Misc Initialization methods == */

    /**
     * Sets the date format.
     * @param format the new format
     */
    public void setFormat(
        final String format) {
        try {
            /* datePicker.setFormats(new SimpleDateFormat(format)); */
            Reporting.logUnexpected();
        } catch (final Throwable t) {
            Reporting.reportUnexpected("invalid date format"
                + " - "
                + "Invalid date format "
                + format
                + " specified");
        }
    }

    /* == GUIObject overrrides == */
    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        try {
            return datePicker.getText();
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        super.disableNotifications();
        if ((value != null)
            && (value.trim().length() > 0)) {
            try {
                datePicker.setText(value);
                /* datePicker.setDate(defaultFormat.parse(value)); */
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            datePicker.setText("");
        }
        super.setValue(getValue());
        super.enableNotifications();
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

    /* == Date Selection Dialog == */

    /** The dialog. */
    private JDialog dialog = null;

    /**
     * Show date selection dialog.
     */
    public synchronized void showDateSelectionDialog() {
        // CHECKSTYLE OFF: MagicNumber
        /* get rid of old dialog */
        if (dialog != null) {
            dialog.dispose();
            dialog = null;
        }
        /* set up month view */
        final JXMonthView mv = new JXMonthView();
        mv.setShowingWeekNumber(true);
        mv.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        mv.setTraversable(true);
        mv.setPreferredRowCount(1);
        mv.setPreferredColumnCount(1);
        /* set date */
        try {
            final Date date = defaultFormat.parse(datePicker.getText());
            mv.setSelectionDate(date);
            mv.setFirstDisplayedDay(date);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        /* when date is selected */
        mv.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent e) {
                datePicker.setText(defaultFormat.format(((JXMonthView) e
                    .getSource()).getSelection().first()));
                dialog.setVisible(false);
            }
        });
        /* when user clicks somewhere else */
        mv.setFocusable(true);
        /* mv.getRootPane().setFocusable(true); */
        mv.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(
                final FocusEvent arg0) {
                /* Reporting.log("focus lost"); */
                dialog.setVisible(false);
            }

            @Override
            public void focusGained(
                final FocusEvent arg0) {
                /* Reporting.log("focus gained"); */

            }
        });
        /* wrap it in a panel */
        final JPanel panel = SwingUtils.BorderLayouts.getNew();
        panel.add(mv);
        /* set up next dialog */
        dialog = SwingUtils.DIALOG.createDialog(
            panel,
            "Please select a date",
            800,
            600,
            false);
        dialog.setUndecorated(true);
        dialog.getRootPane().setWindowDecorationStyle(
            JRootPane.NONE);
        final Point p = datePicker.getLocationOnScreen();
        p.translate(
            0,
            datePicker.getHeight());
        dialog.setLocation(p);
        dialog.pack();
        mv.requestFocusInWindow();
        dialog.setVisible(true);
        // CHECKSTYLE ON: MagicNumber
    }

}
