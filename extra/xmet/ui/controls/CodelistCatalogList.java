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
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;

import n.algorithm.ArrayUtil;
import n.io.CSC;
import xmet.profiles.catalogs.model.Codelist;
import xmet.profiles.catalogs.model.CodelistCatalog;

/**
 * This manages a CodeListCatalog.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
@CSC("CheckedList")
public class CodelistCatalogList
    extends GUIObject
    implements
    ActionListener,
    Observer {

    /** The list. */
    private final MoveOverList list = new MoveOverList();
    {
        getList().addObserver(
            this);
    }

    /** The catalog. */
    private CodelistCatalog catalog;

    /** The codelists combo box. */
    private JComboBox codelistsComboBox;

    /**
     * Instantiates a new codelist catalog list.
     */
    public CodelistCatalogList() {
        rebuildPanel();
    }

    /**
     * Gets the check boxes panel.
     */
    private void rebuildPanel() {
        removeAll();
        setLayout(new BorderLayout());
        this.add(getList());
        if (catalog != null) {
            codelistsComboBox = new JComboBox(
                ArrayUtil.arrayFromList(catalog.getCodelists()));
            this.add(
                codelistsComboBox,
                BorderLayout.NORTH);
            codelistsComboBox.addActionListener(this);
            codelistsComboBox.setSelectedIndex(0);
        }
    }

    /**
     * Sets the catalog.
     * @param aCatalog the new catalog
     */
    public void setCatalog(
        final CodelistCatalog aCatalog) {
        this.catalog = aCatalog;
        rebuildPanel();
    }

    /* == GuiObject items overrides == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        getList().setValue(
            value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return getList().getValue();
    }

    /* == ActionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        getList().setCodeList(
            (Codelist) codelistsComboBox.getSelectedItem());
    }

    /* == Observer Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(
        final Observable o,
        final Object arg) {
        notifyObserversIfChanged();
    }

    /**
     * Gets the list.
     * @return the list
     */
    public MoveOverList getList() {
        return list;
    }

}
