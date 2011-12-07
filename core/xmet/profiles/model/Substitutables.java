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
import java.util.ArrayList;

/**
 * A list of element declarations a particular element declaration can be
 * substituted by.
 * @author Nahid Akbar
 */
public class Substitutables
    implements
    Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * A single substitutable element declaration.
     */
    public static class Substitutable
        implements
        Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The name of the ed. */
        private String name;

        /** reference to the element declataion. */
        private ElementDeclaration entity;

        /**
         * Instantiates a new substitutable.
         * @param aName the name
         * @param entity2 the entity2
         */
        public Substitutable(
            final String aName,
            final ElementDeclaration entity2) {
            this.setName(aName);
            setEntity(entity2);
        }

        /**
         * Gets the name.
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the entity.
         * @return the entity
         */
        public ElementDeclaration getEntity() {
            return entity;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return getName();
        }

        /**
         * Sets the name of the ed.
         * @param aName the new name of the ed
         */
        public void setName(
            final String aName) {
            name = aName;
        }

        /**
         * Sets the reference to the element declataion.
         * @param aEntity the new reference to the element declataion
         */
        public void setEntity(
            final ElementDeclaration aEntity) {
            entity = aEntity;
        }
    }

    /** The list of substitutables. */
    private ArrayList<Substitutable> substitutables =
        new ArrayList<Substitutable>();

    /**
     * Adds the substitutable ed/name pair.
     * @param name the name
     * @param entity the entity
     */
    public void addSubstitutable(
        final String name,
        final ElementDeclaration entity) {
        if (!hasSubstitutable(name)) {
            getSubstitutables().add(
                new Substitutable(
                    name,
                    entity));
            entity.setSubtitutables(this);
        }
    }

    /**
     * Checks for substitutable by name.
     * @param name the name
     * @return true, if successful
     */
    public boolean hasSubstitutable(
        final String name) {
        for (final Substitutable s : getSubstitutables()) {
            if (s.getName().equals(
                name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a substutable element declaration of a name given its new parent.
     * @param name the name
     * @param parent the parent
     * @return the element declaration if substitutable found or null
     */
    public ElementDeclaration getElementDeclaration(
        final String name,
        final Entity parent) {

        for (final Substitutable s : getSubstitutables()) {
            if (s.getName().equals(
                name)) {
                final ElementDeclaration ed =
                    (ElementDeclaration) ModelUtils.cloneEntity(s.getEntity());
                ed.setParent(parent);
                return ed;
            }
        }
        return null;
    }

    /**
     * get the list of substitutable as an array of element declarations.
     * @return the element declaration[]
     */
    public ElementDeclaration[] asEdList() {
        final ElementDeclaration[] eds =
            new ElementDeclaration[getSubstitutables().size()];
        for (int i = 0; i < eds.length; i++) {
            eds[i] = getSubstitutables().get(
                i).getEntity();
        }
        return eds;
    }

    /**
     * Gets the list of substitutables.
     * @return the list of substitutables
     */
    public ArrayList<Substitutable> getSubstitutables() {
        return substitutables;
    }

    /**
     * Sets the list of substitutables.
     * @param aSubstitutables the new list of substitutables
     */
    public void setSubstitutables(
        final ArrayList<Substitutable> aSubstitutables) {
        substitutables = aSubstitutables;
    }

}
