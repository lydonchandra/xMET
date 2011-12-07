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
package xmet.tools.metadata.editor.views.generated;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.profiles.model.Repeated;

/**
 * Editor class for Repeated items.
 * @author Nahid Akbar
 */
public class RepeatedGeneratedViewEditor
    extends GeneratedViewEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The action listener. */
    private final ActionListener actionListener;

    /** The item. */
    private final Repeated item;

    /** The list. */
    private final JList list;

    /**
     * Instantiates a new repeated generated view editor.
     * @param aItem the item
     * @param aActionListener the action listener
     */
    public RepeatedGeneratedViewEditor(
        final Repeated aItem,
        final ActionListener aActionListener) {
        this.actionListener = aActionListener;
        this.item = aItem;
        setLayout(new GridBagLayout());

        // {

        list = new JList(
            getListModel());
        final Component component = new JScrollPane(
            list);
        SwingUtils.GridBag.add(
            this,
            component,
            "w=rem;");
        final Object[] params = {};
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "add",
                new ClassMethodCallback(
                    this,
                    "addNewChild",
                    params)),
            "w=rem;");
        final Object[] params1 = {};
        SwingUtils.GridBag.add(
            this,
            SwingUtils.BUTTON.getNew(
                "remove",
                new ClassMethodCallback(
                    this,
                    "removeChild",
                    params1)),
            "w=rem;");
        // }

        // {
        if (aItem.isValid()) {
            SwingUtils.GridBag.add(
                this,
                new JLabel(
                    "Validation: Valid"),
                "w=rem;f=h;");
        } else {
            SwingUtils.GridBag.add(
                this,
                new JLabel(
                    String.format(
                        "Validation: Invalid - %1$s",
                        aItem.getValidationError())),
                "w=rem;f=h;");
        }
        // }

        SwingUtils.GridBag.add(
            this,
            Box.createGlue(),
            "w=rem;f=b;wx=1;wy=1;");
        setBorder(BorderFactory.createTitledBorder("Repeated Item"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void stopEditting() {

    }

    /**
     * Toggle.
     */
    public void toggle() {
        actionListener.actionPerformed(null);
    }

    /**
     * Gets the list model.
     * @return the list model
     */
    private ListModel getListModel() {
        return new ListModel() {

            @Override
            public void removeListDataListener(
                final ListDataListener arg0) {
                /* TODO Auto-generated method stub */

            }

            @Override
            public int getSize() {
                return item.entityCount();
            }

            @Override
            public Object getElementAt(
                final int arg0) {
                return item.getEntityByIndex(
                    arg0).getQualifiedName()
                    + " "
                    + (arg0 + 1);
            }

            @Override
            public void addListDataListener(
                final ListDataListener arg0) {
                /* TODO Auto-generated method stub */

            }
        };
    }

    /**
     * Adds the new child.
     */
    public void addNewChild() {
        (item).addNewEntity();
        actionListener.actionPerformed(null);
    }

    /**
     * Removes the child.
     */
    public void removeChild() {
        final int index = list.getSelectedIndex();
        if ((index >= 0)
            && (index < item.entityCount())) {
            item.removeEntityByIndex(index);
            actionListener.actionPerformed(null);
        }
    }

}
