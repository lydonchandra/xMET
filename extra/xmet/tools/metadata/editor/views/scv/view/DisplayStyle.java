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
package xmet.tools.metadata.editor.views.scv.view;

import javax.swing.JLabel;

/**
 * Style information for how different things should look.
 * @author Nahid Akbar
 */
public class DisplayStyle {

    /**
     * style elements seperated out for one place editting.
     */
    private static DisplayStyle style = new DisplayStyle();

    /**
     * Instantiates a new display style.
     */
    public DisplayStyle() {

    }

    /**
     * private DisplayStyle() { Gets the field label.
     * @param title the title
     * @param description the description
     * @return the field label
     */
    public JLabel getFieldLabel(
        final String title,
        final String description) {
        if (description == null) {
            /* no description...render */
            return new JLabel(
                "<html><font style=\"font-size: 12px;font-weight: bold;\">"
                    + title);
        } else {
            if (title != null) {
                return new JLabel(
                    "<html><font style=\"font-size: 12px;font-weight: bold;\">"
                        + title
                        + "<table width=\"150px\"><tr><td>"
                        + description);
            } else {
                return new JLabel(
                    "<html><table width=\"150px\"><tr><td>"
                        + description);
            }

        }
    }

    /**
     * Format group header.
     * @param aLabel the label
     * @return the string
     */
    public String formatGroupHeader(
        final String aLabel) {
        String label = null;
        if (aLabel != null) {
            label = aLabel;
        } else {
            label = "";
        }
        return "<html><font style=\""
            + "font-weight: bold;"
            + " font-size: 12px;"
            + " text-decoration: underline;\">"
            + label;
    }

    /**
     * Format hover text.
     * @param hover the hover
     * @return the string
     */
    public String formatHoverText(
        final String hover) {
        return "<html><table width=\"200px\"><tr><td>"
            + hover;
    }

    /**
     * Gets the style elements seperated out for one place editting.
     * @return the style elements seperated out for one place editting
     */
    public static DisplayStyle getStyle() {
        return style;
    }
}
