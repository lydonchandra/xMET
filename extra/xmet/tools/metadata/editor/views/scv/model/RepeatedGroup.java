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

import n.io.CS;
import n.io.CSC;

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

    /**
     * Gets the label.
     * @return the label
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     * @param aLabel the new label
     */
    public void setLabel(
        final String aLabel) {
        this.label = aLabel;
    }

    /**
     * Gets the base.
     * @return the base
     */
    @Override
    public String getBase() {
        return base;
    }

    /**
     * Sets the base.
     * @param aBase the new base
     */
    public void setBase(
        final String aBase) {
        this.base = aBase;
    }

    /**
     * Checks if is compact.
     * @return true, if is compact
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
        this.compact = aCompact;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        visitor.preVisitRepeatedGroup(this);
        for (final GroupSubitem i : this.getItems()) {
            if (i != null) {
                i.accept(visitor);
            }
        }
        visitor.postVisitRepeatedGroup(this);
    }
}
