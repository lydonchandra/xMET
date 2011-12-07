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
package xmet.tools;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import n.ui.SwingUtils;
import n.ui.patterns.callback.RunnableCallback;
import n.ui.patterns.propertySheet.PSEExtraParams;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.ClientContext;

/**
 * This is a PSE that allows the selction of a tool.
 * @author Nahid Akbar
 */
@PSEExtraParams({
    "client"
})
@SuppressWarnings("serial")
public class ToolSelectionPropertySheetEditor
    extends JPanel
    implements
    PropertySheetEditor,
    Runnable {

    /* == Parameter client == */
    /** The context. */
    private ClientContext context;

    /**
     * Gets the context.
     * @return the context
     */
    public ClientContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     * @param client the new context
     */
    public void setContext(
        final ClientContext client) {
        this.context = client;
    }

    /* == build panel == */
    /** The value. */
    private final JTextField value;

    /** The select button. */
    private final JButton selectButton;

    /**
     * Instantiates a new tool selection property sheet editor.
     */
    public ToolSelectionPropertySheetEditor() {
        value = new JTextField();
        selectButton = SwingUtils.BUTTON.getNew(
            "set",
            new RunnableCallback(
                this));
        setLayout(new BorderLayout());
        add(value);
        add(
            selectButton,
            BorderLayout.EAST);
    }

    /* == PSE Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object aValue,
        final PropertySheetItem item) {
        this.value.setText((String) aValue);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        return value.getText();
    }

    /* == Runable Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        new ToolSelectionDialog();
    }

    /* == The editor dialog == */
    /**
     * The editor dialog.
     */
    private class ToolSelectionDialog
        extends JDialog
        implements
        ListCellRenderer,
        ListSelectionListener {

        /** The tools. */
        private final Tool[] tools;

        /** The list. */
        private final JList list;

        /** The image. */
        private final JLabel image = new JLabel();

        /**
         * Instantiates a new tool selection dialog.
         */
        public ToolSelectionDialog() {
            // CHECKSTYLE OFF: MagicNumber
            setTitle("select an image and close this window");
            tools = context.getTools().getAsArray();
            SwingUtils.WINDOW.centreSizeWindow(
                this,
                200);
            final JPanel panel = SwingUtils.GridBag.getNew();
            list = new JList(
                new ListModelImpl());
            SwingUtils.GridBag.add(
                panel,
                new JScrollPane(
                    list),
                "w=rem;i=10,10,10,10;wx=1;wy=1;f=b;");
            setContentPane(panel);
            list.setCellRenderer(this);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.getSelectionModel().addListSelectionListener(
                this);
            setModal(true);
            setVisible(true);
            // CHECKSTYLE ON: MagicNumber
        }

        /**
         * ListModel Implementation.
         */
        private class ListModelImpl
            implements
            ListModel {

            /* == == */
            /**
             * {@inheritDoc}
             */
            @Override
            public Object getElementAt(
                final int index) {
                return tools[index];
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void addListDataListener(
                final ListDataListener l) {
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void removeListDataListener(
                final ListDataListener l) {
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int getSize() {
                return tools.length;
            }
        }

        /* == ListCellRenderer Implementation == */
        /**
         * {@inheritDoc}
         */
        @Override
        public Component getListCellRendererComponent(
            final JList aList,
            final Object aValue,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
            if (aValue instanceof Tool) {
                image.setText(((Tool) aValue).getName());
                image.setOpaque(true);
            }
            if (isSelected) {
                image.setBackground(UIManager
                    .getColor("Tree.selectionBackground"));
            } else {
                image.setBackground(UIManager.getColor("Tree.background"));
            }
            return image;
        }

        /* == ListSelectionListener Implementation == */
        /**
         * {@inheritDoc}
         */
        @Override
        public void valueChanged(
            final ListSelectionEvent e) {
            final Tool f = (Tool) list.getSelectedValue();
            if (f != null) {
                final String name = f.getName();
                value.setText(name);
            }

        }
    }

}
