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

import n.reporting.Reporting;

/**
 * Repeated represents repeated element declarations.
 * @author Nahid Akbar
 */
public class Repeated
    extends Entity
    implements
    Iterable<Entity> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new repeated.
     * @param parent the parent
     * @param entity the entity
     */
    public Repeated(
        final Entity parent,
        final Entity entity) {
        super(parent, entity.getQualifiedName(), entity.getNamespace());
        setBaseEntity(entity);
        getBaseEntity().setParent(
            this);
    }

    /* == Base entity == */
    /** The base entity. */
    private Entity baseEntity;

    /**
     * Gets the base entity.
     * @return the base entity
     */
    public Entity getBaseEntity() {
        return baseEntity;
    }

    /**
     * Sets the base entity.
     * @param aBaseEntity the new base entity
     */
    public void setBaseEntity(
        final Entity aBaseEntity) {
        this.baseEntity = aBaseEntity;
    }

    /* == Multiplicity constraints == */

    /**
     * Gets the multiplicity constraints.
     * @return the multiplicity constraints
     */
    public MultiplicityConstraints getMultiplicityConstraints() {
        if (getBaseEntity() instanceof ElementDeclaration) {
            return ((ElementDeclaration) getBaseEntity()).getConstraints();
        } else {
            return null;
        }
    }

    /**
     * Sets the multiplicity constraints.
     * @param multiplicityConstraints the new multiplicity constraints
     */
    public void setMultiplicityConstraints(
        final MultiplicityConstraints multiplicityConstraints) {
        Reporting.logUnexpected();
    }

    /* == Repeated entities == */
    /** The repeated entities. */
    private final ArrayList<Entity> repeatedEntities = new ArrayList<Entity>();

    /**
     * Gets an entity by index; adding new when necessary.
     * @param index the index of the entity to get
     * @return the entity at the index
     */
    public Entity getEntityByIndex(
        final int index) {
        while (entityCount() <= index) {
            addNewEntity();
        }
        return repeatedEntities.get(index);
    }

    /**
     * Index of specified entity.
     * @param entity the specified entity
     * @return the index
     */
    public int indexOfEntity(
        final Entity entity) {
        return repeatedEntities.indexOf(entity);
    }

    /**
     * Returns the number of repeated entities.
     * @return the int
     */
    public int entityCount() {
        return repeatedEntities.size();
    }

    /**
     * Adds the new repeated entity.
     * @return the entity
     */
    public Entity addNewEntity() {
        final Entity e = ModelUtils.cloneEntity(getBaseEntity());
        e.setParent(this);
        repeatedEntities.add(e);
        return e;
    }

    /**
     * Removes an entity by specified index.
     * @param index the index of the entity to remove
     * @return the entity
     */
    public Entity removeEntityByIndex(
        final int index) {
        return repeatedEntities.remove(index);
    }

    /**
     * Removes an entity.
     * @param entity the entity to remove
     * @return true, if successful
     */
    public boolean removeEntity(
        final Entity entity) {
        return repeatedEntities.remove(entity);
    }

    /**
     * Gets the children.
     * @return the children
     */
    public ArrayList<Entity> getEntities() {
        return repeatedEntities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Entity> iterator() {
        return repeatedEntities.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "repeated %1$s",
            getQualifiedName());
    }

}
