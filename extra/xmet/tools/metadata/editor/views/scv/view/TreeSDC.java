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
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import n.reporting.Reporting;
import n.ui.SwingUtils;
import n.ui.SwingUtils.BorderLayouts;
import n.ui.TreeModelListenersList;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.ClientContext;
import xmet.tools.metadata.editor.views.scv.impl.Page;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * This display context displays a tree hierarchy view of pages on the left side
 * of the sheet display context and the active page on the right.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class TreeSDC
    extends SheetDC
    implements
    TreeModel,
    TreeCellRenderer,
    TreeSelectionListener {

    /**
     * The Class TSDCTree.
     */
    private static final class TSDCTree
        extends JTree {

        {
            setRootVisible(false);
            getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
            setBackground(Color.WHITE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void setExpandedState(
            final javax.swing.tree.TreePath path,
            final boolean state) {
            super.setExpandedState(
                path,
                true);
        }
    }

    /** The pages tree. */
    private JTree pagesTree;

    /** The internal panel containing both the tree and the page panel. */
    private final JPanel internalPanel = BorderLayouts.getNew();

    /**
     * Instantiates a new tree sdc.
     * @param sheet the sheet
     */
    public TreeSDC(
        final Sheet sheet) {
        super(sheet, SwingUtils.BorderLayouts.getNew(
            2,
            2));
        buildInternalPanel(sheet);
    }

    /**
     * Builds the internal panel.
     * @param sheet the sheet
     */
    private void buildInternalPanel(
        final Sheet sheet) {
        // CHECKSTYLE OFF: MagicNumber
        getDisplayPanel().add(
            internalPanel);
        pagesTree = new TSDCTree();
        tmll = new TreeModelListenersList(
            pagesTree);
        pagesTree.setModel(this);
        pagesTree.setCellRenderer(this);
        pagesTree.getSelectionModel().addTreeSelectionListener(
            this);
        for (int i = 0; i < pagesTree.getRowCount(); i++) {
            pagesTree.expandRow(i);
        }
        pagesTree.setSelectionRow(0);
        SwingUtils.FONT.increaseFontSize(
            pagesTree,
            4.0f);
        getDisplayPanel().add(
            new JScrollPane(
                pagesTree,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
            BorderLayout.WEST);

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
    public void rebuildDisplayPanel() {
        pagesTree.setModel(this);
        pagesTree.setCellRenderer(this);
    };

    /* == TreeModel Implementation == */
    /** The tmll. */
    private TreeModelListenersList tmll;

    /* helper methods */
    /**
     * Gets the child count h.
     * @param node the node
     * @return the child count h
     */
    private int getChildCountH(
        final ParentItem node) {
        int count = 0;
        for (final Object iog : node.getChildren()) {
            if (iog instanceof RepeatedPage) {
                if (((RepeatedPage) iog).isVisible()) {
                    final ArrayList<Page> repeatedItems =
                        ((RepeatedPage) iog).getRIC().getRepeatedItems();
                    count += repeatedItems.size();
                }
            } else if (iog instanceof Page) {
                if (((Page) iog).isVisible()) {
                    count++;
                }
            }
            // else {
            //
            // }
        }
        return count;
    }

    /**
     * Gets the child h.
     * @param node the node
     * @param index the index
     * @return the child h
     */
    private Object getChildH(
        final ParentItem node,
        final int index) {
        int count = 0;
        for (final Object iog : node.getChildren()) {
            if (iog instanceof RepeatedPage) {
                if (((RepeatedPage) iog).isVisible()) {
                    final ArrayList<Page> repeatedItems =
                        ((RepeatedPage) iog).getRIC().getRepeatedItems();
                    for (int i = 0; i < repeatedItems.size(); i++) {
                        if (count == index) {
                            return repeatedItems.get(i);
                        } else {
                            count++;
                        }
                    }
                }
            } else if (iog instanceof Page) {
                if (((Page) iog).isVisible()) {
                    if (count == index) {
                        return iog;
                    } else {
                        count++;
                    }
                }
            }
        }
        Reporting.logUnexpected();
        return node.getChildren().getItem(
            index);
    }

    /**
     * Gets the index of child h.
     * @param node the node
     * @param arg1 the arg1
     * @return the index of child h
     */
    private int getIndexOfChildH(
        final ParentItem node,
        final Object arg1) {
        int count = 0;
        for (final Object iog : node.getChildren()) {
            if (iog instanceof RepeatedPage) {
                if (((RepeatedPage) iog).isVisible()) {
                    final ArrayList<Page> repeatedItems =
                        ((RepeatedPage) iog).getRIC().getRepeatedItems();
                    for (int i = 0; i < repeatedItems.size(); i++) {
                        if (iog == arg1) {
                            return count;
                        } else {
                            count++;
                        }
                    }
                }
            } else if (iog instanceof Page) {
                if (((Page) iog).isVisible()) {
                    if (iog == arg1) {
                        return count;
                    } else {
                        count++;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTreeModelListener(
        final TreeModelListener arg0) {
        tmll.addTreeModelListener(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getChild(
        final Object node,
        final int index) {
        if (node instanceof Sheet) {
            return getChildH(
                (Sheet) node,
                index);
        } else if (node instanceof Page) {
            return getChildH(
                (Page) node,
                index);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChildCount(
        final Object node) {
        if (node instanceof Sheet) {
            return getChildCountH((Sheet) node);
        } else if (node instanceof Page) {
            return getChildCountH((Page) node);
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndexOfChild(
        final Object node,
        final Object arg1) {
        if (node instanceof Sheet) {
            return getIndexOfChildH(
                (Sheet) node,
                arg1);
        } else if (node instanceof Page) {
            return getIndexOfChildH(
                (Page) node,
                arg1);
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRoot() {
        return getSheet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf(
        final Object node) {
        // if (node instanceof Sheet) {
        // return false;
        // } else if (node instanceof Page) {
        // return false;
        // } else {
        // return true;
        // }
        // The above code has been simplified as follows, to satisfy
        // Checkstyle.
        return !((node instanceof Sheet) || (node instanceof Page));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTreeModelListener(
        final TreeModelListener arg0) {
        tmll.removeTreeModelListener(arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueForPathChanged(
        final TreePath arg0,
        final Object arg1) {
        Reporting.logUnexpected();
    }

    /* == TreeModel Implementation == */
    /** The label. */
    private final JLabel label = new JLabel();

    /** The valid page icon. */
    private static Icon validPageIcon = null;

    /** The invalid page icon. */
    private static Icon invalidPageIcon = null;

    /** The background color. */
    private static Color backgroundColor;

    /** The selection background color. */
    private static Color selectionBackgroundColor;

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(
        final JTree tree,
        final Object node,
        final boolean selected,
        final boolean expanded,
        final boolean leaf,
        final int row,
        final boolean hasFocus) {

        if (validPageIcon == null) {
            init(getSheet().getIC().getContext());
        }
        if (node instanceof Sheet) {
            label.setText(((Sheet) node).getTitle());
            label.setIcon(validPageIcon);
        } else if (node instanceof Page) {
            label.setText(((Page) node).getTitle());
            if ((((Page) node).getDC() != null)
                && ((Page) node).getDC().isValid()) {
                label.setIcon(validPageIcon);
            } else {
                label.setIcon(invalidPageIcon);
                // if ((((Page) node).getDC() != null)) {
                // label
                // .setText(((Page) node).getDC().getValidationErrorMessage());
                // }
            }
        } else {
            label.setText(String.format(
                "%1$s",
                node));
            label.setIcon(validPageIcon);
        }
        if (selected) {
            label.setBackground(selectionBackgroundColor);
        } else {
            label.setBackground(backgroundColor);
        }
        return label;
    }

    /**
     * Inits the.
     * @param context the context
     */
    private static void init(
        final ClientContext context) {
        // CHECKSTYLE OFF: MagicNumber
        validPageIcon = context.getResources().getImageIconResourceResize(
            "images/scv.validPage.png",
            16,
            16);
        invalidPageIcon = context.getResources().getImageIconResourceResize(
            "images/scv.invalidPage.png",
            16,
            16);
        backgroundColor = UIManager.getColor("Tree.background");
        selectionBackgroundColor =
            UIManager.getColor("Tree.selectionBackground");
        // CHECKSTYLE ON: MagicNumber
    }

    /* == TreeSelectionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(
        final TreeSelectionEvent arg0) {
        TreePath tp = pagesTree.getSelectionPath();
        if (tp == null) {
            tp = pagesTree.getPathForRow(0);
            pagesTree.setSelectionPath(tp);
            return;
        } else {
            try {
                internalPanel.removeAll();
                final Object node = tp.getLastPathComponent();
                // if (node instanceof Sheet) {
                // /* internalPanel.add(((Sheet) node).getDC().getPanel(), */
                // /* BorderLayout.CENTER); */
                // } else
                if (node instanceof Page) {
                    final JScrollPane varJScrollPane = new JScrollPane(
                        ((Page) node).getDC().getDisplayPanel(),
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    internalPanel.add(
                        varJScrollPane,
                        BorderLayout.CENTER);
                }
                // else {
                // // Reporting.unimplemented();
                // }
                /* Reporting.log("node %1$s", node); */
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        getDisplayPanel().repaint();
        getDisplayPanel().revalidate();
        /* Reporting.log("page changed"); */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextPage() {
        if (pagesTree != null) {
            final int selected =
                pagesTree.getRowForPath(pagesTree.getSelectionPath());
            if (selected + 1 < pagesTree.getRowCount()) {
                pagesTree.setSelectionRow(selected + 1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void previousPage() {
        if (pagesTree != null) {
            final int selected =
                pagesTree.getRowForPath(pagesTree.getSelectionPath());
            if (selected > 0) {
                pagesTree.setSelectionRow(selected - 1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected synchronized void updateValidation() {
        super.updateValidation();
        if (pagesTree != null) {
            pagesTree.repaint();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recachePages() {
        final int row = pagesTree.getSelectionRows()[0];
        pagesTree.setRootVisible(true);
        tmll.rowChildrenChanged(
            pagesTree,
            0);
        /* pagesTree.setModel(this); */
        /* pagesTree.setCellRenderer(this); */
        for (int i = 0; i < pagesTree.getRowCount(); i++) {
            pagesTree.expandRow(i);
        }
        pagesTree.setRootVisible(false);
        pagesTree.setSelectionRow(row);
    }
}
