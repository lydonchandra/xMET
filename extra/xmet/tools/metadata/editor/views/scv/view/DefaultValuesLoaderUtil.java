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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import n.reporting.Reporting;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.utils.DefaultModelVisitor;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;

/**
 * This util loads default value of items onto the interface.
 * @author Nahid Akbar
 */
public class DefaultValuesLoaderUtil
    extends DefaultModelVisitor {

    /**
     * Instantiates a new default values loader.
     * @param sheet the sheet
     */
    public DefaultValuesLoaderUtil(
        final ModelItem sheet) {
        SCVUtils.accept(
            sheet,
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
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        visitItem(item);
        super.postVisitCompositeItem(item);
    }

    /**
     * Visit item.
     * @param item the item
     */
    private void visitItem(
        final Item item) {
        /* attempt to first set the thingy in the gui object which should */
        /* automatically fire events to set it to xpath */
        /* but this also allows you to set value to a xpath place; */
        if (item.getDefaultValue() != null) {
            switch (item.getDefaultValue().getType()) {
            case text:
                setValue(
                    item,
                    item.getDefaultValue().getValuetrim());

                break;
            case eval:
                // {
                String toSet = null;
                if ((item.getDefaultValue().getValuetrim() == null)
                    || (item.getDefaultValue().getValuetrim().length() == 0)) {
                    Reporting.reportUnexpected("evaluation term not set");
                } else if (item.getDefaultValue().getValuetrim().equals(
                    "uuid")) {
                    toSet = UUID.randomUUID().toString();
                } else if (item.getDefaultValue().getValue().equals(
                    "currentDate")) {
                    toSet = (new SimpleDateFormat(
                        "yyyy-MM-dd")).format(new Date());
                } else {
                    Reporting.logUnexpected(item
                        .getDefaultValue()
                        .getValuetrim());
                }
                if (toSet != null) {
                    setValue(
                        item,
                        toSet);
                }
                // }
                break;
            default:
                Reporting.logUnexpected();
            }
        }
        // else
        // {
        // /* Reporting.err("wtf?"); */
        // }
    }

    /**
     * Sets the value.
     * @param item the item
     * @param value the value
     */
    private void setValue(
        final Item item,
        final String value) {
        if (item.getDc() != null) {
            item.getDc().setDefaultValue(
                value);
        } else if (item.getIc() != null) {
            item.getIc().setSetableValue(
                value);
        } else {
            Reporting.logUnexpected("Item/Sheet not initialized");
        }
    }

}
