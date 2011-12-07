/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br />
 * <br />
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br />
 * <br />
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br />
 * <br />
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br />
 * <br />
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.tools.metadata.editor.views.scv.model;

import java.util.Observable;

import n.io.CS;
import n.io.CSC;
import n.io.CSL;
import n.utils.TreeChildrenArrayList;

/**
 * Represents a editor sheet. Root of all the editor sheet tree.
 * @author Nahid Akbar
 */
@CSC("gui")
public class Sheet
    extends Observable
    implements
    ParentItem<Page>,
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

    /**
     * Gets the title.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     * @param aTitle the new title
     */
    public void setTitle(
        final String aTitle) {
        this.title = aTitle;
    }

    /**
     * Gets the items.
     * @return the items
     */
    @Override
    public TreeChildrenArrayList<Page> getItems() {
        return items;
    }

    /**
     * Sets the items.
     * @param aItems the new items
     */
    public void setItems(
        final TreeChildrenArrayList<Page> aItems) {
        this.items = aItems;
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
        this.validationXSL = aValidationXSL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        final Sheet src = this;
        visitor.preVisitSheet(src);
        for (final PageSubitem igp : src.items) {
            if (igp != null) {
                igp.accept(visitor);
            }
        }
        visitor.postVisitSheet(src);
    }

}
