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

import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedGroup;

/**
 * The purpose of this util is re-hard tracing all the existing xpaths of a
 * choice stream so that we get valid data.
 * @author Nahid Akbar
 */
public class RetraceUtil
    extends DefaultModelVisitor {

    /**
     * Instantiates a new retrace util.
     * @param selectedItem the selected item
     */
    public RetraceUtil(
        final ModelItem selectedItem) {
        SCVUtils.accept(
            selectedItem,
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitItem(
        final Item item) {
        item.getIc().hardRetraceLastXpath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitCompositeItem(
        final CompositeItem item) {
        item.getIc().hardRetraceLastXpath();
        super.preVisitCompositeItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        repeated.getRIC().hardRetraceLastXpath();
        return super.preVisitRepeatedGroup(repeated);
    }

}
