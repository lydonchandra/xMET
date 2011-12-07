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
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListDataListener;

import n.io.CSC;
import n.reporting.Reporting;
import n.ui.SwingUtils;
import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;

/**
 * Implementation of the DoubleList UI Design pattern.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
@CSC("MoveOverList")
public class MoveOverList
    extends GUIObject
    implements
    ActionListener,
    CompositeGUIObject<String> {

    /** The list items. */
    private List<MoveOverListItem> listItems;

    /** The selected list. */
    private final List<MoveOverListItem> selectedList;

    /** The unselected list. */
    private final List<MoveOverListItem> unselectedList;

    /** The selected list control. */
    private JList selectedListControl;

    /** The unselected list control. */
    private JList unselectedListControl;

    /** The add button. */
    private JButton addButton;

    /** The remove button. */
    private JButton removeButton;

    /**
     * Instantiates a new checked list.
     */
    public MoveOverList() {
        super();
        listItems = new ArrayList<MoveOverListItem>();
        selectedList = new ArrayList<MoveOverListItem>();
        unselectedList = new ArrayList<MoveOverListItem>();
        rebuildPanel();
    }

    /**
     * Gets the check boxes panel.
     */
    private void rebuildPanel() {
        // CHECKSTYLE OFF: MagicNumber

        final JPanel panel = SwingUtils.GridBag.getNew();

        removeAll();

        setLayout(new BorderLayout());
        selectedListControl = new JList(
            getSelectedListModel());
        final JScrollPane component = new JScrollPane(
            selectedListControl,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        component.setBorder(BorderFactory.createLineBorder(
            Color.BLUE,
            2));
        selectedListControl.setToolTipText("List of selected items");
        SwingUtils.GridBag.add(
            panel,
            component,
            "x=2;y=0;h=4;f=b;wx=1;wy=1;");
        unselectedListControl = new JList(
            getUnselectedListModel());
        final Component component1 = new JScrollPane(
            unselectedListControl,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        unselectedListControl.setToolTipText("List of available items");
        SwingUtils.GridBag.add(
            panel,
            component1,
            "x=0;y=0;h=4;f=b;wx=1;wy=1;");
        selectedListControl.setFixedCellWidth(50);
        unselectedListControl.setFixedCellWidth(50);

        SwingUtils.GridBag.add(
            panel,
            Box.createGlue(),
            "x=1;y=0;wx=0;wy=1;f=b;a=c;");
        addButton = new JButton(
            "Add");
        SwingUtils.GridBag.add(
            panel,
            addButton,
            "x=1;y=1;wx=0;f=h;a=c;");
        removeButton = new JButton(
            "Remove");
        SwingUtils.GridBag.add(
            panel,
            removeButton,
            "x=1;y=2;wx=0;f=h;a=c;");
        SwingUtils.GridBag.add(
            panel,
            Box.createGlue(),
            "x=1;y=3;wx=0;wy=1;f=b;a=c;");

        addButton.addActionListener(this);
        removeButton.addActionListener(this);

        add(panel);
        panel.revalidate();
        revalidate();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Gets the unselected list model.
     * @return the unselected list model
     */
    private ListModel getUnselectedListModel() {
        return new ListModel() {

            @Override
            public void removeListDataListener(
                final ListDataListener arg0) {

            }

            @Override
            public int getSize() {
                return unselectedList.size();
            }

            @Override
            public Object getElementAt(
                final int arg0) {
                if (unselectedList.size() > 0) {
                    return unselectedList.get(
                        arg0).getLabel();
                } else {
                    return null;
                }
            }

            @Override
            public void addListDataListener(
                final ListDataListener arg0) {

            }
        };
    }

    /**
     * Gets the selected list model.
     * @return the selected list model
     */
    private ListModel getSelectedListModel() {
        return new ListModel() {

            @Override
            public void removeListDataListener(
                final ListDataListener l) {

            }

            @Override
            public int getSize() {
                return selectedList.size();
            }

            @Override
            public Object getElementAt(
                final int index) {
                if (selectedList.size() > 0) {
                    return selectedList.get(
                        index).getLabel();
                } else {
                    return 0;
                }
            }

            @Override
            public void addListDataListener(
                final ListDataListener l) {

            }
        };
    }

    /* == List Building methods == */
    /**
     * Sets the custom list.
     * @param customList the new custom list
     */
    public void setCustomList(
        final String customList) {
        final String[] values = customList.split("\\|");

        listItems.clear();
        for (int i = 0; i < values.length; i++) {
            listItems.add(new MoveOverListItem(
                values[i]));
        }
        selectedList.clear();
        unselectedList.clear();
        unselectedList.addAll(listItems);
        rebuildPanel();
    }

    /**
     * Sets the code list.
     * @param codeList the new code list
     */
    public void setCodeList(
        final Codelist codeList) {
        super.disableNotifications();
        if (codeList != null) {
            final List<MoveOverListItem> itemsList =
                new ArrayList<MoveOverListItem>();
            for (final CodeItem item : codeList.getItems()) {
                itemsList.add(new MoveOverListItem(
                    item));
            }
            listItems = itemsList;
            unselectedList.clear();
            unselectedList.addAll(itemsList);
            for (int i = 0; i < unselectedList.size(); i++) {
                final MoveOverListItem uci = unselectedList.get(i);
                for (final MoveOverListItem sci : selectedList) {
                    if (uci.getValue().equals(
                        sci.getValue())) {
                        unselectedList.remove(i--);
                    }
                }
            }
            rebuildPanel();
        } else {
            Reporting.reportUnexpected("null codelist parameter");
        }
        super.setValue(getValue());
        super.enableNotifications();
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
        final StringBuilder sb = new StringBuilder();
        final String[] values = getValues();
        for (final String value : values) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(value);
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
        selectedList.clear();
        unselectedList.clear();
        /* set existing list */
        for (int j = 0; j < listItems.size(); j++) {
            boolean set = false;
            for (int i = 0; i < values.length; i++) {
                if ((values[i] != null)
                    && values[i].trim().equals(
                        listItems.get(
                            j).getValue())) {
                    set = true;
                    break;
                }
            }
            if (set) {
                selectedList.add(listItems.get(j));
            } else {
                unselectedList.add(listItems.get(j));
            }
        }
        /* non-existing items */
        for (int i = 0; i < values.length; i++) {
            boolean set = false;
            for (int j = 0; j < listItems.size(); j++) {
                if ((values[i] != null)
                    && values[i].trim().equals(
                        listItems.get(
                            j).getValue())) {
                    set = true;
                    break;
                }
            }
            if (!set) {
                selectedList.add(new MoveOverListItem(
                    values[i]));
            }
        }
        selectedListControl.setModel(getSelectedListModel());
        unselectedListControl.setModel(getUnselectedListModel());
        super.setValue(getValue());
        super.enableNotifications();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getValues() {
        final ArrayList<String> values = new ArrayList<String>();
        for (int i = 0; i < selectedList.size(); i++) {
            values.add(selectedList.get(
                i).getValue());
        }
        return values.toArray(new String[values.size()]);
    }

    /* == ActionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        if (arg0.getSource() == addButton) {
            final int[] indecies = unselectedListControl.getSelectedIndices();
            if (indecies.length > 0) {
                for (int i = indecies.length - 1; i >= 0; i--) {
                    selectedList.add(unselectedList.get(indecies[i]));
                    unselectedList.remove(indecies[i]);
                }
                unselectedListControl.setModel(getUnselectedListModel());
                selectedListControl.setModel(getSelectedListModel());
                notifyObserversIfChanged();
            }
        } else if (arg0.getSource() == removeButton) {
            final int[] indecies = selectedListControl.getSelectedIndices();
            if (indecies.length > 0) {
                for (int i = indecies.length - 1; i >= 0; i--) {
                    unselectedList.add(selectedList.get(indecies[i]));
                    selectedList.remove(indecies[i]);
                }
                unselectedListControl.setModel(getUnselectedListModel());
                selectedListControl.setModel(getSelectedListModel());
                notifyObserversIfChanged();
            }
        }
    }

    /**
     * A list item.
     */
    public static class MoveOverListItem {

        /** The label. */
        private final String label;

        /** The value. */
        private final String value;

        /**
         * Instantiates a new move over list item.
         * @param aLabel the label
         * @param aValue the value
         */
        public MoveOverListItem(
            final String aLabel,
            final String aValue) {
            super();
            this.label = String.format(
                "%1$s (%2$s)",
                aValue,
                aLabel);
            this.value = aValue;
        }

        /**
         * Gets the value.
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * Gets the label.
         * @return the label
         */
        public String getLabel() {
            return label;
        }

        /**
         * Instantiates a new move over list item.
         * @param aValue the value
         */
        public MoveOverListItem(
            final String aValue) {
            this.value = aValue;
            label = aValue;
        }

        /**
         * Instantiates a new move over list item.
         * @param item the item
         */
        public MoveOverListItem(
            final CodeItem item) {
            label = String.format(
                "%1$s (%2$s)",
                item.getValue(),
                item.getLabel());
            value = item.getValue();
        }

    }

}
