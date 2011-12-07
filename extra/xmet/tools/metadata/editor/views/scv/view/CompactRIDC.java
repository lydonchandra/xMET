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
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import n.ui.SwingUtils;
import n.ui.SwingUtils.GridBag;
import n.ui.patterns.callback.ClassMethodCallback;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import xmet.tools.metadata.editor.views.scv.impl.RepeatedItem;

import com.jhlabs.image.BlurFilter;

/**
 * Compact Repeated item Display Context - displays stuff compactly inside a
 * page - somewhat similar to card layout stuff with buttons.
 * @param <E> the element type
 * @author Nahid Akbar
 */
public class CompactRIDC<E>
    extends RepeatedItemDC<E> {

    /** The content sub panel. */
    private JPanel contentSubPanel;

    /** The display panel. */
    private JPanel displayPanel;

    /** The state display label. */
    private JLabel stateDisplayLabel;

    /** The lockable. */
    private LockableUI lockable;

    /** The current active panel. */
    private int currentActivePanel = -1;

    /* == DC Implementation == */
    /**
     * Instantiates a new compact ridc.
     * @param repeated the repeated
     */
    public CompactRIDC(
        final RepeatedItem<E> repeated) {
        super(repeated);
        rebuildSubPanelWrapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildSubPanelWrapper() {
        // CHECKSTYLE OFF: MagicNumber
        if (contentSubPanel == null) {
            contentSubPanel = GridBag.getNew();
            displayPanel = SwingUtils.BorderLayouts.getNew();
            displayPanel.add(contentSubPanel);
        }

        contentSubPanel.removeAll();

        final int varRepeatedItemsCount =
            getRepeated().getRIC().getRepeatedItemsCount();
        if (varRepeatedItemsCount > 0) {
            if (currentActivePanel < 0) {
                currentActivePanel = 0;
            }
            if (currentActivePanel >= varRepeatedItemsCount) {
                currentActivePanel = (varRepeatedItemsCount - 1);
            }
        }

        stateDisplayLabel = new JLabel(
            getLabelString(getRepeated()));

        final JPanel labelPanel = SwingUtils.BoxLayouts.getHorizontalPanel();

        labelPanel.add(stateDisplayLabel);

        SwingUtils.GridBag.add(
            labelPanel,
            Box.createHorizontalGlue(),
            "w=rem;");
        final Object[] params = {};
        labelPanel.add(SwingUtils.BUTTON.getNew(
            getAddIcon(),
            new ClassMethodCallback(
                this,
                "addItem",
                params)));
        final Object[] params1 = {};
        labelPanel.add(SwingUtils.BUTTON.getNew(
            getRemoveIcon(),
            new ClassMethodCallback(
                this,
                "removeItem",
                params1)));
        final Object[] params2 = {};
        labelPanel.add(SwingUtils.BUTTON.getNew(
            getPrevIcon(),
            new ClassMethodCallback(
                this,
                "prevItem",
                params2)));
        final Object[] params3 = {};
        labelPanel.add(SwingUtils.BUTTON.getNew(
            getNextIcon(),
            new ClassMethodCallback(
                this,
                "nextItem",
                params3)));

        SwingUtils.GridBag.add(
            contentSubPanel,
            labelPanel,
            "w=rem;f=h;wx=0;a=l;");

        lockable = new LockableUI(
            new BufferedImageOpEffect(
                new BlurFilter()));

        JXLayer<JComponent> editorControlPanel;
        editorControlPanel = new JXLayer<JComponent>(
            getCurrentPanel());
        final JComponent component =

        new JScrollPane(
            editorControlPanel);

        component.setBorder(BorderFactory.createEmptyBorder(
            0,
            15,
            0,
            10));
        SwingUtils.GridBag.add(
            contentSubPanel,
            component,
            "w=rem;f=b;wx=1;wy=1;");
        editorControlPanel.setUI(lockable);

        if (stateDisplayLabel != null) {
            stateDisplayLabel.setText(DisplayStyle
                .getStyle()
                .formatGroupHeader(
                    getLabelString(getRepeated())));
        }
        if (lockable != null) {
            lockable.setLocked(varRepeatedItemsCount == 0);
        }
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
    public void refreshPanel() {
        try {
            displayPanel.revalidate();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /* == Misc Implementation == */

    /**
     * Gets the label string.
     * @param repeatedGroup the repeated group
     * @return the label string
     */
    public String getLabelString(
        final RepeatedItem<E> repeatedGroup) {
        return String.format(
            "%1$s (%2$d of %3$d)",
            repeatedGroup.getLabel(),
            currentActivePanel + 1,
            repeatedGroup.getRIC().getRepeatedItemsCount());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addItem() {
        if (super.addItem()) {
            final int varRepItemsCount =
                getRepeated().getRIC().getRepeatedItemsCount();
            if (currentActivePanel == varRepItemsCount - 1) {
                currentActivePanel = (currentActivePanel + 1);
            }
            refreshPanel();
            return true;
        }
        return false;
    }

    /**
     * Removes the item.
     */
    public void removeItem() {
        final int varRepItemsCount =
            getRepeated().getRIC().getRepeatedItemsCount();
        if ((currentActivePanel >= 0)
            && (currentActivePanel < varRepItemsCount)) {
            currentActivePanel = (currentActivePanel - 1);
            super.removeItem(currentActivePanel + 1);
            refreshPanel();
        }
    }

    /**
     * Next item.
     */
    public void nextItem() {
        currentActivePanel = (currentActivePanel + 1);
        rebuildDisplayPanel();
        refreshPanel();
    }

    /**
     * Prev item.
     */
    public void prevItem() {
        currentActivePanel = (currentActivePanel - 1);
        rebuildDisplayPanel();
        refreshPanel();
    }

    /**
     * Gets the current panel.
     * @return the current panel
     */
    private JPanel getCurrentPanel() {
        final int varSize = getSubPanels().size();
        if ((varSize == 0)
            || (varSize <= currentActivePanel)) {
            return new JPanel();
        }
        return getSubPanels().get(
            currentActivePanel);
    }

}
