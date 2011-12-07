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
package xmet.profiles.model;

/**
 * xs:choice implementation.
 * @author Nahid Akbar
 */
public class ChoiceGroup
    extends Group {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new choice.
     * @param parent the parent
     */
    public ChoiceGroup(
        final Entity parent) {
        super(parent, "xs:choice", "http://www.w3.org/2001/XMLSchema");
    }

    /**
     * The selected.
     */
    private int selected = 0;

    /**
     * Gets the selected.
     * @return the selected
     */
    public int getSelected() {
        return selected;
    }

    /**
     * Checks if is selected.
     * @return true, if is selected
     */
    public boolean isSelected() {
        return ((getSelected() >= 0) && (getSelected() < getChildren().size()));
    }

    /**
     * Sets the selected.
     * @param aSelected the selected to set
     */
    public void setSelected(
        final int aSelected) {
        /* Reporting.log("setSelected %1$d", selected); */
        this.selected = aSelected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "<xs:choice>";
    }

}
