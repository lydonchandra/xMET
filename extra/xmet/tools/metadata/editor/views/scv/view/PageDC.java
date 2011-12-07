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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import n.ui.SwingUtils;
import xmet.tools.metadata.editor.views.scv.impl.Page;

/**
 * DC for Pages.
 * @author Nahid Akbar
 */
public class PageDC
    extends DisplayContext {

    /** The display panel. */
    private JPanel displayPanel;

    /**
     * The page.
     */
    private Page page;

    /**
     * Gets the page.
     * @return the page
     */
    public Page getPage() {
        return page;
    }

    /**
     * Sets the page.
     * @param aPage the new page
     */
    public void setPage(
        final Page aPage) {
        this.page = aPage;
    }

    /**
     * Instantiates a new page display context.
     * @param aPage the page
     */
    public PageDC(
        final Page aPage) {
        super();
        this.page = aPage;
        rebuildDisplayPanel();
    }

    /**
     * Instantiates a new page display context.
     * @param aPage the page
     * @param panel the panel
     */
    public PageDC(
        final Page aPage,
        final JPanel panel) {
        // CHECKSTYLE OFF: MagicNumber
        this.page = aPage;
        displayPanel = SwingUtils.BorderLayouts.getNew();
        JScrollPane jsp;
        jsp = new JScrollPane(
            panel);
        displayPanel.add(jsp);
        jsp.setBorder(BorderFactory.createEmptyBorder(
            5,
            5,
            5,
            5));
        // CHECKSTYLE ON: MagicNumber
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
    public void rebuildDisplayPanel() {
        new PanelBuilderUtil(
            page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshPanel() {
        displayPanel.revalidate();
    }
}
