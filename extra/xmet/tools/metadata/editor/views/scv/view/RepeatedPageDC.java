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

import javax.swing.JPanel;

import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedItem;
import xmet.tools.metadata.editor.views.scv.utils.RepeatedIC;

/**
 * DC for Repeated Page.
 * @author Nahid Akbar
 */
public class RepeatedPageDC
    extends RepeatedItemDC<Page> {

    // /** The sub panel. */
    // private JPanel subPanel;

    // /** The display panel. */
    // private JPanel displayPanel;

    /**
     * Instantiates a new repeated page dc.
     * @param repeated the repeated
     */
    public RepeatedPageDC(
        final RepeatedItem<Page> repeated) {
        super(repeated);
        rebuildSubPanelWrapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildSubPanelWrapper() {
        RepeatedIC<Page> vRic = getRepeated().getRIC();
        for (int i = 0; i < vRic.getRepeatedItemsCount(); i++) {
            vRic.getRepeatedItems().get(
                i).setTitle(
                String.format(
                    "%1$s %2$d",
                    getRepeated().getLabel(),
                    i + 1));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshPanel() {
        /* nothing to do */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getDisplayPanel() {
        // return displayPanel;
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addItem() {
        final boolean addItem = super.addItem();
        pagesChangedCallback();
        getRepeated().getRIC().reValidate();
        return addItem;
    }

    /**
     * Removes the item.
     * @param panel the panel
     */
    public void removeItem(
        final JPanel panel) {
        int current = getSubPanels().indexOf(
            panel);
        if ((current >= 0)
            && (current < getRepeated().getRIC().getRepeatedItemsCount())) {
            current = (current - 1);
            super.removeItem(current + 1);
            pagesChangedCallback();
            getRepeated().getRIC().reValidate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSubPanel(
        final JPanel pop,
        final int index) {

        final JPanel buttonPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        final Object[] params = {};
        buttonPanel.add(SwingUtils.BUTTON.getNewV(
            String.format(
                "Add new %1$s",
                getRepeated().getLabel()),
            getAddIcon(),
            new ClassMethodCallback(
                this,
                "addItem",
                params)));
        final Object[] params1 = {
            pop
        };
        buttonPanel.add(SwingUtils.BUTTON.getNewV(
            String.format(
                "Remove this %1$s",
                getRepeated().getLabel()),
            getRemoveIcon(),
            new ClassMethodCallback(
                this,
                "removeItem",
                params1)));
        SwingUtils.GridBag.add(
            pop,
            buttonPanel,
            "x=0;w=2;y=-1;h=1;");
        super.addSubPanel(
            pop,
            index);
    }

    /**
     * Pages changed callback.
     */
    public void pagesChangedCallback() {
        getRepeated().getRIC().getSheet().getDc().recachePages();
    }

}
