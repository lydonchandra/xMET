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
import xmet.tools.metadata.editor.views.scv.impl.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;
import xmet.tools.metadata.editor.views.scv.utils.DefaultModelVisitor;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;

/**
 * After data has been loaded, it feeds the semi custom view gui with the data.
 * @author Nahid Akbar
 */
public class DataLoaderUtil
    extends DefaultModelVisitor {

    /**
     * Instantiates a new data loader.
     * @param group the sheet
     */
    public DataLoaderUtil(
        final ModelItem group) {
        SCVUtils.accept(
            group,
            this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {
        visitItem(item);
        super.postVisitItem(item);
    }

    /**
     * Visit item.
     * @param item the item
     */
    private void visitItem(
        final Item item) {
        if ((item.getDc() != null)
            && (item.getIc() != null)) {
            try {
                item.getDc().loadValue();
                item.getDc().onLoadUpdate();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        visitItem(item);
        super.postVisitCompositeItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        if (repeated.getRIC() != null) {
            repeated.getRIC().synchronizeRepeatedEntities();
        }
        return super.preVisitRepeatedGroup(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSheet(
        final Sheet sheet) {
        super.postVisitSheet(sheet);
        if (sheet.getDc() != null) {
            sheet.getDc().reValidate(
                sheet);
        }
    }

}
