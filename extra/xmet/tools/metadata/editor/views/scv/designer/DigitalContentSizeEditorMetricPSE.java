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

import n.ui.patterns.propertySheet.ObjectChoicePSE;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.tools.metadata.editor.views.scv.model.Param;
import xmet.ui.controls.DigitalContentSizeEditor;

/**
 * This PSE Edits the Metric parameter of DigitalContentSizeEditor control.
 * @author Nahid Akbar
 */
@SuppressWarnings("unchecked")
public class DigitalContentSizeEditorMetricPSE
    extends ItemParamEditor
    implements
    PropertySheetEditor {

    /** The editable field. */
    private final ObjectChoicePSE editableField = new ObjectChoicePSE();

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        setParams((ArrayList<Param>) value);
        final String param = getParamTexttrim("Metric");
        editableField.setChoices(DigitalContentSizeEditor.getSizeUnitSuffix());
        String varValue = null;
        if (param != null) {
            varValue = param;
        } else {
            varValue = "B";
        }
        return editableField.getEditor(
            varValue,
            item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        clearParams("Metric");
        final String value = (String) editableField.getValue();
        setParam(
            "Metric",
            value);
        return getParams();
    }

}
