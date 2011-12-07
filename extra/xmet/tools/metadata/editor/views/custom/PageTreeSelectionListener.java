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

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 * The listener interface for receiving pageTreeSelection events. The class that
 * is interested in processing a pageTreeSelection event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's <code>addPageTreeSelectionListener</code>
 * method. When the pageTreeSelection event occurs, that object's appropriate
 * method is invoked.
 * @author Shaan
 */
public class PageTreeSelectionListener
    implements
    TreeSelectionListener {

    /** The view. */
    private View view;

    /** The view2. */
    private CustomView view2;

    /* JTree tree; */

    /**
     * Instantiates a new page tree selection listener.
     * @param aView the view
     */
    public PageTreeSelectionListener(
        final View aView) {
        this.view = aView;
        /* this.tree = tree; */
    }

    /**
     * Instantiates a new page tree selection listener.
     * @param customView the custom view
     */
    public PageTreeSelectionListener(
        final CustomView customView) {
        view2 = customView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(
        final TreeSelectionEvent arg0) {
        if (view != null) {
            final TreePath treePath = arg0.getPath();
            /* System.out.println("path count=" + treePath.getPathCount()); */
            final Object object = treePath.getLastPathComponent();
            if (object instanceof Page) {
                final Page page = (Page) object;
                view.getEditorPane().setViewportView(
                    page.getPanel());
            }
        } else {
            final TreePath treePath = arg0.getPath();
            /* System.out.println("path count=" + treePath.getPathCount()); */
            final Object object = treePath.getLastPathComponent();
            if (object instanceof Page) {
                final Page page = (Page) object;
                view2.getEditorPane().setViewportView(
                    page.getPanel());
                view2.updateSelected();
            }
        }
    }

}
