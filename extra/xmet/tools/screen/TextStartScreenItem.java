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
import n.ui.patterns.propertySheet.UseStringPSE;

/**
 * A text SSI which displays text.
 * @author Nahid Akbar
 */
@CSC("text")
public class TextStartScreenItem
    extends StartScreenItem {

    /** The text. */
    @PSELabel("Text")
    @UseStringPSE
    @CS
    private String text;

    /** The font style. */
    @PSELabel("Font Style")
    @UseStringPSE
    @CS
    private String fontStyle;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Text: ");
        if (getText() == null
            || getText().trim().length() == 0) {
            sb.append("Unset");
        } else {
            sb.append(getText());
        }
        return sb.toString();
    }

    /**
     * Gets the text.
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     * @param aText the new text
     */
    public void setText(
        final String aText) {
        text = aText;
    }

    /**
     * Gets the font style.
     * @return the font style
     */
    public String getFontStyle() {
        return fontStyle;
    }

    /**
     * Sets the font style.
     * @param aFontStyle the new font style
     */
    public void setFontStyle(
        final String aFontStyle) {
        fontStyle = aFontStyle;
    }
}
