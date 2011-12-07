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
package xmet.tools.metadata.editor.views.scv.model;

import n.io.CS;
import n.io.CSC;
import n.io.CSL;
import n.utils.TreeChildrenArrayList;

/**
 * Represents a page.
 * @author Nahid Akbar
 */
@CSC("page")
public class Page
    implements
    PageSubitem,
    ParentItem<PageSubitem>,
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

    /**
     * Gets the serialversionuid.
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * Gets the default page order.
     * @return the default page order
     */
    public static int getDefaultPageOrder() {
        return DEFAULT_PAGE_ORDER;
    }

    /**
     * Gets the title (presented).
     * @return the title (presented)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the name of the page (for identification if needed).
     * @return the name of the page (for identification if needed)
     */
    public String getName() {
        return name;
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
     * {@inheritDoc}
     */
    @Override
    public TreeChildrenArrayList<PageSubitem> getItems() {
        return items;
    }

    /**
     * Checks if is whether the page is visible or not.
     * @return the whether the page is visible or not
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Gets the code that gets executed when a page is initialized.
     * @return the code that gets executed when a page is initialized
     */
    public Code getOnInitialize() {
        return onInitialize;
    }

    /**
     * Gets the on validation.
     * @return the on validation
     */
    public Code getOnValidation() {
        return onValidation;
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
     * Sets the name of the page (for identification if needed).
     * @param aName the new name of the page (for identification if needed)
     */
    public void setName(
        final String aName) {
        name = aName;
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
     * Sets the child items - groups or items only.
     * @param aItems the new child items - groups or items only
     */
    public void setItems(
        final TreeChildrenArrayList<PageSubitem> aItems) {
        items = aItems;
    }

    /**
     * Sets the whether the page is visible or not.
     * @param aVisible the new whether the page is visible or not
     */
    public void setVisible(
        final boolean aVisible) {
        visible = aVisible;
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
     * Sets the on validation.
     * @param aOnValidation the new on validation
     */
    public void setOnValidation(
        final Code aOnValidation) {
        onValidation = aOnValidation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        visitor.preVisitPage(this);
        if (this.getOnInitialize() != null) {
            visitor.preVisitCode(
                this,
                "onInitialize");
            getOnInitialize().accept(
                visitor);
            visitor.postVisitCode(
                this,
                "onInitialize");
        }
        if (this.getOnValidation() != null) {
            visitor.preVisitCode(
                this,
                "onValidation");
            getOnValidation().accept(
                visitor);
            visitor.postVisitCode(
                this,
                "onValidation");
        }
        for (final PageSubitem iog : this.getItems()) {
            if (iog != null) {
                iog.accept(visitor);
            }
        }
        visitor.postVisitPage(this);
    }
}
