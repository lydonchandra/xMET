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
 * A group is a group of a bunch of items. <br>
 * TODO: Add a display context.
 * @author Nahid Akbar
 */
@CSC("group")
public class Group
    implements
    PageSubitem,
    GroupSubitem,
    ParentItem,
    HideableNamedItem {

    /* == Properties == */

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

    /* == Runtime helper stuff == */
    /** The initialization context - initialize first. */
    private transient InitializationContext ic;

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

    /* == PageSubitem Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeChildrenArrayList<GroupSubitem> getChildren() {
        return getItems();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayContext getDC() {
        return null;
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
    public void setName(
        final String aName) {
        this.name = aName;
    }

    //
    // /**
    // * Copy labeled group.
    // *
    // * @param src
    // * the src
    // * @param new_
    // * the new_
    // */
    // public static void copyLabeledGroup(final LabeledGroup src,
    // final LabeledGroup new_) {
    // copyGroup(src, new_);
    // new_.label = src.label;
    // if (src.items != null) {
    // new_.items = new TreeChildrenArrayList<GroupSubitem>(src.items);
    // for (int i = 0; i < new_.items.size(); i++) {
    // new_.items.set(i, (GroupSubitem) clone(new_.items.get(i)));
    // }
    // }
    // }
    //
    // /**
    // * Copy item.
    // *
    // * @param src
    // * the src
    // * @param new_
    // * the new_
    // */
    // public static void copyItem(final Item src, final Item new_) {
    // new_.title = src.title;
    // new_.type = src.type;
    // new_.defaultValue = src.defaultValue;
    // new_.xpath = src.xpath;
    // new_.name = src.name;
    // new_.visible = src.visible;
    // new_.description = src.description;
    // new_.hover = src.hover;
    // new_.validation = src.validation;
    // new_.helpContextID = src.helpContextID;
    // new_.columnSpan = src.columnSpan;
    // if (src.onDataLoad != null) {
    // new_.onDataLoad = (Code) clone(src.onDataLoad);
    // }
    // if (src.onDataChange != null) {
    // new_.onDataChange = (Code) clone(src.onDataChange);
    // }
    // if (src.params != null) {
    // new_.params = new ArrayList<Param>(src.params);
    // }
    // }
    //
    // /**
    // * Copy if code.
    // *
    // * @param src
    // * the src
    // * @param new_
    // * the new_
    // */
    // public static void copyIfCode(final IfCode src, final IfCode new_) {
    // new_.base = src.base;
    // new_.code = src.code;
    // new_.condition = src.condition;
    // new_.expression = src.expression;
    // if (src.code != null) {
    // new_.code = (Code) clone(src.code);
    // }
    // if (src.elseCode != null) {
    // new_.elseCode = (Code) clone(src.elseCode);
    // }
    // }
    //
    // /**
    // * Copy group.
    // *
    // * @param src
    // * the src
    // * @param new_
    // * the new_
    // */
    /**
     * Copy group.
     * @param src the src
     * @param newGroup the new_
     */
    public static void copyGroup(
        final Group src,
        final Group newGroup) {
        newGroup.setName(src.getName());
        newGroup.setVisible(src.isVisible());
        if (src.getItems() != null) {
            newGroup.setItems(new TreeChildrenArrayList<GroupSubitem>(
                src.getItems()));
            for (int i = 0; i < newGroup.getItems().size(); i++) {
                newGroup.getItems().set(
                    i,
                    (GroupSubitem) SCVUtils.clone(newGroup.getItems().get(
                        i)));
            }
        }
    }

    //
    // /**
    // * Copy code block.
    // *
    // * @param src
    // * the src
    // * @param dest
    // * the dest
    // */
    // public static void copyCodeBlock(final CodeBlock src, final CodeBlock
    // dest) {
    // if (src.code != null) {
    // dest.code = new TreeChildrenArrayList<Code>(src.code);
    // for (int i = 0; i < dest.code.size(); i++) {
    // dest.code.set(i, (Code) clone(dest.code.get(i)));
    // }
    // }
    // }

    /**
     * Gets the child items - list of group subitems.
     * @return the child items - list of group subitems
     */
    public TreeChildrenArrayList<GroupSubitem> getItems() {
        return items;
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
     * Gets the initialization context - initialize first.
     * @return the initialization context - initialize first
     */
    public InitializationContext getIc() {
        return ic;
    }

    /**
     * Sets the initialization context - initialize first.
     * @param aIc the new initialization context - initialize first
     */
    public void setIc(
        final InitializationContext aIc) {
        ic = aIc;
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
}
