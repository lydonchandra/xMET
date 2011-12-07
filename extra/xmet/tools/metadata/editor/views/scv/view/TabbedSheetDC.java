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

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * This is a DC for a Sheet which shows multiple tabs.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class TabbedSheetDC
    extends SheetDC {

    /**
     * The Class TabbedSDCTab.
     */
    private static final class TabbedSDCTab
        extends JTabbedPane {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isFocusTraversable() {
            return true;
        }
    }

    /** The pages tabs. */
    private final JTabbedPane pagesTabs;

    /**
     * Instantiates a new tabbed sheet display context.
     * @param sheet the sheet
     */
    public TabbedSheetDC(
        final Sheet sheet) {
        // CHECKSTYLE OFF: MagicNumber
        super(sheet, SwingUtils.BorderLayouts.getNew());
        pagesTabs = new TabbedSDCTab();
        SwingUtils.FONT.increaseFontSize(
            pagesTabs,
            4.0f);
        for (final Page page : sheet.getItems()) {
            if (page.isVisible()
                && (page.getDC() != null)) {
                /* page.displayContext = new PageDisplayContext(page); */
                pagesTabs.add(
                    page.getTitle(),
                    page.getDC().getDisplayPanel());
            }
        }
        getDisplayPanel().add(
            pagesTabs);
        if (sheet.getIc().getClient() != null) {
            final JPanel buttonsPanel =
                SwingUtils.BoxLayouts.getHorizontalPanel();
            final Object[] params = {};
            buttonsPanel.add(SwingUtils.BUTTON.getNewV(
                "Back",
                sheet.getIc().getClient().getResources().getImageIconResource(
                    "images/toolbar.common.previousPage.png"),
                new ClassMethodCallback(
                    this,
                    "previousPage",
                    params)));
            final Object[] params1 = {};
            buttonsPanel.add(SwingUtils.BUTTON.getNewV(
                "Next",
                sheet.getIc().getClient().getResources().getImageIconResource(
                    "images/toolbar.common.nextPage.png"),
                new ClassMethodCallback(
                    this,
                    "nextPage",
                    params1)));
            getDisplayPanel().add(
                buttonsPanel,
                BorderLayout.SOUTH);
        }
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextPage() {
        if (pagesTabs != null) {
            final int selected = pagesTabs.getSelectedIndex();
            if (selected < pagesTabs.getTabCount() - 1) {
                pagesTabs.setSelectedIndex(selected + 1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void previousPage() {
        if (pagesTabs != null) {
            final int selected = pagesTabs.getSelectedIndex();
            if (selected > 0) {
                pagesTabs.setSelectedIndex(selected - 1);
            }
        }
    }

}
