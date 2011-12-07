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
package xmet.tools.metadata.editor.views.custom;

import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * PageTreeModel.
 * @author Shaan
 */
public class PageTreeModel
    implements
    TreeModel {

    /** The root. */
    private final Page root;

    /** The tree. */
    private JTree tree;

    /** The listener list. */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * Instantiates a new page tree model.
     * @param aRoot the root
     */
    public PageTreeModel(
        final Page aRoot) {
        this.root = aRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTreeModelListener(
        final TreeModelListener arg0) {
        getListenerList().add(
            TreeModelListener.class,
            arg0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getChild(
        final Object arg0,
        final int arg1) {
        if (arg0 instanceof Page) {
            final Page page = (Page) arg0;
            return page.get(arg1);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChildCount(
        final Object arg0) {
        if (arg0 instanceof Page) {
            final Page page = (Page) arg0;
            return page.getChildren().size();
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndexOfChild(
        final Object arg0,
        final Object arg1) {
        if (arg0 instanceof Page) {
            final Page page = (Page) arg0;
            return page.getChildren().indexOf(
                arg1);
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getRoot() {
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf(
        final Object arg0) {
        if (arg0 instanceof Page) {
            final Page page = (Page) arg0;
            return (page.getChildren().size() < 1);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTreeModelListener(
        final TreeModelListener arg0) {
        getListenerList().remove(
            TreeModelListener.class,
            arg0);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueForPathChanged(
        final TreePath arg0,
        final Object arg1) {
        /* TODO Auto-generated method stub */

    }

    /**
     * Sets the tree.
     * @param aTree the new tree
     */
    public void setTree(
        final JTree aTree) {
        this.tree = aTree;
    }

    /**
     * Structure changed.
     */
    public void structureChanged() {
        if (tree == null) {
            return;
        }
        final TreePath treePath = tree.getSelectionPath();
        if (treePath == null) {
            return;
        }
        final Object[] listeners = getListenerList().getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i--) {
            if (listeners[i] == TreeModelListener.class) {
                /* Lazily create the event: */
                if (e == null) {
                    e = new TreeModelEvent(
                        treePath.getLastPathComponent(),
                        treePath,
                        new int[] {},
                        new Object[] {});
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * Gets the listener list.
     * @return the listener list
     */
    public EventListenerList getListenerList() {
        return listenerList;
    }

    /**
     * Sets the listener list.
     * @param aListenerList the new listener list
     */
    public void setListenerList(
        final EventListenerList aListenerList) {
        listenerList = aListenerList;
    }

}
