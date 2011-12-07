/******************************************************************************
 * xMet - eXtensible Metadata Editor Copyright (C) 2010-2011 - Office Of Spatial
 * Data Management This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version. This software is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. For a copy of the GNU
 * General Public License, see http://www.gnu.org/licenses/
 ******************************************************************************/
package xmet.config;

import java.io.File;

import n.io.CS;
import n.io.CSC;
import n.io.bin.Files;
import n.io.net.ProxyInformation;

/**
 * Main configuration data structure for parsistant configuration items that are
 * stored and loaded. <br />
 * <br />
 * It is using a selective introspection class data encoder and decoder to save
 * and load settings to and from xml. These are the "CS*" annotation tags.
 */
@CSC("Config")
public final class PersistentConfiguration {

    /**
     * The default height and width to resize toolbar icons to.
     */
    private static final int DEFAULT_TOOLBAR_ICON_WIDTH = 48;

    /**
     * The local metadata directory. This setting rightfully belongs to the
     * metadata manager. TODO: Move this configuration item storage to Metadata
     * Manager tool
     */
    @CS
    private String localMetadataDirectory = Files
        .getMyDocumentsPath()
        + File.separator
        + "xMET_Metadata";

    /** Whether to show labels in toolbars or just icons. */
    @CS
    private boolean showToolbarLabels = true;

    /** Whether to show icons in toolbar or just labels. */
    @CS
    private boolean showToolbarIcons = true;

    /** The height and width to resize toolbar icons to. */
    @CS
    private int toolbarIconWidth = DEFAULT_TOOLBAR_ICON_WIDTH;

    /** Proxy information for network access elements. */
    @CSC
    private ProxyInformation proxyInfo = new ProxyInformation();

    /**
     * Gets the local metadata directory.
     * @return the local metadata directory
     */
    public String getLocalMetadataDirectory() {
        return localMetadataDirectory;
    }

    /**
     * Sets the local metadata directory.
     * @param aLocalMetadataDir the new local metadata directory
     */
    public void setLocalMetadataDirectory(
        final String aLocalMetadataDir) {
        localMetadataDirectory = aLocalMetadataDir;
    }

    /**
     * Checks if is show toolbar labels.
     * @return true, if is show toolbar labels
     */
    public boolean isShowToolbarLabels() {
        return showToolbarLabels;
    }

    /**
     * Sets the show toolbar labels.
     * @param aShowToolbarLabels the new show toolbar labels
     */
    public void setShowToolbarLabels(
        final boolean aShowToolbarLabels) {
        showToolbarLabels = aShowToolbarLabels;
    }

    /**
     * Checks if is show toolbar icons.
     * @return true, if is show toolbar icons
     */
    public boolean isShowToolbarIcons() {
        return showToolbarIcons;
    }

    /**
     * Sets the show toolbar icons.
     * @param aShowToolbarIcons the new show toolbar icons
     */
    public void setShowToolbarIcons(
        final boolean aShowToolbarIcons) {
        showToolbarIcons = aShowToolbarIcons;
    }

    /**
     * Gets the toolbar icon width.
     * @return the toolbar icon width
     */
    public int getToolbarIconWidth() {
        return toolbarIconWidth;
    }

    /**
     * Sets the toolbar icon width.
     * @param aToolbarIconWidth the new toolbar icon width
     */
    public void setToolbarIconWidth(
        final int aToolbarIconWidth) {
        toolbarIconWidth = aToolbarIconWidth;
    }

    /**
     * Gets the proxy info.
     * @return the proxy info
     */
    public ProxyInformation getProxyInfo() {
        return proxyInfo;
    }

    /**
     * Sets the proxy info.
     * @param aProxyInfo the new proxy info
     */
    public void setProxyInfo(
        final ProxyInformation aProxyInfo) {
        proxyInfo = aProxyInfo;
    }

}
