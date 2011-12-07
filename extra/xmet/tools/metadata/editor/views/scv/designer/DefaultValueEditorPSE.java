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
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.tools.metadata.editor.views.scv.model.Default;
import xmet.tools.metadata.editor.views.scv.model.DefaultType;

/**
 * This PSE Edits the DefaultValue object types.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class DefaultValueEditorPSE
    extends JPanel
    implements
    PropertySheetEditor {

    /**
     * The value type field.
     */
    private final JComboBox valueTypeField;

    /**
     * The value field.
     */
    private final JComboBox valueField;

    /**
     * Instantiates a new default value editor item.
     */
    public DefaultValueEditorPSE() {
        setLayout(new GridBagLayout());
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Type:"),
            "w=rel;wx=0;f=h;");
        valueTypeField = new JComboBox();
        SwingUtils.GridBag.add(
            this,
            valueTypeField,
            "w=rem;f=h;wx=1;");
        SwingUtils.GridBag.add(
            this,
            new JLabel(
                "Value:"),
            "w=rel;wx=0;f=h;");
        valueField = new JComboBox();
        SwingUtils.GridBag.add(
            this,
            valueField,
            "w=rem;f=h;wx=1;");

        valueTypeField.addItem("");
        valueTypeField.addItem("text");
        valueTypeField.addItem("eval");

        valueField.addItem("uuid");
        valueField.addItem("currentDate");
        valueField.setEditable(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        final Default defaultValue = (Default) value;
        if (defaultValue != null) {
            if (defaultValue.getType() != null) {
                valueTypeField.setSelectedItem(defaultValue
                    .getType()
                    .toString());
                valueField.setSelectedItem(defaultValue.getValue());
            } else {
                valueTypeField.setSelectedIndex(0);
                valueField.setSelectedItem("");
            }
        } else {
            valueTypeField.setSelectedIndex(0);
            valueField.setSelectedItem("");
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        String valueType = (String) valueTypeField.getSelectedItem();
        String value = (String) valueField.getSelectedItem();
        // {
        final String v2 = (String) valueField.getEditor().getItem();
        if (v2 != null) {
            value = v2.trim();
        }
        // }

        if (valueType == null
            || valueType.trim().length() == 0) {
            valueType = "text";
        }
        if (value == null) {
            value = "";
        } else {
            value = value.trim();
        }
        if (value.length() > 0) {

            valueType = valueType.trim();
            final Default defaultValue = new Default();
            if (valueType.equals("text")) {
                defaultValue.setType(DefaultType.text);
                defaultValue.setValue(value);
            } else if (valueType.equals("eval")) {
                defaultValue.setType(DefaultType.eval);
                if (value.equals("uuid")) {
                    defaultValue.setValue("uuid");
                } else if (value.equals("currentDate")) {
                    defaultValue.setValue("currentDate");
                } else {
                    Reporting.logUnexpected(value);
                }
            } else {
                Reporting.logUnexpected(valueType);
            }
            return defaultValue;
        } else {
            return null;
        }
    }
}
