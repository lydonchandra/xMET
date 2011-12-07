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
package xmet.tools.metadata.editor.views.scv.view;

import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.utils.DefaultModelVisitor;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;

/**
 * Resets the data of a ModelItem branch - ideal when reusing UI elements.
 * @author Nahid Akbar
 */
public class DataResetUtil
    extends DefaultModelVisitor {

    /**
     * Instantiates a new data reset util.
     * @param item the item
     * @param lastPath the last path
     */
    public DataResetUtil(
        final ModelItem item,
        final String lastPath) {
        super(lastPath);
        SCVUtils.accept(
            item,
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        if (item.getDc() != null) {
            item.getDc().getControl().setValue(
                null);
            item.getDc().onChangeUpdate();
        }
        super.postVisitCompositeItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {
        if (item.getDc() != null) {
            item.getDc().getControl().setValue(
                null);
        }
        super.postVisitItem(item);
    }
}
