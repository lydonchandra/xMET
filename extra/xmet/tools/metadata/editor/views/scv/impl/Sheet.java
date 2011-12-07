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
package xmet.tools.metadata.editor.views.scv.impl;

import java.util.Observable;

import n.io.CS;
import n.io.CSC;
import n.io.CSL;
import n.utils.TreeChildrenArrayList;
import xmet.tools.metadata.editor.views.scv.utils.InitializationContext;
import xmet.tools.metadata.editor.views.scv.utils.SheetIC;
import xmet.tools.metadata.editor.views.scv.view.DisplayContext;
import xmet.tools.metadata.editor.views.scv.view.SheetDC;

/**
 * Represents a editor sheet. Root of all the editor sheet tree.
 * @author Nahid Akbar
 */
@CSC("gui")
public class Sheet
    extends Observable
    implements
    ParentItem,
    ModelItem,
    CodeParent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Properties == */
    /** Title of the sheet (printed). */
    @CS
    private String title;

    /**
     * Sub items of the sheet. a list of pages
     */
    @CSL(listMode = CSL.LIST_LINIENT_MODE,
        listClass = TreeChildrenArrayList.class)
    @CSC
    private TreeChildrenArrayList<Page> items =
        new TreeChildrenArrayList<Page>();

    /** The validation xsl. */
    @CS
    private String validationXSL;
    /* == Runtime Helper Stuff == */

    /**
     * Initialization context - filling signifies that the sheet is ready to be
     * used.
     */
    private transient SheetIC ic;

    /**
     * Display context - signifies that the sheet is ready to be displayed.
     * Note: Fill initializationcontext first
     */
    private transient SheetDC dc;

    /* == GroupOrPageOrSheet Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeChildrenArrayList<Page> getChildren() {
        return getItems();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayContext getDC() {
        return dc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InitializationContext getIC() {
        return ic;
    }

    /* == Observable methods == */
    /* this is for notifying the SemiCustomView Editor view that our display */
    /* panel changed or something */
    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObservers(
        final Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "Sheet[%1$s]",
            getTitle());
    }

    /* == Code parent implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXpath() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMandatory(
        final boolean mandatory) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(
        final boolean visible) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidationError(
        final String validationError) {
        getDc().setValidationErrorMessage(
            validationError);
    }

    /**
     * Gets the title of the sheet (printed).
     * @return the title of the sheet (printed)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the sheet (printed).
     * @param aTitle the new title of the sheet (printed)
     */
    public void setTitle(
        final String aTitle) {
        title = aTitle;
    }

    /**
     * Gets the sub items of the sheet.
     * @return the sub items of the sheet
     */
    public TreeChildrenArrayList<Page> getItems() {
        return items;
    }

    /**
     * Sets the sub items of the sheet.
     * @param aItems the new sub items of the sheet
     */
    public void setItems(
        final TreeChildrenArrayList<Page> aItems) {
        items = aItems;
    }

    /**
     * Gets the validation xsl.
     * @return the validation xsl
     */
    public String getValidationXSL() {
        return validationXSL;
    }

    /**
     * Sets the validation xsl.
     * @param aValidationXSL the new validation xsl
     */
    public void setValidationXSL(
        final String aValidationXSL) {
        validationXSL = aValidationXSL;
    }

    /**
     * Gets the initialization context - filling signifies that the sheet is
     * ready to be used.
     * @return the initialization context - filling signifies that the sheet is
     *         ready to be used
     */
    public SheetIC getIc() {
        return ic;
    }

    /**
     * Sets the initialization context - filling signifies that the sheet is
     * ready to be used.
     * @param aIc the new initialization context - filling signifies that the
     *            sheet is ready to be used
     */
    public void setIc(
        final SheetIC aIc) {
        ic = aIc;
    }

    /**
     * Gets the display context - signifies that the sheet is ready to be
     * displayed.
     * @return the display context - signifies that the sheet is ready to be
     *         displayed
     */
    public SheetDC getDc() {
        return dc;
    }

    /**
     * Sets the display context - signifies that the sheet is ready to be
     * displayed.
     * @param aDc the new display context - signifies that the sheet is ready to
     *            be displayed
     */
    public void setDc(
        final SheetDC aDc) {
        dc = aDc;
    }

}
