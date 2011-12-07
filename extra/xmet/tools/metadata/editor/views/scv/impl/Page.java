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

import n.io.CS;
import n.io.CSC;
import n.io.CSL;
import n.utils.TreeChildrenArrayList;
import xmet.tools.metadata.editor.views.scv.utils.InitializationContext;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;
import xmet.tools.metadata.editor.views.scv.view.DisplayContext;

/**
 * Represents a page.
 * @author Nahid Akbar
 */
@CSC("page")
@SuppressWarnings("rawtypes")
public class Page
    implements
    PageSubitem,
    ParentItem,
    HideableNamedItem,
    CodeParent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * The Constant DEFAULT_PAGE_ORDER.
     */
    public static final int DEFAULT_PAGE_ORDER = 9999999;

    /* == Properties == */
    /** The title (presented). */
    @CS
    private String title;

    /** name of the page (for identification if needed). */
    @CS
    private String name;

    /**
     * The order - after loading, pages are sorted in ascending order based on
     * their order value.
     */
    @CS
    private int order = DEFAULT_PAGE_ORDER;

    /** Child items - groups or items only. */
    @CSL(listMode = CSL.LIST_LINIENT_MODE,
        listClass = TreeChildrenArrayList.class)
    @CSC
    private TreeChildrenArrayList<PageSubitem> items =
        new TreeChildrenArrayList<PageSubitem>();

    /** Whether the page is visible or not. */
    @CS
    private boolean visible = true;

    /** Code that gets executed when a page is initialized. */
    @CSC
    private Code onInitialize;

    /** The on validation. */
    @CSC
    private Code onValidation;

    /* == Runtime Helper stuff == */
    /** initialization context - initialize first before any other use. */
    private transient InitializationContext ic;

    /** display context - initialize before displaying. */
    private transient DisplayContext dc;

    /* == Manipulable Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(
        final boolean aVisible) {
        if (aVisible != this.visible) {
            this.visible = aVisible;
            if (getIc() != null) {
                getIc().visibilityChanged(
                    this);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        /* Nothing needs to be done for a page */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMandatory(
        final boolean mandatory) {
        /* Nothing needs to be done for a page */
    }

    /* == PageSubitem Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeChildrenArrayList getChildren() {
        return getItems();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayContext getDC() {
        return getDc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InitializationContext getIC() {
        return getIc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXpath() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "Page[%1$s]",
            getTitle());
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
     * {@inheritDoc}
     */
    @Override
    public void setName(
        final String aName) {
        this.name = aName;
    }

    /**
     * Copy page.
     * @param src the src
     * @param newPage the new_
     */
    public static void copyPage(
        final Page src,
        final Page newPage) {
        newPage.setTitle(src.getTitle());
        newPage.setOrder(src.getOrder());
        newPage.setVisible(src.isVisible());
        newPage.setName(src.getName());
        if (src.getOnInitialize() != null) {
            newPage
                .setOnInitialize((Code) SCVUtils.clone(src.getOnInitialize()));
        }
        if (src.getOnValidation() != null) {
            newPage
                .setOnValidation((Code) SCVUtils.clone(src.getOnValidation()));
        }
        if (src.getItems() != null) {
            newPage.setItems(new TreeChildrenArrayList<PageSubitem>(
                src.getItems()));
            for (int i = 0; i < newPage.getItems().size(); i++) {
                newPage.getItems().set(
                    i,
                    (PageSubitem) SCVUtils.clone(newPage.getItems().get(
                        i)));
            }
        }
    }

    /**
     * Gets the title (presented).
     * @return the title (presented)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title (presented).
     * @param aTitle the new title (presented)
     */
    public void setTitle(
        final String aTitle) {
        title = aTitle;
    }

    /**
     * Gets the order - after loading, pages are sorted in ascending order based
     * on their order value.
     * @return the order - after loading, pages are sorted in ascending order
     *         based on their order value
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the order - after loading, pages are sorted in ascending order based
     * on their order value.
     * @param aOrder the new order - after loading, pages are sorted in
     *            ascending order based on their order value
     */
    public void setOrder(
        final int aOrder) {
        order = aOrder;
    }

    /**
     * Gets the child items - groups or items only.
     * @return the child items - groups or items only
     */
    public TreeChildrenArrayList<PageSubitem> getItems() {
        return items;
    }

    /**
     * Sets the child items - groups or items only.
     * @param aItems the new child items - groups or items only
     */
    public void setItems(
        final TreeChildrenArrayList<PageSubitem> aItems) {
        items = aItems;
    }

    /**
     * Gets the code that gets executed when a page is initialized.
     * @return the code that gets executed when a page is initialized
     */
    public Code getOnInitialize() {
        return onInitialize;
    }

    /**
     * Sets the code that gets executed when a page is initialized.
     * @param aOnInitialize the new code that gets executed when a page is
     *            initialized
     */
    public void setOnInitialize(
        final Code aOnInitialize) {
        onInitialize = aOnInitialize;
    }

    /**
     * Gets the on validation.
     * @return the on validation
     */
    public Code getOnValidation() {
        return onValidation;
    }

    /**
     * Sets the on validation.
     * @param aOnValidation the new on validation
     */
    public void setOnValidation(
        final Code aOnValidation) {
        onValidation = aOnValidation;
    }

    /**
     * Gets the initialization context - initialize first before any other use.
     * @return the initialization context - initialize first before any other
     *         use
     */
    public InitializationContext getIc() {
        return ic;
    }

    /**
     * Sets the initialization context - initialize first before any other use.
     * @param aIc the new initialization context - initialize first before any
     *            other use
     */
    public void setIc(
        final InitializationContext aIc) {
        ic = aIc;
    }

    /**
     * Gets the display context - initialize before displaying.
     * @return the display context - initialize before displaying
     */
    public DisplayContext getDc() {
        return dc;
    }

    /**
     * Sets the display context - initialize before displaying.
     * @param aDc the new display context - initialize before displaying
     */
    public void setDc(
        final DisplayContext aDc) {
        dc = aDc;
    }

    /**
     * Checks if is whether the page is visible or not.
     * @return the whether the page is visible or not
     */
    public boolean isVisible() {
        return visible;
    }
}
