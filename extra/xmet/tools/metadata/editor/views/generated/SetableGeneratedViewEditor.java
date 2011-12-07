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
package xmet.tools.metadata.editor.views.generated;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import xmet.profiles.model.Entity;
import xmet.profiles.model.Settable;

/**
 * Editor class for setable items.
 * @author Nahid Akbar
 */
public class SetableGeneratedViewEditor
    extends GeneratedViewEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The item. */
    private final Settable item;

    /** The value field. */
    private final JTextField valueField;

    /**
     * Instantiates a new setable generated view editor.
     * @param aItem the item
     * @param actionListener the action listener
     */
    public SetableGeneratedViewEditor(
        final Settable aItem,
        final ActionListener actionListener) {

        this.item = aItem;
        setLayout(new GridBagLayout());
        // {
        // {
        final JPanel panel = GridBag.getNew();
        // {
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "value type="
                    + aItem.getValueType()),
            "w=rem;");
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "value:"),
            "w=rel;");

        valueField = new JTextField();
        SwingUtils.GridBag.add(
            panel,
            valueField,
            "w=rem;f=h;wx=1;");
        String varString = null;
        if (aItem.getValue() != null) {
            varString = aItem.getValue().trim();
        } else {
            varString = "";
        }
        valueField.setText(varString);
        // }
        panel.setBorder(BorderFactory.createTitledBorder("Value"));
        SwingUtils.GridBag.add(
            this,
            panel,
            "w=rem;f=h;");
        // }
        // }

        if (aItem instanceof Entity) {
            if (((Entity) aItem).isValid()) {
                SwingUtils.GridBag.add(
                    this,
                    new JLabel(
                        "Validation: Valid"),
                    "w=rem;f=h;");
            } else {
                SwingUtils.GridBag.add(
                    this,
                    new JLabel(
                        String.format(
                            "Validation: Invalid - %1$s",
                            ((Entity) aItem).getValidationError())),
                    "w=rem;f=h;");
            }
        }

        SwingUtils.GridBag.add(
            this,
            Box.createGlue(),
            "w=rem;f=b;wx=1;wy=1;");

        setBorder(BorderFactory.createTitledBorder("Setable"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void stopEditting() {
        String value = valueField.getText();

        if (value != null
            && value.trim().length() != 0) {
            value = value.trim();
            item.setValue(value);
        } else {
            item.setValue(null);
        }
    }

}
