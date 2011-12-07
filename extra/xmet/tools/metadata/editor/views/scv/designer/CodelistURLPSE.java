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

import javax.swing.JTextField;

import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.tools.metadata.editor.views.scv.model.Param;

/**
 * This PSE Is an editor for the Catalog parameter of the CodelistCatalogList
 * control.
 * @author Nahid Akbar
 */
public class CodelistURLPSE
    extends ItemParamEditor
    implements
    PropertySheetEditor {

    /** Editing field. */
    private final JTextField valueField = new JTextField();

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        setParams((ArrayList<Param>) value);
        if (getParams() != null) {
            final String codeList = getParamTexttrim("CodeList");
            if (codeList != null) {
                valueField.setText(codeList);
            } else {
                valueField.setText("");
            }
        }
        return valueField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        clearParams("CodeList");
        setParam(
            "CodeList",
            valueField.getText().trim());
        return getParams();
    }

}
