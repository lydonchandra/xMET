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

import java.io.Serializable;
import java.util.Observable;

/**
 * This is the parent of anything that can be referenced from within the model.
 * e.g. element declarations, simple, model group etc.
 * @author Nahid Akbar
 */
public abstract class Entity
    extends Observable
    implements
    Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /* == Identification == */
    /**
     * name of the entity.<br />
     * format:<br />
     * <b>[namespacePrefix:entityName]</b> or just <b>[entityName]</b><br />
     * if there is a namespace and has a short prefix and it should be set that
     * way.
     */
    private String qualifiedName = null;

    /**
     * Gets the name.
     * @return the name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setQualifiedName(
        final String name) {
        qualifiedName = name;
    }

    /**
     * The namespace url.
     */
    private String namespace;

    /**
     * Gets the namespace.
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace.
     * @param aNamespace the new namespace
     */
    public void setNamespace(
        final String aNamespace) {
        this.namespace = aNamespace;
    }

    /* == Backlinking == */

    /**
     * Direct parent entity of the entity.
     */
    private Entity parent = null;

    /**
     * Gets the parent.
     * @return the parent
     */
    public Entity getParent() {
        return parent;
    }

    /**
     * Sets the parent.
     * @param aParent the parent to set
     */
    public void setParent(
        final Entity aParent) {
        this.parent = aParent;
    }

    /* == Constructors == */

    /**
     * Instantiates a new entity.
     * @param aParent the parent
     * @param name the name
     * @param aNamespace the namespace
     */
    protected Entity(
        final Entity aParent,
        final String name,
        final String aNamespace) {
        this.setParent(aParent);
        setQualifiedName(name);
        this.setNamespace(aNamespace);
    }

    /* == for validation Purposes == */
    /**
     * Whether the entity is valid. Set to true if not assessed.
     */
    private String validationError = null;

    /**
     * Checks if is valid.
     * @return the valid
     */
    public boolean isValid() {
        return getValidationError() == null;
    }

    /**
     * Sets the validation error.
     * @param aValidationError the new validation error
     */
    public void setValidationError(
        final String aValidationError) {
        this.validationError = aValidationError;
    }

    /**
     * Gets the validation error.
     * @return the validation error
     */
    public String getValidationError() {
        return validationError;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "<"
            + getClass().getName()
            + " - "
            + getQualifiedName()
            + ">";
    }

}
