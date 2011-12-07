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

import java.awt.Component;
import java.util.ArrayList;

import n.ui.patterns.propertySheet.BooleanPSE;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.tools.metadata.editor.views.scv.model.Param;

/**
 * Variant of ListEditor PSE which also lets the entry and editing of Editable
 * paramater.
 * @author Nahid Akbar
 */
public class ListEditableEditorPSE
    extends ItemParamEditor
    implements
    PropertySheetEditor {

    /** The editable field. */
    private final BooleanPSE editableField = new BooleanPSE();

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        setParams((ArrayList<Param>) value);
        final String param = getParamTexttrim("Editable");
        return editableField.getEditor(
            (param != null)
                && param.equals("true"),
            item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        /* clearParams(); */
        final String value =
            Boolean.toString((Boolean) editableField.getValue());
        setParam(
            "Editable",
            value);
        return getParams();
    }

}
