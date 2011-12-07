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
import java.util.Stack;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.SwingUtils.COMPONENT;
import n.ui.SwingUtils.GridBag;
import xmet.tools.metadata.editor.views.scv.impl.Choices;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.EditorType;
import xmet.tools.metadata.editor.views.scv.impl.Group;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.LabeledGroup;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;
import xmet.tools.metadata.editor.views.scv.utils.DefaultModelVisitor;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;

/**
 * Utility is for building panels of initialized SCV Sheet Model.
 * @author Nahid Akbar
 */
public class PanelBuilderUtil
    extends DefaultModelVisitor {

    /**
     * The sheet.
     * @param sheet the sheet
     */
    // Sheet sheet;

    /**
     * Instantiates a new panel builder.
     * @param sheet the sheet
     */
    public PanelBuilderUtil(
        final Sheet sheet) {
        super();
        // this.sheet = sheet;
        SCVUtils.accept(
            sheet,
            this);
    }

    /**
     * Instantiates a new panel builder.
     * @param page the page
     */
    public PanelBuilderUtil(
        final Page page) {
        Reporting.logUnexpected();
        SCVUtils.accept(
            page,
            this);
    }

    /**
     * Instantiates a new panel builder.
     * @param group the group
     */
    public PanelBuilderUtil(
        final RepeatedGroup group) {
        SCVUtils.accept(
            group,
            this);
    }

    /**
     * Instantiates a new panel builder.
     * @param item the item
     */
    public PanelBuilderUtil(
        final ModelItem item) {
        SCVUtils.accept(
            item,
            this);
    }

    /**
     * Instantiates a new panel builder util.
     * @param <E> the element type
     * @param group the group
     */
    public <E> PanelBuilderUtil(
        final RepeatedItem<E> group) {
        SCVUtils.accept(
            group,
            this);
    }

    /**
     * Instantiates a new panel builder util.
     * @param choices the choices
     */
    public PanelBuilderUtil(
        final Choices choices) {
        SCVUtils.accept(
            choices,
            this);
    }

    /**
     * The panel stack.
     */
    private final Stack<JPanel> panelStack = new Stack<JPanel>();

    /**
     * The panel row stack.
     */
    private final Stack<Integer> panelRowStack = new Stack<Integer>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitSheet(
        final Sheet sheet) {
        super.preVisitSheet(sheet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitSheet(
        final Sheet sheet) {
        super.postVisitSheet(sheet);
        if (sheet.getDc() != null) {
            sheet.getDc().stopValidationUpdateThread();
        }
        sheet.setDc(new TreeSDC(
            sheet));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {
        visitItem(item);
        super.postVisitItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        visitItem(item);
        super.postVisitCompositeItem(item);
    }

    /**
     * Visit item.
     * @param item the item
     */
    private void visitItem(
        final Item item) {
        if ((item.getDc() == null)
            && (item.getType() != EditorType.Unspecified)) {

            JLabel itemLabel = null;
            if (item.getType() != EditorType.Label
                && (item.getTitle() != null || item.getDescription() != null)) {
                /* Labels are wide */
                itemLabel = DisplayStyle.getStyle().getFieldLabel(
                    item.getTitle(),
                    item.getDescription());
            }
            String itemHover;

            if ((item.getHover() != null)
                && (item.getHover().trim().length() > 0)) {
                itemHover = DisplayStyle.getStyle().formatHoverText(
                    item.getHover().trim());
            } else if ((item.getDescription() != null)
                && (item.getDescription().trim().length() > 0)) {
                itemHover = DisplayStyle.getStyle().formatHoverText(
                    item.getDescription().trim());
            } else {
                itemHover = "";
            }

            if (item.getIc() != null) {
                item.setDc(new ItemDC(
                    item,
                    itemLabel,
                    itemHover,
                    item.getIc().getContext()));
                item.getDc().loadValue();
                item.getDc().onLoadUpdate();
            }
        }
        if ((item.getDc() != null)
            && item.isVisible()
            && !panelStack.empty()) {
            String span;
            String wy;
            try {
                String varString = null;
                if (item.getDc().getPreferredColumnSpan() <= 1) {
                    varString = "h";
                } else {
                    varString = "b";
                }
                span = varString;
                wy = "0";
                if (item.getDc().hasItemLabel()) {
                    addToGridbagPanelWide(
                        panelStack.peek(),
                        item.getDc().getControl(),
                        getRowIncrement(),
                        span,
                        wy);
                } else {
                    addToGridbagPanelNormal(
                        panelStack.peek(),
                        item.getDc().getItemLabel(),
                        item.getDc().getControl(),
                        getRowIncrement(),
                        span,
                        wy);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitLabeledGroup(
        final LabeledGroup visitor) {
        super.preVisitLabeledGroup(visitor);
        panelStack.push(GridBag.getNew());
        panelRowStack.push(Integer.valueOf(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitLabeledGroup(
        final LabeledGroup item) {
        Component old = null;
        if (item.getDC() != null) {
            old = item.getDC().getDisplayPanel();
        }

        item.setDc(new LabeledGroupDC(
            item,
            panelStack.pop(),
            DisplayStyle.getStyle().formatGroupHeader(
                item.getLabel())));
        panelRowStack.pop();
        if (item.isVisible()) {
            if (panelStack.empty()) {
                COMPONENT.replace(
                    old,
                    item.getDC().getDisplayPanel());
            } else {
                addToGridbagPanelWide(
                    panelStack.peek(),
                    item.getDC().getDisplayPanel(),
                    getRowIncrement(),
                    "b",
                    "0");
            }
        }
        super.postVisitLabeledGroup(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        final int ret = super.preVisitRepeatedGroup(repeated);
        if (repeated.getRDC() == null) {
            RepeatedItemDC<Group> varRepeatedItemDC = null;
            if (repeated.isCompact()) {
                varRepeatedItemDC = new CompactRIDC<Group>(
                    repeated);
            } else {
                varRepeatedItemDC = new TrailingRIDC(
                    repeated);
            }
            repeated.setRDC(varRepeatedItemDC);
        } else {
            repeated.getRDC().clearPanels();
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedGroup(
        final RepeatedGroup item) {
        if (panelStack.empty()) {
            /* just rebuilding */
            try {
                item.getRIC().getParent().getDC().refreshPanel();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            addToGridbagPanelWide(
                panelStack.peek(),
                item.getRDC().getDisplayPanel(),
                getRowIncrement(),
                "b",
                "0");
        }
        item.getRDC().rebuildSubPanelWrapper();
        super.postVisitRepeatedGroup(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedGroupIndex(
        final RepeatedGroup repeated,
        final int index) {
        super.preVisitRepeatedGroupIndex(
            repeated,
            index);
        panelStack.push(GridBag.getNew());
        panelRowStack.push(Integer.valueOf(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedGroupIndex(
        final RepeatedGroup repeated,
        final int index) {
        repeated.getRDC().addSubPanel(
            panelStack.pop(),
            index);
        panelRowStack.pop();
        new DataLoaderUtil(
            repeated.getRIC().getRepeatedItems().get(
                index));
        super.postVisitRepeatedGroupIndex(
            repeated,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitRepeatedPage(
        final RepeatedPage repeated) {
        if (repeated.getRDC() == null) {
            repeated.setDc(new RepeatedPageDC(
                repeated));
        } else {
            repeated.getRDC().clearPanels();
        }
        return super.preVisitRepeatedPage(repeated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPage(
        final RepeatedPage item) {
        /* if (panelStack.empty()) { */
        // /*just rebuilding */
        // } else {
        /* addToGridbagPanelWide(panelStack.peek(), item.dc.getPanel(), */
        /* getRowIncrement(), "b", "0"); */
        // }
        item.getRDC().rebuildSubPanelWrapper();
        super.postVisitRepeatedPage(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedPageIndex(
        final RepeatedPage repeated,
        final int index) {
        super.preVisitRepeatedPageIndex(
            repeated,
            index);
        panelStack.push(GridBag.getNew());
        panelRowStack.push(Integer.valueOf(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitRepeatedPageIndex(
        final RepeatedPage repeated,
        final int index) {
        final JPanel pop = panelStack.pop();
        SwingUtils.GridBag.add(
            pop,
            Box.createVerticalGlue(),
            ("x=0;y="
                + panelRowStack.pop() + ";w=2;h=1;f=h;wy=1;"));
        repeated.getRDC().addSubPanel(
            pop,
            index);
        repeated.getRIC().getRepeatedItems().get(
            index).setDc(
            new PageDC(
                repeated.getRIC().getRepeatedItems().get(
                    index),
                repeated.getRDC().getSubPanel(
                    index)));
        /* new DataLoaderUtil(repeated.ic.getRepeatedItems().get(index)); */
        super.postVisitRepeatedPageIndex(
            repeated,
            index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitPage(
        final Page page) {
        super.preVisitPage(page);
        panelStack.push(GridBag.getNew());
        panelRowStack.push(Integer.valueOf(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitPage(
        final Page page) {
        super.postVisitPage(page);
        SwingUtils.GridBag.add(
            panelStack.peek(),
            Box.createVerticalGlue(),
            ("x=0;y="
                + panelRowStack.pop() + ";w=2;h=1;f=h;wy=1;"));
        page.setDc(new PageDC(
            page,
            panelStack.pop()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int preVisitChoices(
        final Choices choices) {
        final int preVisitChoices = super.preVisitChoices(choices);
        panelStack.push(GridBag.getNew());
        panelRowStack.push(Integer.valueOf(0));
        return preVisitChoices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitChoices(
        final Choices choices) {
        if (choices.getDC() == null) {
            choices.setDc(new ChoiceDC(
                choices));
        }
        choices.getDC().setPanel(
            panelStack.pop());
        panelRowStack.pop();
        if (panelStack.empty()) {
            /* just rebuilding */
            try {
                choices.getIc().getParent().getDC().refreshPanel();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            addToGridbagPanelWide(
                panelStack.peek(),
                choices.getDC().getDisplayPanel(),
                getRowIncrement(),
                "b",
                "0");
        }
        super.postVisitChoices(choices);
    }

    /**
     * Gets the row increment.
     * @return the row increment
     */
    private int getRowIncrement() {
        final Integer i = panelRowStack.pop();
        panelRowStack.push(i + 1);
        return i;
    }

    /**
     * private helper method for adding a wide entry for example a label...to a
     * gridbag panel.
     * @param panel the panel
     * @param component the component
     * @param row the row
     * @param span the span
     * @param wy the wy
     */
    private static void addToGridbagPanelWide(
        final JPanel panel,
        final Component component,
        final int row,
        final String span,
        final String wy) {
        final Object[] objects = {
        row,
        span,
        wy
        };
        if (component instanceof JComponent) {
            ((JComponent) component).setAlignmentY(Component.TOP_ALIGNMENT);
        }
        SwingUtils.GridBag.add(
            panel,
            component,
            "y=%1$d;x=0;w=2;h=1;f=%2$s;i=0,0,5,0;wx=1;wy=%3$s",
            objects);
    }

    /**
     * private helper method for adding a label/control pair to a gridbag panel.
     * @param panel the panel
     * @param label the label
     * @param component the component
     * @param row the row
     * @param span the span
     * @param wy the wy
     */
    private static void addToGridbagPanelNormal(
        final JPanel panel,
        final JComponent label,
        final JComponent component,
        final int row,
        final String span,
        final String wy) {
        /* label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); */
        /* component.setBorder(BorderFactory.createLineBorder(Color.BLACK)); */
        component.setAlignmentY(Component.TOP_ALIGNMENT);
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        SwingUtils.GridBag.add(
            panel,
            label,
            "y=%1$d;x=0;w=1;h=1;i=0,10,5,0;a=tl;",
            row);
        SwingUtils.GridBag.add(
            panel,
            component,
            "y=%1$d;x=1;w=1;h=1;f=%2$s;wx=1;wy=%3$s;i=0,0,5,0;a=tl;",
            row,
            span,
            wy);
    }

}
