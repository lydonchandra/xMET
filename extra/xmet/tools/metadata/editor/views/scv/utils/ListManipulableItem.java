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

import n.reporting.Reporting;
import xmet.tools.metadata.editor.views.scv.impl.ContentNamedItem;
import xmet.tools.metadata.editor.views.scv.impl.HideableNamedItem;
import xmet.tools.metadata.editor.views.scv.impl.NamedItem;

/**
 * List class for maintaining a list of manipulable items with the same name.
 * @author Nahid Akbar
 */
public class ListManipulableItem
    extends ArrayList<NamedItem>
    implements
    HideableNamedItem,
    ContentNamedItem {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /** The name of the list of manipulable items. */
    private final String name;

    /**
     * Instantiates a new list manipulable item.
     * @param aName the name
     */
    public ListManipulableItem(
        final String aName) {
        super();
        this.name = aName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(
        final boolean visible) {
        for (final NamedItem item : this) {
            if (item instanceof HideableNamedItem) {
                ((HideableNamedItem) item).setVisible(visible);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        for (final NamedItem item : this) {
            if (item instanceof ContentNamedItem) {
                ((ContentNamedItem) item).setValue(value);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMandatory(
        final boolean mandatory) {
        for (final NamedItem item : this) {
            if (item instanceof ContentNamedItem) {
                ((ContentNamedItem) item).setMandatory(mandatory);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        final StringBuffer sb = new StringBuffer();
        for (final NamedItem item : this) {
            if (item instanceof ContentNamedItem) {
                sb.append(((ContentNamedItem) item).getValue());
                sb.append(' ');
            }
        }
        return sb.toString().trim();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidationError(
        final String validationError) {
        for (final NamedItem item : this) {
            if (item instanceof ContentNamedItem) {
                ((ContentNamedItem) item).setValidationError(validationError);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(
        final String aName) {
        Reporting.logUnexpected();
    }

}
