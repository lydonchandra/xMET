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
package xmet.tools.metadata.editor.views.scv.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import n.algorithm.ArrayUtil;
import n.java.ReflectionUtils;
import n.reporting.Reporting;
import n.ui.patterns.propertySheet.PropertySheet;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;

/**
 * A PropertySheet which takes in information encoded in a list of
 * CustomReflectivePropertySheet objects to generate the PropertySheet.
 * @author Nahid Akbar
 */
@SuppressWarnings({
"serial",
"rawtypes"
})
public class CustomReflectivePropertySheet
    extends PropertySheet {

    /** The infos. */
    private CustomReflectivePropertySheetItem[] infos = null;

    /** The params. */
    private final Map<String, Object> params;

    /**
     * Instantiates a new custom reflective property sheet.
     * @param items the items
     * @param aParams the params
     */
    public CustomReflectivePropertySheet(
        final CustomReflectivePropertySheetItem[] items,
        final Object[] aParams) {
        super(extractItems(items));
        this.infos = items.clone();
        this.params = ArrayUtil.extractStringObjectPairMap(aParams);
        update();
    }

    /**
     * Extract PropertySheetItem objects from CustomReflectivePropertySheetItem
     * objects.
     * @param items the items
     * @return the property sheet item[]
     */
    private static PropertySheetItem[] extractItems(
        final CustomReflectivePropertySheetItem[] items) {
        final ArrayList<PropertySheetItem> psis =
            new ArrayList<PropertySheetItem>();
        for (final CustomReflectivePropertySheetItem psi : items) {
            psis.add(new PropertySheetItem(
                psi.getLabel(),
                psi.getDescription()));
        }
        return psis.toArray(new PropertySheetItem[psis.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getValue(
        final int index) {
        if (infos == null) {
            return null;
        }
        final CustomReflectivePropertySheetItem info = infos[index];
        try {
            if ((info.getFieldName() != null)
                && (info.getItem() != null)) {
                return ReflectionUtils.getFieldValueByName(
                    info.getFieldName(),
                    info.getItem());
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(
        final int index,
        final Object value) {
        if (infos == null) {
            return;
        }
        final CustomReflectivePropertySheetItem info = infos[index];
        try {
            if ((info.getFieldName() != null)
                && (info.getItem() != null)) {
                ReflectionUtils.setFieldValueByName(
                    info.getFieldName(),
                    info.getItem(),
                    value);
            }
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PropertySheetEditor getEditor(
        final int index) {
        if (infos == null) {
            return null;
        }
        final CustomReflectivePropertySheetItem info = infos[index];
        final Class editorClass = info.getType();

        Map<String, Object> paramsList = null;
        if ((info.getParams() != null)
            && (info.getParams().length > 0)) {
            paramsList = new HashMap<String, Object>();
            for (final String param : info.getParams()) {
                paramsList.put(
                    param,
                    params.get(param));
            }
        }
        return initializeEditor(
            editorClass,
            paramsList);
    }

}
