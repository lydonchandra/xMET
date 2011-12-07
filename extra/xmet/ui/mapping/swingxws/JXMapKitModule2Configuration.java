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
package xmet.ui.mapping.swingxws;

import n.io.CS;

/**
 * Configuration object for JXMapKit module.
 * @author Nahid Akbar
 */
public class JXMapKitModule2Configuration {

    /** The base url. */
    @CS
    private String baseURL;

    /** The layer. */
    @CS
    private String layer;

    /** The type. */
    @CS
    private String type;

    /**
     * Instantiates a new swing xws cnmfiguration.
     * @param aBaseURL the base url
     * @param aLayer the layer
     * @param aType the type
     */
    public JXMapKitModule2Configuration(
        final String aBaseURL,
        final String aLayer,
        final String aType) {
        super();
        this.setBaseURL(aBaseURL);
        this.setLayer(aLayer);
        this.setType(aType);
    }

    /**
     * Instantiates a new jX map kit module2 configuration.
     */
    public JXMapKitModule2Configuration() {

    }

    /**
     * Gets the base url.
     * @return the base url
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * Sets the base url.
     * @param aBaseURL the new base url
     */
    public void setBaseURL(
        final String aBaseURL) {
        baseURL = aBaseURL;
    }

    /**
     * Gets the layer.
     * @return the layer
     */
    public String getLayer() {
        return layer;
    }

    /**
     * Sets the layer.
     * @param aLayer the new layer
     */
    public void setLayer(
        final String aLayer) {
        layer = aLayer;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * @param aType the new type
     */
    public void setType(
        final String aType) {
        type = aType;
    }

}
