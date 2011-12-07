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
import java.util.Iterator;

/**
 * Represents a ModelGroup element.
 * @author Nahid Akbar
 */
public abstract class Group
    extends Entity
    implements
    Iterable<Entity> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new group.
     * @param parent the parent
     * @param name the name
     * @param namespace the namespace
     */
    protected Group(
        final Entity parent,
        final String name,
        final String namespace) {
        super(parent, name, namespace);
    }

    /* == Particle Elements constraints == */
    /** The constraints. */
    private MultiplicityConstraints constraints = null;

    /**
     * Checks for constraints.
     * @return true, if successful
     */
    public boolean hasConstraints() {
        return constraints != null;
    }

    /**
     * Gets the constraints.
     * @return the constraints
     */
    public MultiplicityConstraints getConstraints() {
        return constraints;
    }

    /**
     * Sets the constraints.
     * @param aConstraints the new constraints
     */
    public void setConstraints(
        final MultiplicityConstraints aConstraints) {
        this.constraints = aConstraints;
    }

    /* == Particles/Children == */
    /** Particles. */
    private ArrayList<Entity> children = new ArrayList<Entity>();

    /**
     * Gets the children.
     * @return the children
     */
    public ArrayList<Entity> getChildren() {
        return children;
    }

    /**
     * Sets the children.
     * @param aChildren the children to set
     */
    public void setChildren(
        final ArrayList<Entity> aChildren) {
        this.children = aChildren;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Entity> iterator() {
        return getChildren().iterator();
    }

}
