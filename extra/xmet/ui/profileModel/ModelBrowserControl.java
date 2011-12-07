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
package xmet.ui.profileModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import n.reporting.Reporting;
import n.ui.GetTreeCellRendererParameters;
import n.ui.SwingUtils;
import n.ui.TreeCellRendererInterceptor;
import n.ui.patterns.callback.ClassMethodCallback;

import org.jdesktop.swingx.JXTree;

import xmet.profiles.model.ChoiceGroup;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.Group;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Optional;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.Settable;

/**
 * A control that shows an instance of a Profile Model for the users
 * graphically.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class ModelBrowserControl
    extends JPanel
    implements
    TreeSelectionListener {

    /**
     * The root.
     */
    private Entity root;

    /**
     * The tree.
     */
    private final JXTree tree;

    /**
     * The add button.
     */
    private final JButton addButton;

    /**
     * The delete button.
     */
    private final JButton deleteButton;

    /**
     * The substitution label.
     */
    private final JLabel substitutionLabel;

    /**
     * The substitution combo box.
     */
    private final JComboBox substitutionComboBox;

    /**
     * The change button.
     */
    private final JButton changeButton;

    /**
     * The choice label.
     */
    private final JLabel choiceLabel;

    /**
     * The choice combo box.
     */
    private final JComboBox choiceComboBox;

    /**
     * The value label.
     */
    private final JLabel valueLabel;

    /** The value editor. */
    private final JTextField valueEditor;

    /** The value set button. */
    private final JButton valueSetButton;

    /** The toolbar. */
    private final JToolBar toolbar;

    /** The evl. */
    private final ArrayList<ModelBrowserSelectionUpdateListener> evl =
        new ArrayList<ModelBrowserSelectionUpdateListener>();

    /** The cell renderer interceptor. */
    private TreeCellRendererInterceptor cellRendererInterceptor;

    /** The scroll pane. */
    private final JScrollPane scrollPane;

    /**
     * Adds the selection update listener.
     * @param listener the listener
     */
    public void addSelectionUpdateListener(
        final ModelBrowserSelectionUpdateListener listener) {
        if (listener != null) {
            evl.add(listener);
        }
    }

    /**
     * Removes the selection update listener.
     * @param listener the listener
     */
    public void removeSelectionUpdateListener(
        final ModelBrowserSelectionUpdateListener listener) {
        if (listener != null) {
            evl.remove(listener);
        }
    }

    /**
     * Notify selection update listeners.
     * @param parent the parent
     * @param newSelected the new selected
     */
    private void notifySelectionUpdateListeners(
        final Object parent,
        final Object newSelected) {
        for (final ModelBrowserSelectionUpdateListener l : evl) {
            l.selectedValueChanged(
                parent,
                newSelected);
        }
    }

    /**
     * Instantiates a new model borwser control.
     * @param model the model
     */
    public ModelBrowserControl(
        final Entity model) {
        super();
        root = model;
        setLayout(new BorderLayout());
        tree = new JXTree(
            new TreeModelImpl()) {

            @Override
            protected void setExpandedState(
                final TreePath path,
                final boolean state) {
                super.setExpandedState(
                    path,
                    true);
            }
        };
        scrollPane = new JScrollPane(
            tree);
        add(
            scrollPane,
            BorderLayout.CENTER);
        tree.expandAll();
        tree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        tree.setCellRenderer(getTreeCellRenderer());

        toolbar = new JToolBar(
            SwingConstants.HORIZONTAL);
        add(
            toolbar,
            BorderLayout.NORTH);

        toolbar.add(new JLabel(
            "Editting Toolbox:"));
        final Object[] params = {};
        addButton = SwingUtils.BUTTON.getNew(
            "Add",
            new ClassMethodCallback(
                this,
                "addButtonCallback",
                params));
        toolbar.add(addButton);
        deleteButton = SwingUtils.BUTTON.getNew(
            "Delete",
            new ClassMethodCallback(
                this,
                "deleteButtonCallback"));
        toolbar.add(deleteButton);
        substitutionLabel = new JLabel(
            "Substitute: ");
        toolbar.add(substitutionLabel);
        substitutionComboBox = new JComboBox();
        toolbar.add(substitutionComboBox);

        choiceLabel = new JLabel(
            "Choice: ");
        toolbar.add(choiceLabel);
        choiceComboBox = new JComboBox();
        toolbar.add(choiceComboBox);
        changeButton = SwingUtils.BUTTON.getNew(
            "Change",
            new ClassMethodCallback(
                this,
                "changeButtonCallback"));
        toolbar.add(changeButton);

        valueLabel = new JLabel(
            "Value: ");
        toolbar.add(valueLabel);
        // CHECKSTYLE OFF: MagicNumber
        valueEditor = new JTextField(
            10);
        // CHECKSTYLE ON: MagicNumber
        toolbar.add(valueEditor);
        final Object[] params3 = {};
        valueSetButton = SwingUtils.BUTTON.getNew(
            "Set",
            new ClassMethodCallback(
                this,
                "setButtonCallback",
                params3));
        toolbar.add(valueSetButton);

        toolbar.add(Box.createHorizontalGlue());

        updateToolbar();
    }

    /**
     * Gets the tree cell renderer.
     * @return the tree cell renderer
     */
    private DefaultTreeCellRenderer getTreeCellRenderer() {
        return new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(
                final JTree aTree,
                final Object value,
                final boolean sel,
                final boolean expanded,
                final boolean leaf,
                final int row,
                final boolean hasFocus) {
                final Component component = super.getTreeCellRendererComponent(
                    aTree,
                    value,
                    sel,
                    expanded,
                    leaf,
                    row,
                    hasFocus);
                if (cellRendererInterceptor != null) {
                    return cellRendererInterceptor
                        .interceptGetTreeCellRenderer(
                            component,
                            new GetTreeCellRendererParameters(
                                aTree,
                                value,
                                sel,
                                expanded,
                                leaf,
                                row,
                                hasFocus));
                }
                return component;
            }
        };
    }

    /**
     * Rebuild panel.
     */
    void rebuildPanel() {

    }

    /**
     * Gets the model.
     * @return the model
     */
    public Entity getModel() {
        return root;
    }

    /**
     * Sets the model.
     * @param model the new model
     */
    public void setModel(
        final Entity model) {
        root = model;
    }

    /**
     * TreeModel implementation for profile model.
     */
    class TreeModelImpl
        implements
        TreeModel {

        /**
         * {@inheritDoc}
         */
        @Override
        public void addTreeModelListener(
            final TreeModelListener l) {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getChild(
            final Object parent,
            final int index) {
            if (parent instanceof ElementDeclaration) {
                final ElementDeclaration elementDeclaration =
                    ((ElementDeclaration) parent);
                int attributes;
                if (elementDeclaration.hasAttributes()) {
                    attributes = elementDeclaration.getAttributes().size();
                } else {
                    attributes = 0;
                }
                if ((index >= 0)
                    && (index < attributes)) {
                    return elementDeclaration
                        .getAttributes()
                        .getAttributeByIndex(
                            index);
                } else {
                    return elementDeclaration.getGroup();
                }
            } else if (parent instanceof Optional) {
                final Optional optional = (Optional) parent;
                return optional.getSetTerm();
            } else if (parent instanceof Settable) {
                return null;
            } else if (parent instanceof ChoiceGroup) {
                final ChoiceGroup group = (ChoiceGroup) parent;
                return group.getChildren().get(
                    group.getSelected());
            } else if (parent instanceof Group) {
                final Group group = (Group) parent;
                return group.getChildren().get(
                    index);
            } else if (parent instanceof Repeated) {
                final Repeated repeated = (Repeated) parent;
                return repeated.getEntityByIndex(index);
            }
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getChildCount(
            final Object parent) {
            if (parent instanceof ElementDeclaration) {
                final ElementDeclaration ed = ((ElementDeclaration) parent);
                int count = 0;
                if (ed.hasAttributes()) {
                    count += ed.getAttributes().size();
                }
                if (ed.hasGroup()) {
                    count++;
                }
                return count;
            } else if (parent instanceof Optional) {
                final Optional optional = (Optional) parent;
                if (optional.isSetTermPresent()) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (parent instanceof Settable) {
                return 0;
            } else if (parent instanceof ChoiceGroup) {
                final ChoiceGroup group = (ChoiceGroup) parent;
                if (group.isSelected()) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (parent instanceof Group) {
                final Group group = (Group) parent;
                return group.getChildren().size();
            } else if (parent instanceof Repeated) {
                final Repeated repeated = (Repeated) parent;
                return repeated.entityCount();
            }
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIndexOfChild(
            final Object parent,
            final Object child) {
            if (parent instanceof ElementDeclaration) {
                final ElementDeclaration elementDeclaration =
                    (ElementDeclaration) parent;
                if (elementDeclaration.getGroup() == child) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (parent instanceof Optional) {
                final Optional optional = (Optional) parent;
                if (optional.getSetTerm() == child) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (parent instanceof Settable) {
                return -1;
            } else if (parent instanceof ChoiceGroup) {
                @SuppressWarnings("unused")
                final ChoiceGroup group = (ChoiceGroup) parent;
                Reporting.logUnexpected();
                return 0;
            } else if (parent instanceof Group) {
                final Group group = (Group) parent;
                return group.getChildren().indexOf(
                    child);
            } else if (parent instanceof Repeated) {
                final Repeated repeated = (Repeated) parent;
                return repeated.indexOfEntity((Entity) child);
            }
            return -1;
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
            final Object node) {
            return getMaximumChildrenCount(node) == 0;
        }

        /**
         * Gets the maximum children count.
         * @param parent the parent
         * @return the maximum children count
         */
        public int getMaximumChildrenCount(
            final Object parent) {
            if (parent instanceof ElementDeclaration) {
                return -1;
            } else if (parent instanceof Optional) {
                return -1;
            } else if (parent instanceof Settable) {
                return 0;
            } else if (parent instanceof Group) {
                return -1;
            } else if (parent instanceof Repeated) {
                return -1;
            }
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeTreeModelListener(
            final TreeModelListener l) {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void valueForPathChanged(
            final TreePath path,
            final Object newValue) {
            Reporting.logUnexpected();
        }

    }

    /* == Tree Selection Listener == */

    /**
     * The selected row.
     */
    private int selectedRow;

    /**
     * The selected item.
     */
    private Object selectedItem;

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(
        final TreeSelectionEvent arg0) {
        if (tree.getSelectionCount() > 0) {
            final Object o = tree.getSelectionPath().getLastPathComponent();
            setSelectedRow(tree.getSelectionRows()[0]);
            if (o != getSelectedItem()) {
                setSelectedItem(o);
                updateToolbar();
                Object varParent = null;
                if (getSelectedRow() == 0) {
                    varParent = null;
                } else {
                    varParent =
                        tree
                            .getSelectionPath()
                            .getParentPath()
                            .getLastPathComponent();
                }
                notifySelectionUpdateListeners(
                    varParent,
                    getSelectedItem());
            }
        }
    }

    /**
     * Update toolbar.
     */
    private void updateToolbar() {
        if ((getSelectedItem() instanceof ElementDeclaration)
            && ((ElementDeclaration) getSelectedItem()).hasSubtitutables()) {
            /* TODO: Fill up substitution label */
            substitutionComboBox.removeAllItems();
            final ElementDeclaration varEd =
                ((ElementDeclaration) getSelectedItem());
            for (final ElementDeclaration ed : varEd
                .getSubstitutables()
                .asEdList()) {
                substitutionComboBox.addItem(ed.getQualifiedName());
                if (((ElementDeclaration) getSelectedItem())
                    .getQualifiedName()
                    .equals(
                        ed.getQualifiedName())) {
                    substitutionComboBox.setSelectedItem(ed.getQualifiedName());
                }
            }
            substitutionLabel.setVisible(true);
            substitutionComboBox.setVisible(true);
        } else {
            substitutionLabel.setVisible(false);
            substitutionComboBox.setVisible(false);
        }
        if ((getSelectedItem() instanceof Optional)
            || (getSelectedItem() instanceof Repeated)) {
            addButton.setVisible(true);
            if (!(getSelectedItem() instanceof Repeated)) {
                deleteButton.setVisible(true);
            } else {
                deleteButton.setVisible(false);
            }
        } else {
            addButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        if (getSelectedItem() instanceof ElementDeclaration) {
            final ElementDeclaration varEd =
                ((ElementDeclaration) getSelectedItem());
            if (varEd.getParent() instanceof Repeated
                || varEd.getParent() instanceof Optional) {
                deleteButton.setVisible(true);
            }
        }

        if (getSelectedItem() instanceof Settable) {
            /* TODO: Initialize editor */
            if (((Settable) getSelectedItem()).getValue() != null) {
                valueEditor.setText(((Settable) getSelectedItem()).getValue());
            } else {
                valueEditor.setText("");
            }
            valueLabel.setVisible(true);
            valueEditor.setVisible(true);
            valueSetButton.setVisible(true);
        } else {
            valueLabel.setVisible(false);
            valueEditor.setVisible(false);
            valueSetButton.setVisible(false);
        }

        if (getSelectedItem() instanceof ChoiceGroup) {
            choiceComboBox.removeAllItems();
            choiceComboBox.addItem("");
            for (final Entity e : ((ChoiceGroup) getSelectedItem())
                .getChildren()) {
                choiceComboBox.addItem(e.getQualifiedName());
            }
            if (((ChoiceGroup) getSelectedItem()).getSelected() < 0) {
                choiceComboBox.setSelectedIndex(0);
            } else {
                choiceComboBox
                    .setSelectedIndex(((ChoiceGroup) getSelectedItem())
                        .getSelected() + 1);
            }

            choiceLabel.setVisible(true);
            choiceComboBox.setVisible(true);
        } else {
            choiceLabel.setVisible(false);
            choiceComboBox.setVisible(false);
        }

        if (choiceComboBox.isVisible()
            || substitutionComboBox.isVisible()) {
            changeButton.setVisible(true);
        } else {
            changeButton.setVisible(false);
        }

    }

    /**
     * Adds the button callback.
     */
    public void addButtonCallback() {
        if (getSelectedItem() != null) {
            if (getSelectedItem() instanceof Optional) {
                if (!((Optional) getSelectedItem()).isSetTermPresent()) {
                    ((Optional) getSelectedItem()).setTermPresent(true);
                }
                tree.setModel(new TreeModelImpl());
                tree.expandAll();
                tree.scrollRowToVisible(getSelectedRow());
                tree.setSelectionRow(getSelectedRow());
                return;
            } else if (getSelectedItem() instanceof Repeated) {
                ((Repeated) getSelectedItem()).addNewEntity();
                tree.setModel(new TreeModelImpl());
                tree.expandAll();
                tree.scrollRowToVisible(getSelectedRow());
                tree.setSelectionRow(getSelectedRow());
                return;
            }
        }
    }

    /**
     * Delete button callback.
     */
    public void deleteButtonCallback() {
        if (getSelectedItem() != null) {
            if (getSelectedItem() instanceof Entity) {
                if (getSelectedItem() instanceof Optional) {
                    ((Optional) getSelectedItem()).setTermPresent(false);
                    tree.setModel(new TreeModelImpl());
                    tree.expandAll();
                    tree.scrollRowToVisible(getSelectedRow());
                    tree.setSelectionRow(getSelectedRow());
                    return;
                } else {
                    final Entity varParent =
                        ((Entity) getSelectedItem()).getParent();
                    if ((varParent instanceof Optional)
                        || (varParent instanceof Repeated)) {
                        if (varParent instanceof Optional) {
                            ((Optional) varParent).setTermPresent(false);
                        } else {
                            ((Repeated) varParent)
                                .removeEntity((Entity) getSelectedItem());
                        }
                        setSelectedRow(tree.getRowForPath(tree.getPathForRow(
                            getSelectedRow()).getParentPath()));
                        tree.setModel(new TreeModelImpl());
                        tree.expandAll();
                        tree.scrollRowToVisible(getSelectedRow());
                        tree.setSelectionRow(getSelectedRow());
                        return;
                    }
                }
            }
            Reporting.logUnexpected(getSelectedItem().toString());
        }
    }

    /**
     * Change button callback.
     */
    public void changeButtonCallback() {
        if (getSelectedItem() != null) {
            boolean vHasSubs = getSelectedItem() instanceof ElementDeclaration
                && ((ElementDeclaration) getSelectedItem()).hasSubtitutables();
            if (vHasSubs) {
                final String newValue =
                    (String) substitutionComboBox.getSelectedItem();
                final Entity substituted = ModelUtils.substituteChild(
                    (Entity) getSelectedItem(),
                    newValue);
                if (getSelectedItem() == root) {
                    root = substituted;
                }
                tree.setModel(new TreeModelImpl());
                tree.expandAll();
                tree.scrollRowToVisible(getSelectedRow());
                tree.setSelectionRow(getSelectedRow());
            } else if (getSelectedItem() instanceof ChoiceGroup) {
                final ChoiceGroup choice = (ChoiceGroup) getSelectedItem();
                final String newValue =
                    (String) choiceComboBox.getSelectedItem();
                ModelUtils.choiceSetSelected(
                    choice,
                    newValue);
                tree.setModel(new TreeModelImpl());
                tree.expandAll();
                tree.scrollRowToVisible(getSelectedRow());
                tree.setSelectionRow(getSelectedRow());
            } else {
                Reporting.logUnexpected(getSelectedItem().toString());
            }
        }
    }

    /**
     * Sets the button callback.
     */
    public void setButtonCallback() {
        if (getSelectedItem() != null) {
            if (getSelectedItem() instanceof Settable) {
                final String value = valueEditor.getText();
                if ((value == null)
                    || (value.trim().length() == 0)) {
                    ((Settable) getSelectedItem()).setValue(null);
                } else {
                    ((Settable) getSelectedItem()).setValue(value);
                }
                tree.setModel(new TreeModelImpl());
                tree.expandAll();
                tree.scrollRowToVisible(getSelectedRow());
                tree.setSelectionRow(getSelectedRow());
                return;
            }
            Reporting.logUnexpected(getSelectedItem().toString());
        }
    }

    /**
     * Gets the cell renderer interceptor.
     * @return the cell renderer interceptor
     */
    public TreeCellRendererInterceptor getCellRendererInterceptor() {
        return cellRendererInterceptor;
    }

    /**
     * Sets the cell renderer interceptor.
     * @param aCellRendererInterceptor the new cell renderer interceptor
     */
    public void setCellRendererInterceptor(
        final TreeCellRendererInterceptor aCellRendererInterceptor) {
        this.cellRendererInterceptor = aCellRendererInterceptor;
    }

    /**
     * Gets the scroll position.
     * @return the scroll position
     */
    public int getScrollPosition() {
        return (int) scrollPane.getViewport().getViewPosition().getY();
    }

    /**
     * Sets the scroll position.
     * @param lastScrollPosition the new scroll position
     */
    public void setScrollPosition(
        final double lastScrollPosition) {
        scrollPane.getViewport().setViewPosition(
            new Point(
                0,
                (int) lastScrollPosition));
    }

    /**
     * Gets the selected row.
     * @return the selected row
     */
    public int getSelectedRow() {
        return selectedRow;
    }

    /**
     * Sets the selected row.
     * @param aSelectedRow the new selected row
     */
    public void setSelectedRow(
        final int aSelectedRow) {
        selectedRow = aSelectedRow;
    }

    /**
     * Gets the selected item.
     * @return the selected item
     */
    public Object getSelectedItem() {
        return selectedItem;
    }

    /**
     * Sets the selected item.
     * @param aSelectedItem the new selected item
     */
    public void setSelectedItem(
        final Object aSelectedItem) {
        selectedItem = aSelectedItem;
    }
}
