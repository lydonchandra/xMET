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

import xmet.tools.metadata.editor.views.scv.impl.Group;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * Group IC.
 * @author Nahid Akbar
 */
public class GroupIC
    extends InitializationContext {

    /** The group. */
    private final Group group;

    /**
     * Instantiates a new group ic.
     * @param sheet the sheet
     * @param parent the parent
     * @param aGroup the group
     */
    public GroupIC(
        final Sheet sheet,
        final ParentItem parent,
        final Group aGroup) {
        super(sheet, parent);
        this.group = aGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelItem getItem() {
        return group;
    }

}
