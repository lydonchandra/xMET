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
package xmet.ui.controls.spatialExtent;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListDataListener;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import xmet.profiles.catalogs.model.CodeItem;
import xmet.profiles.catalogs.model.Codelist;
import xmet.profiles.catalogs.model.CodelistCatalog;
import xmet.profiles.geometry.impl2d.ExtentId2d;

/**
 * For editing Code. Simple double drop-down list interface with a inclusive
 * checkbox
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class CodeTSEETCE
    extends TSEETreeCellEditor
    implements
    FocusListener {

    /** The codelist selection combo box. */
    private JComboBox codelistSelectionComboBox;

    /** The codeitem selection combo box. */
    private JComboBox codeitemSelectionComboBox;

    /** The selected codelist. */
    private Codelist selectedCodelist;

    /** The selected codeitem. */
    private CodeItem selectedCodeitem;

    /** The current code. */
    private ExtentId2d currentCode;

    /** The inclusive check box. */
    private JCheckBox inclusiveCheckBox;
    {
        // CHECKSTYLE OFF: MagicNumber
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "<html><b>Geographic Identifier"),
            "w=rel;f=b;wx=1;wy=0;");
        inclusiveCheckBox = new JCheckBox(
            "inclusive",
            true);
        SwingUtils.GridBag.add(
            this,
            inclusiveCheckBox,
            "w=rem;f=b;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Codelist:"),
            "w=rel;f=b;wx=1;wy=0;");
        codelistSelectionComboBox = new JComboBox(
            getListSelectionModel());
        SwingUtils.GridBag.add(
            this,
            codelistSelectionComboBox,
            "w=rem;f=b;wx=1;wy=0;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Code:"),
            "w=rel;f=b;wx=1;wy=0;");
        codeitemSelectionComboBox = new JComboBox(
            getItemSelectionModel());
        SwingUtils.GridBag.add(
            this,
            codeitemSelectionComboBox,
            "w=rem;f=b;wx=1;wy=0;");
        inclusiveCheckBox.setOpaque(false);
        codeitemSelectionComboBox.setRenderer(new DefaultListCellRenderer() {

            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
                final JList list,
                final Object value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
                if (value instanceof CodeItem) {
                    return super.getListCellRendererComponent(
                        list,
                        ((CodeItem) value).getLabel(),
                        index,
                        isSelected,
                        cellHasFocus);
                } else {
                    return super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus);
                }
            }
        });
        setPreferredSize(new Dimension(
            400,
            70));
        revalidate();
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Instantiates a new code tseetce.
     * @param tsee the tsee
     * @param sec the sec
     */
    CodeTSEETCE(
        final TreeSpatialExtentEditor tsee,
        final SpatialExtentControl sec) {
        super(tsee, sec);
    }

    /* == TSEETreeCellEditor implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void configureFor(
        final Object value) {
        currentCode = (ExtentId2d) value;
        inclusiveCheckBox.setSelected(currentCode.isInclusive());
        selectedCodeitem = null;
        final String cs = currentCode.getCodeSpace();
        if ((cs != null)
            && getCatalog() != null
            && cs.startsWith(getCatalog().getCatalogURI())) {
            if (cs.indexOf('#') != -1) {
                final String part = cs.substring(cs.indexOf('#') + 1);
                selectedCodelist = getCatalog().getCodelistByIdentifier(
                    part);
                if (selectedCodelist != null) {
                    for (final CodeItem i : selectedCodelist.getItems()) {
                        if (i.getValue().equals(
                            currentCode.getCode())) {
                            selectedCodeitem = i;
                            break;
                        }
                    }
                    if (selectedCodeitem == null) {
                        Reporting.logUnexpected();
                    }
                } else {
                    Reporting.logUnexpected();
                }
            } else {
                Reporting.logUnexpected();
            }
        }
        // else {
        // /* Reporting.unimplemented(); */
        // }
        revalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        currentCode.setInclusive(inclusiveCheckBox.isSelected());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem() {
        return currentCode;
    }

    /* == Various List model implementations == */
    /**
     * Gets the list selection model.
     * @return the list selection model
     */
    private ComboBoxModel getListSelectionModel() {
        return new ComboBoxModel() {

            @Override
            public void removeListDataListener(
                final ListDataListener l) {

            }

            @Override
            public int getSize() {
                if (getCatalog() != null) {
                    return getCatalog().getCodelists().size();
                }
                return 0;
            }

            @Override
            public Object getElementAt(
                final int index) {
                if (getCatalog() != null) {
                    return getCatalog().getCodelists().get(
                        index);
                }
                return null;
            }

            @Override
            public void addListDataListener(
                final ListDataListener l) {

            }

            @Override
            public void setSelectedItem(
                final Object anItem) {
                selectedCodelist = (Codelist) anItem;
                codeitemSelectionComboBox.setModel(getItemSelectionModel());
            }

            @Override
            public Object getSelectedItem() {
                return selectedCodelist;
            }
        };
    }

    /**
     * Gets the item selection model.
     * @return the item selection model
     */
    private ComboBoxModel getItemSelectionModel() {
        return new ComboBoxModel() {

            @Override
            public void removeListDataListener(
                final ListDataListener l) {

            }

            @Override
            public int getSize() {
                if (selectedCodelist != null) {
                    return selectedCodelist.getItems().size();
                }
                return 0;
            }

            @Override
            public Object getElementAt(
                final int index) {
                if (selectedCodelist != null) {
                    return selectedCodelist.getItems().get(
                        index);
                }
                return null;
            }

            @Override
            public void addListDataListener(
                final ListDataListener l) {

            }

            @Override
            public void setSelectedItem(
                final Object anItem) {
                selectedCodeitem = (CodeItem) anItem;
                currentCode.setCode(selectedCodeitem.getValue());
                currentCode.setCodeSpace(selectedCodelist.getCodelistURL());
                softChangeNotification();
            }

            @Override
            public Object getSelectedItem() {
                return selectedCodeitem;
            }
        };
    }

    /* == Misc Helper Methods == */

    /**
     * Gets the catalog.
     * @return the catalog
     */
    protected CodelistCatalog getCatalog() {
        return getExtentControl().getCatalog();
    }

    /* == FocusListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void focusGained(
        final FocusEvent e) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focusLost(
        final FocusEvent e) {
        commit();
        softChangeNotification();
    }
}
