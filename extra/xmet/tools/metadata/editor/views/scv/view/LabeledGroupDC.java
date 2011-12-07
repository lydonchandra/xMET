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
package xmet.tools.metadata.editor.views.scv.view;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import xmet.tools.metadata.editor.views.scv.impl.LabeledGroup;

/**
 * DC of a Labeled Group.
 * @author Nahid Akbar
 */
public class LabeledGroupDC
    extends DisplayContext {

    /** The display panel. */
    private final JPanel displayPanel;

    /** The labeled group. */
    private final LabeledGroup labeledGroup;

    /**
     * Instantiates a new labeled group display context.
     * @param item the item
     * @param subPanel the pop
     * @param label the label
     */
    public LabeledGroupDC(
        final LabeledGroup item,
        final JPanel subPanel,
        final String label) {
        labeledGroup = item;
        displayPanel = GridBag.getNew();
        SwingUtils.GridBag.add(
            displayPanel,
            new JLabel(
                label),
            "w=rem;f=h;wx=0;a=l;");
        JScrollPane jsp;
        jsp = new JScrollPane(
            subPanel);
        SwingUtils.GridBag.add(
            displayPanel,
            jsp,
            "w=rem;f=b;wx=1;wy=1;");
        jsp.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildDisplayPanel() {
        new PanelBuilderUtil(
            labeledGroup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getDisplayPanel() {
        return displayPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshPanel() {
        displayPanel.revalidate();
    }
}
