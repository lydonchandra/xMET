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
 * Repeated set of items related to the sub-elements of a repeated entity This
 * automatically manages the number of children etc.
 * @author Nahid Akbar
 */
@CSC("repeatedGroup")
public class RepeatedGroup
    extends Group
    implements
    RepeatedItem<Group> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* == Properties == */
    /** label of the group. */
    @CS
    private String label;

    /**
     * path to the repeated entity. this path will be substituted for every $
     * sign found
     */
    @CS
    private String base;

    /** The compact. */
    @CS
    private boolean compact = true;

    /* == Runtime Helper Stuff == */

    /** The ic. */
    private transient RepeatedIC<Group> ric;
    /**
     * The display context.
     */
    private transient RepeatedItemDC<Group> rdc;

    /* == RepeatedItem<Group> Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatedItemDC<Group> getRDC() {
        return rdc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayContext getDC() {
        return getRDC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatedIC<Group> getRIC() {
        return ric;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return label;
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
    public Group getItem() {
        return this;
    }

    /**
     * Checks if is the compact.
     * @return the compact
     */
    public boolean isCompact() {
        return compact;
    }

    /**
     * Sets the compact.
     * @param aCompact the new compact
     */
    public void setCompact(
        final boolean aCompact) {
        compact = aCompact;
    }

    /**
     * Sets the ic.
     * @param aRic the new ic
     */
    public void setRIC(
        final RepeatedIC<Group> aRic) {
        ric = aRic;
    }

    /**
     * Sets the display context.
     * @param aRdc the new display context
     */
    public void setRDC(
        final RepeatedItemDC<Group> aRdc) {
        rdc = aRdc;
    }

    /**
     * Sets the label of the group.
     * @param aLabel the new label of the group
     */
    public void setLabel(
        final String aLabel) {
        label = aLabel;
    }

    /**
     * Sets the path to the repeated entity.
     * @param aBase the new path to the repeated entity
     */
    public void setBase(
        final String aBase) {
        base = aBase;
    }

}
