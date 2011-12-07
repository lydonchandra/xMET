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
import n.io.CSL;
import n.utils.TreeChildrenArrayList;

/**
 * A group is a group of a bunch of items TODO: Add a display context.
 * @author Nahid Akbar
 */
@CSC("group")
public class Group
    implements
    PageSubitem,
    GroupSubitem,
    ParentItem<GroupSubitem>,
    HideableNamedItem {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The name of the group. */
    @CS
    private String name;

    /**
     * whether the group and its children is visible or not (maintained at
     * runtime).
     */
    @CS
    private boolean visible = true;

    /** child items - list of group subitems. */
    @CSL(listMode = CSL.LIST_LINIENT_MODE,
        listClass = TreeChildrenArrayList.class)
    @CSC
    private TreeChildrenArrayList<GroupSubitem> items =
        new TreeChildrenArrayList<GroupSubitem>();

    /**
     * Gets the serialversionuid.
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * Gets the name of the group.
     * @return the name of the group
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if is whether the group and its children is visible or not
     * (maintained at runtime).
     * @return the whether the group and its children is visible or not
     *         (maintained at runtime)
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeChildrenArrayList<GroupSubitem> getItems() {
        return items;
    }

    /**
     * Sets the name of the group.
     * @param aName the new name of the group
     */
    public void setName(
        final String aName) {
        name = aName;
    }

    /**
     * Sets the whether the group and its children is visible or not (maintained
     * at runtime).
     * @param aVisible the new whether the group and its children is visible or
     *            not (maintained at runtime)
     */
    public void setVisible(
        final boolean aVisible) {
        visible = aVisible;
    }

    /**
     * Sets the child items - list of group subitems.
     * @param aItems the new child items - list of group subitems
     */
    public void setItems(
        final TreeChildrenArrayList<GroupSubitem> aItems) {
        items = aItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        visitor.preVisitGroup(this);
        for (final GroupSubitem i : this.getItems()) {
            if (i != null) {
                i.accept(visitor);
            }
        }
        visitor.postVisitGroup(this);
    }
}
