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
import n.ui.patterns.propertySheet.PSEDescription;
import n.ui.patterns.propertySheet.PSELabel;
import n.ui.patterns.propertySheet.UseBooleanPSE;
import n.ui.patterns.propertySheet.UseFloatPSE;
import n.ui.patterns.propertySheet.UseIntegerPSE;

/**
 * An abstract Start Screen Item.
 * @author Nahid Akbar
 */
public class StartScreenItem {

    /** The dev mode only. */
    @UseBooleanPSE
    @PSELabel("Admin Mode:")
    @PSEDescription("If true, the item is only displayed in admin mode")
    @CS
    private boolean devModeOnly = false;

    /** The row. */
    @UseIntegerPSE
    @PSELabel("Row:")
    @PSEDescription("Enter row number of the item")
    @CS
    private int row = -1;

    /** The column. */
    @UseIntegerPSE
    @PSELabel("Column:")
    @PSEDescription("Enter column number of the item")
    @CS
    private int column = -1;

    /** The width. */
    @UseIntegerPSE
    @PSELabel("Width:")
    @PSEDescription("Enter width in cols of the item")
    @CS
    private int width = -1;

    /** The height. */
    @UseIntegerPSE
    @PSELabel("Height:")
    @PSEDescription("Enter height in cols of the item")
    @CS
    private int height = 1;

    /** The scale. */
    @UseFloatPSE
    @PSELabel("Scale:")
    @PSEDescription("Enter horizontal scale of the item - 0 to 1")
    @CS
    private double scale = 1.0f;

    /** The vscale. */
    @UseFloatPSE
    @PSELabel("VerticalScale:")
    @PSEDescription("Enter vertical scale of the item - 0 to 1")
    @CS
    private double vscale = 0f;

    /**
     * Checks if is the dev mode only.
     * @return the dev mode only
     */
    public boolean isDevModeOnly() {
        return devModeOnly;
    }

    /**
     * Sets the dev mode only.
     * @param aDevModeOnly the new dev mode only
     */
    public void setDevModeOnly(
        final boolean aDevModeOnly) {
        devModeOnly = aDevModeOnly;
    }

    /**
     * Gets the row.
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row.
     * @param aRow the new row
     */
    public void setRow(
        final int aRow) {
        row = aRow;
    }

    /**
     * Gets the column.
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column.
     * @param aColumn the new column
     */
    public void setColumn(
        final int aColumn) {
        column = aColumn;
    }

    /**
     * Gets the width.
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width.
     * @param aWidth the new width
     */
    public void setWidth(
        final int aWidth) {
        width = aWidth;
    }

    /**
     * Gets the height.
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height.
     * @param aHeight the new height
     */
    public void setHeight(
        final int aHeight) {
        height = aHeight;
    }

    /**
     * Gets the scale.
     * @return the scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * Sets the scale.
     * @param aScale the new scale
     */
    public void setScale(
        final double aScale) {
        scale = aScale;
    }

    /**
     * Gets the vscale.
     * @return the vscale
     */
    public double getVscale() {
        return vscale;
    }

    /**
     * Sets the vscale.
     * @param aVscale the new vscale
     */
    public void setVscale(
        final double aVscale) {
        vscale = aVscale;
    }
}
