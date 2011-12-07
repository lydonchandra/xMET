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

import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * Page IC.
 * @author Nahid Akbar
 */
public class PageIC
    extends InitializationContext {

    /** The group. */
    private final Page item;

    /**
     * Instantiates a new page ic.
     * @param sheet the sheet
     * @param parent the parent
     * @param page the page
     */
    public PageIC(
        final Sheet sheet,
        final ParentItem parent,
        final Page page) {
        super(sheet, parent);
        item = page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelItem getItem() {
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reValidate() {
        if (getSheet().getDc() != null) {
            if (getParent() instanceof RepeatedItem) {
                getParent().getIC().reValidate();
            } else {
                /*
                 * Have an issue with repeated pages parents not holding the
                 * repeated container as their parent element. This tests if its
                 * a repeated page (i.e. title ends with a page number) and if
                 * so, validates the parent.
                 */
                if (item.getTitle() != null
                    && item.getTitle().matches(
                        "^.*\\d+$")) {
                    getSheet().getDc().reValidate(
                        getParent());
                } else {
                    getSheet().getDc().reValidate(
                        item);
                }
            }
        }
    }
}
