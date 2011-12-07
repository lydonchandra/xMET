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
package xmet.ui;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import n.ui.SwingUtils;
import xmet.ClientContext;

/**
 * Helper Class for building toolbars fast.
 * @author Nahid Akbar
 */
public class ToolbarBuilder {

    /** The context. */
    private ClientContext context;

    /** The name. */
    private String name;

    /** The floatable. */
    private boolean floatable;

    /** The vertical. */
    private boolean vertical;

    /** The items. */
    private ArrayList<ToolbarItem> items = new ArrayList<ToolbarItem>();

    /**
     * Instantiates a new toolbar builder.
     * @param aContext the context
     * @param aName the name
     * @param aFloatable the floatable
     * @param aVertical the vertical
     */
    public ToolbarBuilder(
        final ClientContext aContext,
        final String aName,
        final boolean aFloatable,
        final boolean aVertical) {
        super();
        context = aContext;
        name = aName;
        floatable = aFloatable;
        vertical = aVertical;
    }

    /**
     * Builds the toolbar.
     * @return the j tool bar
     */
    public JToolBar buildToolbar() {
        final boolean showIcons = context.getConfig().isShowToolbarIcons();
        final boolean showLabels = context.getConfig().isShowToolbarLabels();
        final int buttonsWidth = context.getConfig().getToolbarIconWidth();
        return buildCustomToolbar(
            buttonsWidth,
            showLabels,
            showIcons);
    }

    /**
     * Builds the custom toolbar. Doesn' take settings form config but directly
     * through paramaters
     * @param toolbarIconWidth the toolbar icon width
     * @param showToolbarLabels the show toolbar labels
     * @param showToolbarIcons the show toolbar icons
     * @return the j tool bar
     */
    public JToolBar buildCustomToolbar(
        final int toolbarIconWidth,
        final boolean showToolbarLabels,
        final boolean showToolbarIcons) {
        final boolean showIcons = showToolbarIcons;
        final boolean showLabels = showToolbarLabels;
        final int buttonsWidth = toolbarIconWidth;
        int varI;
        if (vertical) {
            varI = SwingConstants.VERTICAL;
        } else {
            varI = SwingConstants.HORIZONTAL;
        }
        final JToolBar toolBar = new JToolBar(
            name,
            varI);
        toolBar.setFloatable(floatable);
        for (final ToolbarItem toolbarItem : items) {
            if (toolbarItem == null) {
                toolBar.addSeparator();
            } else {
                JButton button;
                if (showLabels
                    && showIcons
                    || !(showLabels || showIcons)) {
                    button = SwingUtils.BUTTON.getNewV(
                        toolbarItem.getButtonLabel(),
                        context.getResources().getImageIconResourceResize(
                            toolbarItem.getButtonIconPath(),
                            buttonsWidth,
                            buttonsWidth),
                        toolbarItem.getCallback());
                } else if (showLabels) {
                    button = SwingUtils.BUTTON.getNew(
                        toolbarItem.getButtonLabel(),
                        toolbarItem.getCallback());
                } else { /* if (showIcons) { */
                    button = SwingUtils.BUTTON.getNew(
                        context.getResources().getImageIconResourceResize(
                            toolbarItem.getButtonIconPath(),
                            buttonsWidth,
                            buttonsWidth),
                        toolbarItem.getCallback());
                }
                button.setToolTipText(toolbarItem.getButtonLabel());
                toolBar.add(button);
            }
        }
        return toolBar;
    }

    /**
     * Gets the context.
     * @return the context
     */
    public ClientContext getContext() {
        return context;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the floatable.
     * @return the floatable
     */
    public boolean getFloatable() {
        return floatable;
    }

    /**
     * Checks if is the vertical.
     * @return the vertical
     */
    public boolean isVertical() {
        return vertical;
    }

    /**
     * Gets the items.
     * @return the items
     */
    public ArrayList<ToolbarItem> getItems() {
        return items;
    }

    /**
     * Sets the context.
     * @param aContext the new context
     */
    public void setContext(
        final ClientContext aContext) {
        context = aContext;
    }

    /**
     * Sets the name.
     * @param aName the new name
     */
    public void setName(
        final String aName) {
        name = aName;
    }

    /**
     * Sets the floatable.
     * @param aFloatable the new floatable
     */
    public void setFloatable(
        final boolean aFloatable) {
        floatable = aFloatable;
    }

    /**
     * Sets the vertical.
     * @param aVertical the new vertical
     */
    public void setVertical(
        final boolean aVertical) {
        vertical = aVertical;
    }

    /**
     * Sets the items.
     * @param aItems the new items
     */
    public void setItems(
        final ArrayList<ToolbarItem> aItems) {
        items = aItems;
    }
}
