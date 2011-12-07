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
package xmet.tools.screen;

import n.io.CS;
import n.io.CSC;
import n.io.CSL;
import n.ui.patterns.propertySheet.PSEDescription;
import n.ui.patterns.propertySheet.PSELabel;
import n.ui.patterns.propertySheet.UseBooleanPSE;
import n.ui.patterns.propertySheet.UseStringPSE;
import n.utils.TreeChildrenArrayList;

/**
 * The root of Start Screen Object Model Hierarchy.
 * @author Nahid Akbar
 */
@CSC("StartScreen")
public class StartScreen {

    /** The items. */
    @CSL(listMode = CSL.LIST_LINIENT_MODE,
        listClass = TreeChildrenArrayList.class)
    @CSC
    private TreeChildrenArrayList<StartScreenItem> items =
        new TreeChildrenArrayList<StartScreenItem>();

    /** The title. */
    @PSELabel("Tab Title")
    @UseStringPSE
    @CS
    private String title;

    /** The screen title. */
    @PSELabel("Screen Title")
    @UseStringPSE
    @CS
    private String screenTitle = null;

    /** The sticky. */
    @PSELabel("Sticky")
    @PSEDescription("Not closable after opening")
    @UseBooleanPSE
    @CS
    private boolean sticky = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (getScreenTitle() == null
            || getScreenTitle().trim().length() == 0) {
            sb.append("Untitled Start Screen");
        } else {
            sb.append("Start Screen");
            sb.append('"');
            sb.append(getScreenTitle());
            sb.append('"');
        }
        return sb.toString();
    }

    /**
     * Gets the items.
     * @return the items
     */
    public TreeChildrenArrayList<StartScreenItem> getItems() {
        return items;
    }

    /**
     * Sets the items.
     * @param aItems the new items
     */
    public void setItems(
        final TreeChildrenArrayList<StartScreenItem> aItems) {
        items = aItems;
    }

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
        title = aTitle;
    }

    /**
     * Gets the screen title.
     * @return the screen title
     */
    public String getScreenTitle() {
        return screenTitle;
    }

    /**
     * Sets the screen title.
     * @param aScreenTitle the new screen title
     */
    public void setScreenTitle(
        final String aScreenTitle) {
        screenTitle = aScreenTitle;
    }

    /**
     * Checks if is the sticky.
     * @return the sticky
     */
    public boolean isSticky() {
        return sticky;
    }

    /**
     * Sets the sticky.
     * @param aSticky the new sticky
     */
    public void setSticky(
        final boolean aSticky) {
        sticky = aSticky;
    }
}
