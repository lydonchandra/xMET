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
 * An union is a simple type which contains one or more sub simple types. The
 * data present in the place must be of one of those simple types.
 * @author Nahid Akbar
 */
public class Union
    extends Simple {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new union.
     * @param parent the parent
     * @param name the name
     * @param namespace the namespace
     */
    public Union(
        final Entity parent,
        final String name,
        final String namespace) {
        super(parent, name, namespace);
    }

    /**
     * Instantiates a new union.
     * @param name the name
     * @param namespace the namespace
     */
    public Union(
        final String name,
        final String namespace) {
        super(null, name, namespace);
    }

    /* == Children types == */
    /** The children types. */
    private ArrayList<Simple> children = new ArrayList<Simple>();

    /**
     * Gets the children types.
     * @return the children
     */
    public ArrayList<Simple> getChildren() {
        return children;
    }

    /**
     * Sets the children types.
     * @param aChildren the new children
     */
    public void setChildren(
        final ArrayList<Simple> aChildren) {
        this.children = aChildren;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        for (final Simple me : getChildren()) {
            me.setValue(value);
        }
        super.setValue(value);
    }

}
