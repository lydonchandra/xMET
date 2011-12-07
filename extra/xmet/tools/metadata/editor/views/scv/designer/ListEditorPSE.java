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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import n.ui.SwingUtils;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.tools.metadata.editor.views.scv.model.Param;

/**
 * This allows the entry of either the CustomList or CodeList parameter.
 * @author Nahid Akbar
 */
public class ListEditorPSE
    extends ItemParamEditor
    implements
    PropertySheetEditor {

    /** The Constant CUSTOM_LIST. */
    private static final String CUSTOM_LIST = "CustomList";

    /** The Constant CODE_LIST. */
    private static final String CODE_LIST = "CodeList";

    /**
     * The type field.
     */
    private final JComboBox typeField = new JComboBox();

    {
        typeField.addItem(CODE_LIST);
        typeField.addItem(CUSTOM_LIST);
    }

    /**
     * The value field.
     */
    private final JTextField valueField = new JTextField();

    /** The panel. */
    private JPanel panel;
    {
        panel = SwingUtils.GridBag.getNew();
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Type:*"),
            "w=rel;wx=0;f=h;");
        SwingUtils.GridBag.add(
            panel,
            typeField,
            "w=rem;f=h;wx=1;");

        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Value:*"),
            "w=rel;wx=0;f=h;");
        SwingUtils.GridBag.add(
            panel,
            valueField,
            "w=rem;f=h;wx=1;");
    }

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
            String codeList = getParamTexttrim(CODE_LIST);
            if (codeList != null) {
                typeField.setSelectedIndex(0);
                valueField.setText(codeList);
            } else {
                typeField.setSelectedIndex(1);
                codeList = getParamTexttrim(CUSTOM_LIST);
                valueField.setText(codeList);
            }
        }
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        clearParams(
            CODE_LIST,
            CUSTOM_LIST,
            "Editable");
        removeParam(CODE_LIST);
        removeParam(CUSTOM_LIST);
        final String option = (String) typeField.getSelectedItem();
        if (option != null) {
            setParam(
                option,
                valueField.getText().trim());
        }
        // else {
        // }
        return getParams();
    }

}
