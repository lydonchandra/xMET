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
import xmet.profiles.model.Extra;
import xmet.profiles.model.ModelUtils;

/**
 * Editor for extra xml data not found in xml.
 * @author Nahid Akbar
 */
public class ExtraGeneratedViewEditor
    extends GeneratedViewEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The action listener. */
    private final ActionListener actionListener;

    /** The item. */
    private final Extra item;

    /**
     * Instantiates a new extra generated view editor.
     * @param aItem the item
     * @param aActionListener the action listener
     */
    public ExtraGeneratedViewEditor(
        final Extra aItem,
        final ActionListener aActionListener) {
        this.actionListener = aActionListener;
        this.item = aItem;
        setLayout(new GridBagLayout());

        // {
        // {
        final JPanel panel = GridBag.getNew();
        // {
        SwingUtils.GridBag.add(
            panel,
            new JLabel(
                aItem.getContent()),
            "w=rem;");
        final Object[] params = {};
        SwingUtils.GridBag.add(
            panel,
            SwingUtils.BUTTON.getNew(
                "remove",
                new ClassMethodCallback(
                    this,
                    "remove",
                    params)),
            "w=rem;");
        // }
        panel.setBorder(BorderFactory.createTitledBorder("Content"));
        SwingUtils.GridBag.add(
            this,
            panel,
            "w=rem;f=h;");
        // }
        //
        // }
        SwingUtils.GridBag.add(
            this,
            Box.createGlue(),
            "w=rem;f=b;wx=1;wy=1;");
        setBorder(BorderFactory.createTitledBorder("Extra Data Item"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void stopEditting() {

    }

    /**
     * Removes the.
     */
    public void remove() {
        ModelUtils.removeEntity(item);
        actionListener.actionPerformed(null);
    }

}
