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

import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Settable;
import xmet.tools.metadata.editor.views.scv.impl.NamedItem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * This is a variation of EntityIC that maps to a Setable entity.
 * @author Nahid Akbar
 */
public class SetableIC
    extends EntityIC {

    /** The setable. */
    private Settable setable = null;

    /**
     * Instantiates a new setable ic.
     * @param sheet the sheet
     * @param parent the parent
     */
    public SetableIC(
        final Sheet sheet,
        final ParentItem parent) {
        super(sheet, parent);
    }

    /* == Helper getters and setters == */

    /**
     * Gets the setable.
     * @return the setable
     */
    public Settable getSetable() {
        return setable;
    }

    /**
     * Sets the setable.
     * @param modelEntity the new setable
     */
    public void setSetable(
        final Settable modelEntity) {
        setable = modelEntity;
    }

    /* == EntityIC wrappers == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSetableValue(
        final String toSet) {
        if (setable != null) {
            setable.setValue(toSet);
            setModified(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSetableValue() {
        if (setable != null) {
            if (setable.getValue() != null) {
                return setable.getValue().trim();
            }
        }
        return null;
    }

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
        setable = ModelUtils.getSetable(getEntity());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNamedItem(
        final String name,
        final NamedItem item) {
        addToNamesList(
            name,
            item);
    }

}
