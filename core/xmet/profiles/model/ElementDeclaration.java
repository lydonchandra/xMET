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
 * Represents an element declaration.
 * @author Nahid Akbar
 */
public class ElementDeclaration
    extends Entity {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /* == Constraints == */
    /** The constraints. */
    private MultiplicityConstraints constraints = null;

    /**
     * Checks for constraints.
     * @return true, if successful
     */
    public boolean hasConstraints() {
        return getConstraints() != null;
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

    /* == Attributes == */
    /**
     * Attributes of the entity.
     */
    private AttributesList attributes;

    /**
     * Checks for attributes.
     * @return true, if successful
     */
    public boolean hasAttributes() {
        return getAttributes() != null;
    }

    /**
     * Gets the attributes.
     * @return the attributes
     */
    public AttributesList getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes.
     * @param aAttributes the new attributes
     */
    public void setAttributes(
        final AttributesList aAttributes) {
        this.attributes = aAttributes;
    }

    /* == Model Group == */
    /** The group. */
    private Entity group = null;

    /**
     * Gets the group.
     * @return the group
     */
    public Entity getGroup() {
        return group;
    }

    /**
     * Sets the group.
     * @param aGroup the group to set
     */
    public void setGroup(
        final Entity aGroup) {
        this.group = aGroup;
        if (aGroup != null) {
            aGroup.setParent(this);
        }
    }

    /**
     * Checks for group.
     * @return true, if successful
     */
    public boolean hasGroup() {
        return getGroup() != null;
    }

    /* == Substitutables == */

    /** for elements that can be substituted with other elements. */
    private Substitutables subtitutables = null;

    /**
     * Gets the substitutables.
     * @return the subtitutables
     */
    public Substitutables getSubstitutables() {
        return getSubtitutables();
    }

    /**
     * Checks for subtitutables.
     * @return true, if successful
     */
    public boolean hasSubtitutables() {
        return getSubtitutables() != null;
    }

    /**
     * Sets the subtitutables.
     * @param aSubtitutables the subtitutables to set
     */
    public void setSubtitutables(
        final Substitutables aSubtitutables) {
        this.subtitutables = aSubtitutables;
    }

    /* == Misc Helper Methods == */

    /**
     * Instantiates a new element declaration.
     * @param parent the parent
     * @param name the name
     * @param namespace the namespace
     */
    public ElementDeclaration(
        final Entity parent,
        final String name,
        final String namespace) {
        super(parent, name, namespace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "<%1$s>",
            getQualifiedName());
    }

    /**
     * Gets the for elements that can be substituted with other elements.
     * @return the for elements that can be substituted with other elements
     */
    public Substitutables getSubtitutables() {
        return subtitutables;
    }

}
