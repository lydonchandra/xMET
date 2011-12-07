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
import n.ui.patterns.propertySheet.PSELabel;
import n.ui.patterns.propertySheet.UseCustomPSE;
import n.ui.patterns.propertySheet.UseStringPSE;
import xmet.ui.ImageSelectionPSE;

/**
 * A SSI which has a title and a description and an icon. i.e. a button.
 * @author Nahid Akbar
 */
@CSC("named")
public class NamedStartScreenItem
    extends StartScreenItem {

    /** The title. */
    @PSELabel("Label")
    @UseStringPSE
    @CS
    private String title;

    /** The description. */
    @PSELabel("Description")
    @UseStringPSE
    @CS
    private String description;

    /** The icon url. */
    @PSELabel("Icon")
    @UseCustomPSE(ImageSelectionPSE.class)
    @CS
    private String iconURL;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Label: ");
        if (getTitle() == null
            || getTitle().trim().length() == 0) {
            sb.append("Unset");
        } else {
            sb.append(getTitle());
        }
        return sb.toString();
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
     * Gets the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * @param aDescription the new description
     */
    public void setDescription(
        final String aDescription) {
        description = aDescription;
    }

    /**
     * Gets the icon url.
     * @return the icon url
     */
    public String getIconURL() {
        return iconURL;
    }

    /**
     * Sets the icon url.
     * @param aIconURL the new icon url
     */
    public void setIconURL(
        final String aIconURL) {
        iconURL = aIconURL;
    }
}
