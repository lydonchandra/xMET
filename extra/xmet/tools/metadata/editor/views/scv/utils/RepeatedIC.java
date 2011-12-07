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
package xmet.tools.metadata.editor.views.scv.utils;

import java.util.ArrayList;

import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Repeated;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * A common IC for all types of repeated items.
 * @param <E> the element type that repeats
 * @author Nahid Akbar
 */
public class RepeatedIC<E>
    extends EntityIC {

    /** The repeated model element. */
    private Repeated repeated;

    /** The base group that repeats. */
    private final E baseGroup;

    // /** The item. */
    // RepeatedItem<E> item;

    /* == COnstructors == */
    /**
     * Instantiates a new repeated item ic.
     * @param sheet the sheet
     * @param parent the parent
     * @param aBaseGroup the base group
     * @param item the item
     */
    public RepeatedIC(
        final Sheet sheet,
        final ParentItem parent,
        final E aBaseGroup,
        final RepeatedItem<E> item) {
        super(sheet, parent);
        this.baseGroup = aBaseGroup;
        // this.item = item;
        synchronizeRepeatedEntities();
    }

    /* == Repeated Items == */
    /** The repeated items. */
    private final ArrayList<E> repeatedItems = new ArrayList<E>();

    /**
     * helper method that synchronizes repeated items with the model element.
     */
    public void synchronizeRepeatedEntities() {
        if (repeated != null) {
            if (repeatedItems.size() != repeated.getEntities().size()) {
                while (repeatedItems.size() < repeated.getEntities().size()) {
                    repeatedAddNewItemAndInitialize(true);
                }
                while (repeatedItems.size() > repeated.getEntities().size()) {
                    repeatedItems.remove(repeatedItems.size() - 1);
                }
            }
        } else {
            repeatedItems.clear();
        }
    }

    /**
     * Gets the repeated items.
     * @return the repeated items
     */
    public ArrayList<E> getRepeatedItems() {
        synchronizeRepeatedEntities();
        return repeatedItems;
    }

    /**
     * Gets the count.
     * @return the count
     */
    public int getRepeatedItemsCount() {
        if (repeated != null) {
            return repeated.getEntities().size();
        }
        return 0;
    }

    /**
     * Adds the new repeated item.
     * @return true, if successful
     */
    public boolean addNewRepeatedItem() {
        if ((repeated != null)
            && (baseGroup != null)) {
            repeated.addNewEntity();
            repeatedAddNewItemAndInitialize(false);
            return true;
        }
        return false;
    }

    /**
     * Repeated add new item and initialize.
     * @param synchronizing TODO
     */
    @SuppressWarnings("unchecked")
    private void repeatedAddNewItemAndInitialize(
        final boolean synchronizing) {
        E newElem;
        newElem = (E) SCVUtils.clone((ModelItem) baseGroup);
        repeatedItems.add(newElem);
        /*
         * clonner method in SCVUtils does not clone initialization context so
         * initialization context needs to be set manually through
         * initialization code
         */
        SCVUtils.initializeItem(
            getSheet(),
            getParent(),
            (ModelItem) newElem,
            getIndexPath(repeatedItems.size() - 1));
        if (!synchronizing) {
            SCVUtils.loadDefaultValues((ModelItem) newElem);
        }
    }

    /**
     * Removes the repeated item.
     * @param current the current
     */
    public void removeRepeatedItem(
        final int current) {
        /* assert (repeated.getEntities().size() == repeatedItems.size()); */
        if ((repeated != null)) {
            final E item = repeatedItems.get(current);
            SCVUtils.uninitializeItem(
                getSheet(),
                getParent(),
                (ModelItem) item,
                getIndexPath(repeatedItems.size() - 1));
            repeatedItems.remove(current);
            if ((current >= 0)
                && (current < repeated.getEntities().size())) {
                repeated.removeEntityByIndex(current);
            }
            if (repeated.getEntities().size() == 0) {
                repeated.addNewEntity();
            }
            synchronizeRepeatedEntities();
        }
    }

    /* == EntityIC Overrides == */
    /**
     * {@inheritDoc}
     */
    @Override
    protected void traceXpath(
        final String xpath,
        final boolean hardTrace) {
        super.traceXpath(
            xpath,
            hardTrace);
        final Entity e = getEntity();
        if (e != null) {
            repeated = ModelUtils.getParentRepeatedEntity(e);
        } else {
            repeated = null;
        }
        synchronizeRepeatedEntities();
    }

    /* == Misc helper methods == */
    /**
     * Gets the indexed path.
     * @param index the index
     * @return the index path
     */
    public String getIndexPath(
        final int index) {
        return getNodeXpath()
            + "["
            + (index + 1)
            + "]";
    }

    /**
     * Checks if is model present.
     * @return true, if is model present
     */
    public boolean isModelPresent() {
        if (repeated != null) {
            return ModelUtils.isPresent(repeated);
        }
        return false;
    }
}
