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
import n.ui.patterns.propertySheet.UseIntegerPSE;
import n.ui.patterns.propertySheet.UseStringPSE;
import xmet.ui.ImageSelectionPSE;

/**
 * A SSI for displaying a logo and link in it to an URL.
 * @author Nahid Akbar
 */
@CSC("logo")
public class LogoStartScreenItem
    extends StartScreenItem {

    /** The logo url. */
    @PSELabel("Logo")
    @UseCustomPSE(ImageSelectionPSE.class)
    @CS
    private String logoUrl;

    /** The link url. */
    @PSELabel("Link")
    @UseStringPSE
    @CS
    private String linkUrl;

    /** The logo width. */
    @PSELabel("Logo Width")
    @UseIntegerPSE
    @CS
    private int logoWidth = -1;

    /** The logo height. */
    @PSELabel("Logo Height")
    @UseIntegerPSE
    @CS
    private int logoHeight = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Logo: ");
        if (getLogoUrl() == null
            || getLogoUrl().trim().length() == 0) {
            sb.append("Unset");
        } else {
            sb.append(getLogoUrl());
        }
        return sb.toString();
    }

    /**
     * Gets the logo url.
     * @return the logo url
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * Sets the logo url.
     * @param aLogoUrl the new logo url
     */
    public void setLogoUrl(
        final String aLogoUrl) {
        logoUrl = aLogoUrl;
    }

    /**
     * Gets the link url.
     * @return the link url
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * Sets the link url.
     * @param aLinkUrl the new link url
     */
    public void setLinkUrl(
        final String aLinkUrl) {
        linkUrl = aLinkUrl;
    }

    /**
     * Gets the logo width.
     * @return the logo width
     */
    public int getLogoWidth() {
        return logoWidth;
    }

    /**
     * Sets the logo width.
     * @param aLogoWidth the new logo width
     */
    public void setLogoWidth(
        final int aLogoWidth) {
        logoWidth = aLogoWidth;
    }

    /**
     * Gets the logo height.
     * @return the logo height
     */
    public int getLogoHeight() {
        return logoHeight;
    }

    /**
     * Sets the logo height.
     * @param aLogoHeight the new logo height
     */
    public void setLogoHeight(
        final int aLogoHeight) {
        logoHeight = aLogoHeight;
    }

}
