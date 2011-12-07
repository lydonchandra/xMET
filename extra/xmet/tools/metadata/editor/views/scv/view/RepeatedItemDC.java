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
package xmet.tools.metadata.editor.views.scv.view;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import xmet.tools.metadata.editor.views.scv.impl.RepeatedItem;

/**
 * Abstract DC for Repeated Items.
 * @param <E> the element type
 * @author Nahid Akbar
 */
public abstract class RepeatedItemDC<E>
    extends DisplayContext {

    /**
     * The group.
     */
    private final RepeatedItem<E> repeated;

    /* move to display context */

    /**
     * Instantiates a new repeated item dc.
     * @param aRepeated the repeated
     */
    public RepeatedItemDC(
        final RepeatedItem<E> aRepeated) {
        this.repeated = aRepeated;
    }

    /* == DC Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Component getDisplayPanel();

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildDisplayPanel() {
        new PanelBuilderUtil(
            getRepeated());
    }

    /* == Misc buttons/operations == */

    /**
     * Adds the item.
     * @return true, if successful
     */
    public boolean addItem() {
        if (getRepeated().getRIC().addNewRepeatedItem()) {
            rebuildDisplayPanel();
            return true;
        }
        return false;
    }

    /**
     * Removes the item.
     * @param item the item
     */
    public void removeItem(
        final int item) {
        if ((item >= 0)
            && (item < getRepeated().getRIC().getRepeatedItemsCount())) {
            getRepeated().getRIC().removeRepeatedItem(
                item);
            rebuildDisplayPanel();
        }
    }

    /* == Subpanels == */

    /** The sub panels. */
    private final ArrayList<JPanel> subPanels = new ArrayList<JPanel>();

    /**
     * Clear panels.
     */
    public void clearPanels() {
        getSubPanels().clear();
    }

    /**
     * Adds the sub panel.
     * @param pop the pop
     * @param index the index
     */
    public void addSubPanel(
        final JPanel pop,
        final int index) {
        getSubPanels().add(
            pop);
    }

    /**
     * Gets the sub panel.
     * @param index the index
     * @return the sub panel
     */
    public JPanel getSubPanel(
        final int index) {
        return getSubPanels().get(
            index);
    }

    /**
     * called after sub panels are rebuilt.
     */
    public abstract void rebuildSubPanelWrapper();

    /* == Icons == */
    /** The add icon. */
    private static ImageIcon addIcon = null;

    /** The remove icon. */
    private static ImageIcon removeIcon = null;

    /** The next icon. */
    private static ImageIcon nextIcon = null;

    /** The prev icon. */
    private static ImageIcon prevIcon = null;

    /**
     * Gets the adds the icon.
     * @return the adds the icon
     */
    protected ImageIcon getAddIcon() {
        loadIcons();
        return addIcon;
    }

    /**
     * Gets the removes the icon.
     * @return the removes the icon
     */
    protected ImageIcon getRemoveIcon() {
        loadIcons();
        return removeIcon;
    }

    /**
     * Gets the next icon.
     * @return the next icon
     */
    protected ImageIcon getNextIcon() {
        loadIcons();
        return nextIcon;
    }

    /**
     * Gets the prev icon.
     * @return the prev icon
     */
    protected ImageIcon getPrevIcon() {
        loadIcons();
        return prevIcon;
    }

    /**
     * Load icons.
     */
    private void loadIcons() {
        if (addIcon == null) {
            addIcon =
                getRepeated()
                    .getRIC()
                    .getContext()
                    .getResources()
                    .getImageIconResource(
                        "images/scv.addRepeated.png");
            removeIcon =
                getRepeated()
                    .getRIC()
                    .getContext()
                    .getResources()
                    .getImageIconResource(
                        "images/scv.removeRepeated.png");

            prevIcon =
                getRepeated()
                    .getRIC()
                    .getContext()
                    .getResources()
                    .getImageIconResource(
                        "images/scv.prevRepeated.png");

            nextIcon =
                getRepeated()
                    .getRIC()
                    .getContext()
                    .getResources()
                    .getImageIconResource(
                        "images/scv.nextRepeated.png");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshPanel() {
        /* TODO Auto-generated method stub */

    }

    /**
     * Gets the group.
     * @return the group
     */
    public RepeatedItem<E> getRepeated() {
        return repeated;
    }

    /**
     * Gets the sub panels.
     * @return the sub panels
     */
    public ArrayList<JPanel> getSubPanels() {
        return subPanels;
    }
}
