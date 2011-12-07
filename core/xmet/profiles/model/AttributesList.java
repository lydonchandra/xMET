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

import java.util.ArrayList;

/**
 * Manager for attributes.
 * @author Nahid Akbar
 */
public class AttributesList
    extends ArrayList<ElementAttribute> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Adds the attribute.
     * @param ea the ea
     * @return the entity attribute
     */
    public ElementAttribute addAttribute(
        final ElementAttribute ea) {
        for (final ElementAttribute e : this) {
            if (e.getQualifiedName().equals(
                ea.getQualifiedName())) {
                e.setValue(ea.getValue());
                return e;
            }
        }
        add(ea);
        return ea;
    }

    /**
     * Gets the attribute by index.
     * @param index the index
     * @return the attribute by index
     */
    public ElementAttribute getAttributeByIndex(
        final int index) {
        if ((index >= 0)
            && (index < size())) {
            return get(index);
        }
        return null;
    }

    /**
     * Gets the by name.
     * @param name the name
     * @return the by name
     */
    public ElementAttribute getAttributeByName(
        final String name) {
        for (final ElementAttribute ea : this) {
            if (ea.getQualifiedName().equals(
                name)) {
                return ea;
            }
        }
        return null;
    }

    /**
     * Sets the parent.
     * @param elementDeclaration the new parent
     */
    public void setAttributesParent(
        final ElementDeclaration elementDeclaration) {
        for (final ElementAttribute ea : this) {
            ea.setParent(elementDeclaration);
        }
    }

    /** The valid. */
    private transient boolean valid = true;

    /**
     * Checks if is valid.
     * @return true, if is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets the valied.
     * @param b the new valied
     */
    public void setValid(
        final boolean b) {
        this.valid = b;
    }
}
