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
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.profiles.model.Optional;

/**
 * Editor class for optional nodes.
 * @author Nahid Akbar
 */
public class OptionalGeneratedViewEditor
    extends GeneratedViewEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The action listener. */
    private final ActionListener actionListener;

    /** The item. */
    private final Optional item;

    /**
     * Instantiates a new optional generated view editor.
     * @param aItem the item
     * @param aActionListener the action listener
     */
    public OptionalGeneratedViewEditor(
        final Optional aItem,
        final ActionListener aActionListener) {
        this.actionListener = aActionListener;
        this.item = aItem;
        setLayout(new GridBagLayout());

        // {
        // {
        final JPanel panel = GridBag.getNew();
        // {
        String varString = null;
        if (aItem.isSetTermPresent()) {
            varString = "set";
        } else {
            varString = "not set";
        }
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                "Optional Item is currently "
                    + varString),
            "w=rem;");
        final Object[] params = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "set",
                new ClassMethodCallback(
                    this,
                    "set",
                    params)),
            "w=rem;");
        final Object[] params1 = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "unset",
                new ClassMethodCallback(
                    this,
                    "unset",
                    params1)),
            "w=rem;");
        // }
        panel.setBorder(BorderFactory.createTitledBorder("Set"));
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
        setBorder(BorderFactory.createTitledBorder("Optional Item"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void stopEditting() {

    }

    /**
     * Sets the.
     */
    public void set() {
        item.setTermPresent(true);
        actionListener.actionPerformed(null);
    }

    /**
     * Unset.
     */
    public void unset() {
        item.setTermPresent(false);
        actionListener.actionPerformed(null);
    }

}
