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
import xmet.tools.metadata.editor.views.scv.utils.RepeatedIC;
import xmet.tools.metadata.editor.views.scv.view.DisplayContext;
import xmet.tools.metadata.editor.views.scv.view.RepeatedItemDC;

/**
 * repeated version of Page.
 * @author Nahid Akbar
 */
@CSC("repeatedPage")
public class RepeatedPage
    extends Page
    implements
    RepeatedItem<Page> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /** The base xpath. */
    @CS
    private String base;

    // /** The display context. */
    /* public RepeatedItemDC<Page> dc; */

    /* == RepeatedItem<Page> Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBase() {
        return base;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page getItem() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public RepeatedIC<Page> getRIC() {
        return (RepeatedIC<Page>) getIc();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public RepeatedItemDC<Page> getRDC() {
        return (RepeatedItemDC<Page>) getDc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayContext getDC() {
        return getDc();
    }

    /**
     * Sets the base xpath.
     * @param aBase the new base xpath
     */
    public void setBase(
        final String aBase) {
        base = aBase;
    }

}
