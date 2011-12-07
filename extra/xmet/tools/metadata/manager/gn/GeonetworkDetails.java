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
package xmet.tools.metadata.manager.gn;

import n.io.CS;
import n.ui.patterns.propertySheet.PSEDescription;
import n.ui.patterns.propertySheet.PSELabel;
import n.ui.patterns.propertySheet.UseMaskedStringPSE;
import n.ui.patterns.propertySheet.UseStringPSE;

/**
 * Contains Geonetwork object configuration details.
 * @see Geonetwork
 * @author Nahid Akbar
 */
@PSELabel("Geonetwork Details")
public class GeonetworkDetails {

    /** The url. */
    @PSELabel("Geonetwork URL:")
    @UseStringPSE
    @PSEDescription("e.g. http://localhost:8080/geonetwork/")
    @CS
    private String url;

    /** The user. */
    @PSELabel("Username:")
    @UseStringPSE
    @PSEDescription("e.g. \"admin\"")
    @CS
    private String user;

    /** The pass. */
    @PSELabel("Password:")
    @UseMaskedStringPSE
    @PSEDescription("e.g. \"admin\"")
    @CS
    private String pass;

    /** The service prefix. */
    private String servicePrefix = "srv/en/";

    /**
     * Instantiates a new geonetwork details.
     * @param aUser the user
     * @param aPass the pass
     * @param aUrl the url
     */
    public GeonetworkDetails(
        final String aUser,
        final String aPass,
        final String aUrl) {
        this.user = aUser;
        this.pass = aPass;
        this.url = aUrl;
    }

    /**
     * Instantiates a new geonetwork details.
     */
    public GeonetworkDetails() {

    }

    /**
     * Gets the user.
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the user.
     * @param aUser the new user
     */
    public void setUser(
        final String aUser) {
        this.user = aUser;
    }

    /**
     * Gets the pass.
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Sets the pass.
     * @param aPass the new pass
     */
    public void setPass(
        final String aPass) {
        this.pass = aPass;
    }

    /**
     * Gets the url.
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     * @param aUrl the new url
     */
    public void setUrl(
        final String aUrl) {
        this.url = aUrl;
    }

    /**
     * Gets the service prefix.
     * @return the service prefix
     */
    public String getServicePrefix() {
        return servicePrefix;
    }

    /**
     * Sets the service prefix.
     * @param aServicePrefix the new service prefix
     */
    public void setServicePrefix(
        final String aServicePrefix) {
        this.servicePrefix = aServicePrefix;
    }
}
