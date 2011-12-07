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
 * List is a simple type which contains repetitions of another simple type.
 * @author Nahid Akbar
 */
public class List
    extends Simple {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new list.
     * @param parent the parent
     * @param name the name
     * @param namespace the namespace
     */
    public List(
        final Entity parent,
        final String name,
        final String namespace) {
        super(parent, name, namespace);
    }

    /**
     * Instantiates a new list.
     * @param name the name
     * @param nameSpace the name space
     */
    public List(
        final String name,
        final String nameSpace) {
        super(null, name, nameSpace);
    }

    /* == List type == */
    /** The list type. */
    private Simple listType;

    /**
     * Gets the list type.
     * @return the list type
     */
    public Simple getListType() {
        return listType;
    }

    /**
     * Sets the list type.
     * @param type the new list type
     */
    public void setListType(
        final Simple type) {
        listType = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueType() {
        return "List<"
            + listType.getValueType()
            + ">";
    }
}
