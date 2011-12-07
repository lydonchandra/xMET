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

import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import xmet.profiles.model.Group;

/**
 * Editor class for groups.
 * @author Nahid Akbar
 */
public class GroupGeneratedViewEditor
    extends GeneratedViewEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The item. */
    private final Group item;

    /**
     * Instantiates a new group generated view editor.
     * @param aItem the item
     * @param actionListener the action listener
     */
    public GroupGeneratedViewEditor(
        final Group aItem,
        final ActionListener actionListener) {
        this.item = aItem;
        setLayout(new GridBagLayout());

        // {
        // {
        final JPanel panel = GridBag.getNew();
        if (aItem.hasConstraints()) {
            SwingUtils.GridBag.add(
                panel,
                new JLabel(
                    "minOccurs="
                        + aItem.getConstraints().getMinOccurs()),
                "w=rem;");
            SwingUtils.GridBag.add(
                panel,
                new JLabel(
                    "maxOccurs="
                        + aItem.getConstraints().getMaxOccurs()),
                "w=rem;");
        } else {
            SwingUtils.GridBag.add(
                panel,
                new JLabel(
                    "Constraints not set"),
                "w=rem;");
        }
        panel.setBorder(BorderFactory.createTitledBorder("Constraints"));
        SwingUtils.GridBag.add(
            this,
            panel,
            "w=rem;f=h;");
        // }
        //
        // }
        // {
        if (aItem.isValid()) {
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
                        aItem.getValidationError())),
                "w=rem;f=h;");
        }
        // }
        SwingUtils.GridBag.add(
            this,
            Box.createGlue(),
            "w=rem;f=b;wx=1;wy=1;");

        setBorder(BorderFactory.createTitledBorder("Group Item"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void stopEditting() {

    }

    /**
     * Gets the item.
     * @return the item
     */
    public Group getItem() {
        return item;
    }

}
