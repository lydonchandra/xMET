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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import n.io.CSC;
import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;

/**
 * Checked List Implemented with a list of checkboxes in a.
 * @author Kyle Neideck
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
@CSC("CheckedList")
public class CheckedList
    extends GUIObject
    implements
    DocumentListener,
    CompositeGUIObject<String> {

    /** The items in the checked list. */
    private final List<CheckedListItem> items =
        new ArrayList<CheckedList.CheckedListItem>();

    /** The check boxes panel. */
    private JPanel checkBoxesPanel;

    /**
     * The editable flag - if editable, the othersField becomes visible for free
     * text input.
     */
    private boolean editable = false;
    // CHECKSTYLE OFF: MagicNumber
    /** The others field. */
    private final JTextField othersField = new JTextField(
        15);

    // CHECKSTYLE ON: MagicNumber
    /**
     * Instantiates a new checked list.
     */
    public CheckedList() {
        super();
        othersField.getDocument().addDocumentListener(
            this);
        rebuildPanel();
    }

    /**
     * Gets the check boxes panel.
     */
    private void rebuildPanel() {
        /* initialize */
        checkBoxesPanel = SwingUtils.BoxLayouts.getVerticalPanel();

        for (final CheckedListItem item : items) {
            if (item.checkBox == null) {
                item.createCheckbox();
            }
            checkBoxesPanel.add(item.checkBox);
            item.checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        if (editable) {
            final JPanel panel = GridBag.getNew();
            SwingUtils.GridBag.add(
                panel,
                new JLabel(
                    "Others: "),
                "w=rel;f=h;a=l;");
            SwingUtils.GridBag.add(
                panel,
                othersField,
                "w=rem;f=h;wx=1;a=l;");
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            checkBoxesPanel.add(panel);
            othersField.setToolTipText("seperate values with ,");
        }

        checkBoxesPanel.setToolTipText("Please check the relevant boxes");

        checkBoxesPanel.add(Box.createVerticalGlue());

        removeAll();
        setLayout(new BorderLayout());
        this.add(new JScrollPane(
            checkBoxesPanel));
    }

    /* == List Building methods == */

    /**
     * Sets the editable.
     * @param bool the new editable
     */
    public void setEditable(
        final String bool) {
        editable = bool.trim().equals(
            "true");
        rebuildPanel();
    }

    /**
     * Sets the custom list.
     * @param customList the new custom list
     */
    public void setCustomList(
        final String customList) {
        final String[] values = customList.split("\\|");
        for (final String value : values) {
            items.add(new CheckedListItem(
                value));
        }
        rebuildPanel();
    }

    /**
     * Sets the code list.
     * @param codeList the new code list
     */
    public void setCodeList(
        final Codelist codeList) {

        if (codeList != null) {

            for (final CodeItem c : codeList.getItems()) {
                items.add(new CheckedListItem(
                    c.getValue().trim()
                        + " ("
                        + c.getLabel().trim()
                        + ")",
                    c.getValue()));
            }

            rebuildPanel();
        } else {
            Reporting.reportUnexpected("null codelist parameter");
        }
    }

    /* == GuiObject items overrides == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        /* just a wrapper for setValues */
        if ((value != null)
            && (value.trim().length() > 0)) {
            final String[] values = value.split(",");
            setValues(values);
        } else {
            setValues(new String[0]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        /* just a wrapper for getValues */
        final String[] values = getValues();
        final StringBuilder sb = new StringBuilder();
        for (final String string : values) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(string);
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValues(
        final String[] values) {
        super.disableNotifications();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).extraAdded) {
                items.remove(i--);
            }
        }
        for (final CheckedListItem item : items) {
            item.setChecked(false);
            for (int i = 0; (i < values.length)
                && !item.isChecked(); i++) {
                if ((values[i] != null)
                    && values[i].trim().equals(
                        item.value)) {
                    item.setChecked(true);
                    break;
                }
            }
        }
        if (editable) {
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                boolean set = false;
                for (final CheckedListItem item : items) {
                    if ((values[i] != null)
                        && values[i].trim().equals(
                            item.value)) {
                        set = true;
                    }
                }
                if (!set) {
                    sb.append(values[i].trim());
                    sb.append(',');
                }
            }
            String extra = sb.toString();
            if (extra.length() > 0) {
                extra = extra.substring(
                    0,
                    extra.length() - 1);
            }
            othersField.setText(extra);
        } else {
            for (int i = 0; i < values.length; i++) {
                boolean set = false;
                for (final CheckedListItem item : items) {
                    if ((values[i] != null)
                        && values[i].trim().equals(
                            item.value)) {
                        set = true;
                    }
                }
                if (!set) {
                    final CheckedListItem ci = new CheckedListItem(
                        values[i]);
                    ci.setChecked(true);
                    ci.extraAdded = true;
                    items.add(ci);
                }
            }
            othersField.setText("");
        }
        rebuildPanel();
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getValues() {
        final ArrayList<String> values = new ArrayList<String>();
        for (final CheckedListItem item : items) {
            if (item.isChecked()) {
                values.add(item.value);
            }
        }
        if (editable) {
            final String text = othersField.getText().trim();
            final String[] texts = text.split(",");
            for (int i = 0; i < texts.length; i++) {
                String txt = texts[i];
                if (txt != null
                    && txt.trim().length() != 0) {
                    txt = txt.trim();
                    values.add(txt);
                }
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

    /**
     * A CheckedList item that listens for selection events.
     */
    private class CheckedListItem
        implements
        ActionListener {

        /**
         * Instantiates a new checked list item.
         * @param aValue the value
         */
        public CheckedListItem(
            final String aValue) {
            this.value = aValue;
            label = aValue;
        }

        /**
         * Creates the checkbox.
         */
        public void createCheckbox() {
            checkBox = new JCheckBox(
                label);
            checkBox.setSelected(checked);
            checkBox.addActionListener(this);
        }

        /**
         * Instantiates a new checked list item.
         * @param aLabel the label
         * @param aValue the value
         */
        public CheckedListItem(
            final String aLabel,
            final String aValue) {
            this.label = aLabel;
            this.value = aValue;
        }

        /** The label. */
        private final String label;

        /** The value. */
        private final String value;

        /** The checked. */
        private boolean checked = false;

        /** The check box. */
        private JCheckBox checkBox = null;

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(
            final ActionEvent arg0) {
            checked = checkBox.isSelected();
            notifyObserversIfChanged();
        }

        /**
         * Checks if is checked.
         * @return true, if is checked
         */
        public boolean isChecked() {
            return checked;
        }

        /**
         * Sets the checked.
         * @param aChecked the new checked
         */
        public void setChecked(
            final boolean aChecked) {
            this.checked = aChecked;
            if (checkBox != null) {
                checkBox.setSelected(aChecked);
            }
        }

        /** The extra added. */
        private boolean extraAdded = false;
    }

}
