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
 * An entity that is either present or not present but does not repeat.
 * @author Nahid Akbar
 */
public class Optional
    extends Entity {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new optional.
     * @param parent the parent
     * @param aBaseTerm the base term. can not be null
     */
    public Optional(
        final Entity parent,
        final Entity aBaseTerm) {
        super(parent, aBaseTerm.getQualifiedName(), aBaseTerm.getNamespace());
        this.baseTerm = aBaseTerm;
    }

    /* == Base term == */
    /** The base term. */
    private Entity baseTerm = null;

    /**
     * Gets the base term.
     * @return the base term
     */
    public Entity getBaseTerm() {
        return baseTerm;
    }

    /**
     * Sets the base term reserve.
     * @param reserve the new base term reserve
     */
    public void setBaseTermReserve(
        final Entity reserve) {
        baseTerm = reserve;
    }

    /**
     * Sets the term present.
     * @param b the new term present
     */
    public void setTermPresent(
        final boolean b) {
        if (b) {
            if (getSetTerm() == null) {
                if (getBaseTerm() != null) {
                    setSetTerm(ModelUtils.cloneEntity(getBaseTerm()));
                } else {
                    setSetTerm(null);
                    /* Reporting.err("Reserve not found"); */
                }
            }
            // else {
            // /* Reporting.log("term already set"); */
            // }
        } else {
            setSetTerm(null);
        }
    }

    /* == Term - instanciation == */
    /** The set term. */
    private transient Entity setTerm = null;

    /**
     * Gets the sets the term.
     * @return the term
     */
    public Entity getSetTerm() {
        return setTerm;
    }

    /**
     * Sets the sets the term.
     * @param term the term to set
     */
    public void setSetTerm(
        final Entity term) {
        setTerm = term;
        if (term != null) {
            term.setParent(this);
        }
    }

    /**
     * Checks if is sets the term present.
     * @return true, if is sets the term present
     */
    public boolean isSetTermPresent() {
        return getSetTerm() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getQualifiedName() {
        if (isSetTermPresent()) {
            return getSetTerm().getQualifiedName();
        } else {
            if (baseTerm != null) {
                return baseTerm.getQualifiedName();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespace() {
        if (isSetTermPresent()) {
            return getSetTerm().getNamespace();
        } else {
            if (baseTerm != null) {
                return baseTerm.getNamespace();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "optional %1$s",
            getQualifiedName());
    }

}
