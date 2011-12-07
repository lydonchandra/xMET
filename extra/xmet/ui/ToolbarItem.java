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
package xmet.ui;

import n.ui.patterns.callback.Callback;

/**
 * An item in a toolbar.
 */
public class ToolbarItem {

    /** The button label. */
    private final String buttonLabel;

    /** The button icon path. */
    private final String buttonIconPath;

    /** The callback. */
    private final Callback callback;

    /**
     * Instantiates a new toolbar item.
     * @param aButtonLabel the button label
     * @param aButtonIconPath the button icon path
     * @param aCallback the callback
     */
    public ToolbarItem(
        final String aButtonLabel,
        final String aButtonIconPath,
        final Callback aCallback) {
        super();
        this.buttonLabel = aButtonLabel;
        this.buttonIconPath = aButtonIconPath;
        this.callback = aCallback;
    }

    /**
     * Gets the button label.
     * @return the button label
     */
    public String getButtonLabel() {
        return buttonLabel;
    }

    /**
     * Gets the button icon path.
     * @return the button icon path
     */
    public String getButtonIconPath() {
        return buttonIconPath;
    }

    /**
     * Gets the callback.
     * @return the callback
     */
    public Callback getCallback() {
        return callback;
    }
}
