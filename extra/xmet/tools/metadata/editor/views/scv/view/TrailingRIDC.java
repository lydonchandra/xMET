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

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.tools.metadata.editor.views.scv.impl.Group;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedItem;

/**
 * A DC for Repeated Groups which shows repeated items stacked one below another
 * like a dynamic list. Whether to use this or the CompactRIDC Should depend on
 * the number of data items the user is likely to enter and also on how much
 * free spage there is in the page.
 * @author Nahid Akbar
 */
public class TrailingRIDC
    extends RepeatedItemDC<Group> {

    /**
     * The panel.
     */
    private JPanel itemsSubPanel;

    /** The display panel. */
    private JPanel displayPanel;

    /**
     * Instantiates a new trailing ridc.
     * @param repeated the repeated
     */
    public TrailingRIDC(
        final RepeatedItem<Group> repeated) {
        super(repeated);
        rebuildSubPanelWrapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildSubPanelWrapper() {
        if (itemsSubPanel == null) {
            itemsSubPanel = GridBag.getNew();
            displayPanel = SwingUtils.BorderLayouts.getNew();
            displayPanel.add(new JScrollPane(
                itemsSubPanel));
        }

        itemsSubPanel.removeAll();

        final JPanel labelPanel = SwingUtils.BoxLayouts.getHorizontalPanel();

        SwingUtils.GridBag.add(
            labelPanel,
            Box.createHorizontalGlue(),
            "w=rem;");

        displayPanel.add(
            labelPanel,
            BorderLayout.NORTH);

        /* SwingUtils.GridBag.add(panel, */
        /* SwingUtils.Button.getNew("Add New", addIcon, this, "addItem"), */
        // "w=rem;a=mr;");

        int i = 0;
        for (final JPanel cp : getSubPanels()) {
            SwingUtils.GridBag.add(
                itemsSubPanel,
                new JLabel(
                    getRepeated().getLabel()
                        + " "
                        + ++i),
                "w=rem;f=b;a=mc;");
            SwingUtils.GridBag.add(
                itemsSubPanel,
                cp,
                "w=rel;f=b;wx=1;wy=0;a=l;");
            final Object[] params = {
                getSubPanels().indexOf(
                    cp)
            };
            SwingUtils.GridBag.add(
                itemsSubPanel,
                SwingUtils.BUTTON.getNewV(
                    "Delete",
                    getRemoveIcon(),
                    new ClassMethodCallback(
                        this,
                        "removeItem",
                        params)),
                "w=rem;f=b;a=mc;");
        }
        final Object[] params = {};

        SwingUtils.GridBag.add(
            itemsSubPanel,
            SwingUtils.BUTTON.getNewV(
                "Add New",
                getAddIcon(),
                new ClassMethodCallback(
                    this,
                    "addItem",
                    params)),
            "w=rem;a=mr;");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getDisplayPanel() {
        return displayPanel;
    }

    /**
     * Gets the label string.
     * @param repeatedGroup the repeated group
     * @return the label string
     */
    public String getLabelString(
        final RepeatedItem<Group> repeatedGroup) {
        return String.format(
            "%1$s",
            repeatedGroup.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addItem() {
        final boolean addItem = super.addItem();
        refreshPanel();
        return addItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeItem(
        final int index) {
        if (index != -1) {
            super.removeItem(index);
        }
        refreshPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshPanel() {
        try {
            displayPanel.revalidate();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
