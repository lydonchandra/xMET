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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.tree.TreeCellRenderer;

import n.ui.SwingUtils;
import n.ui.patterns.stb.JSimpleTreeBuilder;
import n.ui.patterns.stb.JSimpleTreeBuilderModelExtended;
import xmet.ClientContext;
import xmet.profiles.Profile;
import xmet.profiles.model.AllGroup;
import xmet.profiles.model.ChoiceGroup;
import xmet.profiles.model.Comment;
import xmet.profiles.model.ElementAttribute;
import xmet.profiles.model.ElementDeclaration;
import xmet.profiles.model.Entity;
import xmet.profiles.model.Extra;
import xmet.profiles.model.Group;
import xmet.profiles.model.ImplicitGroup;
import xmet.profiles.model.List;
import xmet.profiles.model.Optional;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.SequenceGroup;
import xmet.profiles.model.Settable;
import xmet.profiles.model.Simple;
import xmet.profiles.model.SimpleAttribute;
import xmet.profiles.model.Union;
import xmet.tools.metadata.editor.EditorView;

/**
 * Advanced View.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class GeneratedView
    extends EditorView
    implements
    JSimpleTreeBuilderModelExtended,
    ActionListener {

    /** The split pane. */
    private JSplitPane splitPane;

    /** The tree builder. */
    private JSimpleTreeBuilder treeBuilder;

    /** The display panel. */
    private JPanel displayPanel;

    /** The editor. */
    private GeneratedViewEditor editor;

    /** The editing row. */
    private int editingRow;
    {
        // CHECKSTYLE OFF: MagicNumber
        splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(2);
        splitPane.setResizeWeight(0.2);
        treeBuilder = new JSimpleTreeBuilder(
            this);
        splitPane.setLeftComponent(new JScrollPane(
            treeBuilder));
        editor = new DefaultGeneratedViewEditor();
        splitPane.setRightComponent(editor);
        treeBuilder.setButtonPanelPosition(JSimpleTreeBuilder.DONT_SHOW);

        displayPanel = SwingUtils.BorderLayouts.getNew();
        displayPanel.add(splitPane);
        // CHECKSTYLE ON: MagicNumber
    }

    /**
     * Instantiates a new generated view.
     * @param model the model
     * @param profile the profile
     * @param client the client
     */
    public GeneratedView(
        final Entity model,
        final Profile profile,
        final ClientContext client) {
        super(model, profile, client);
        treeBuilder.rowChildrenChanged(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel getEditorPanel() {
        return displayPanel;
    }

    /* == EditorView == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void postLoadCallback() {
        for (int i = 0; i < treeBuilder.getTree().getRowCount(); i++) {
            treeBuilder.getTree().expandRow(
                i);
        }
    }

    /* == JSimpleTreeBuilderExtended == */

    /**
     * {@inheritDoc}
     */
    @Override
    public Class[] getAllowedChildrenClasses(
        final Object parent) {
        return new Class[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addChildren(
        final Object parent,
        final Class childClass) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveChildrenUp(
        final Object parent,
        final Object child) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveChildrenDown(
        final Object parent,
        final Object child) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChildren(
        final Object parent,
        final Object child) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumChildrenCount(
        final Object parent) {
        if (parent instanceof ElementDeclaration) {
            return -1;
        } else if (parent instanceof Optional) {
            return -1;
        } else if (parent instanceof Settable) {
            return 0;
        } else if (parent instanceof ChoiceGroup) {
            return -1;
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
    public Object getChild(
        final Object parent,
        final int index) {
        if (parent instanceof ElementDeclaration) {
            final ElementDeclaration elementDeclaration =
                ((ElementDeclaration) parent);
            int attributes = 0;
            if (elementDeclaration.hasAttributes()) {
                attributes = elementDeclaration.getAttributes().size();
            } else {
                attributes = 0;
            }
            if ((index >= 0)
                && (index < attributes)) {
                return elementDeclaration.getAttributes().getAttributeByIndex(
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
    public void selectionChanged(
        final Object selectedItem,
        final int selectedRow) {
        if (editor != null) {
            editor.stopEditting();
            editor = null;
        }
        if (selectedItem != null) {

            editingRow = selectedRow;
            if (selectedItem instanceof AllGroup) {
                editor = new GroupGeneratedViewEditor(
                    (Group) selectedItem,
                    (ActionListener) this);
            } else if (selectedItem instanceof ChoiceGroup) {
                editor = new ChoiceGroupGeneratedViewEditor(
                    (ChoiceGroup) selectedItem,
                    (ActionListener) this);
            } else if (selectedItem instanceof Comment) {
                editor = new CommentGeneratedViewEditor(
                    (Comment) selectedItem,
                    (ActionListener) this);
            } else if (selectedItem instanceof ElementDeclaration) {
                editor = new ElementDeclarationGeneratedViewEditor(
                    (ElementDeclaration) selectedItem,
                    (ActionListener) this);
            } else if (selectedItem instanceof Extra) {
                editor = new ExtraGeneratedViewEditor(
                    (Extra) selectedItem,
                    (ActionListener) this);
            } else if (selectedItem instanceof ImplicitGroup) {
                editor = new GroupGeneratedViewEditor(
                    (Group) selectedItem,
                    (ActionListener) this);
            } else if (selectedItem instanceof Optional) {
                editor = new OptionalGeneratedViewEditor(
                    (Optional) selectedItem,
                    this);
            } else if (selectedItem instanceof Repeated) {
                editor = new RepeatedGeneratedViewEditor(
                    (Repeated) selectedItem,
                    this);
            } else if (selectedItem instanceof SequenceGroup) {
                editor = new GroupGeneratedViewEditor(
                    (Group) selectedItem,
                    (ActionListener) this);
            } else if (selectedItem instanceof Settable) {
                editor = new SetableGeneratedViewEditor(
                    (Settable) selectedItem,
                    this);
            } else if (selectedItem instanceof Simple) {
                editor = new DefaultGeneratedViewEditor();
            } else if (selectedItem instanceof ElementAttribute) {
                editor = new DefaultGeneratedViewEditor();
            } else if (selectedItem instanceof SimpleAttribute) {
                editor = new DefaultGeneratedViewEditor();
            } else if (selectedItem instanceof Union) {
                editor = new DefaultGeneratedViewEditor();
            } else if (selectedItem instanceof List) {
                editor = new DefaultGeneratedViewEditor();
            } else {
                editor = new DefaultGeneratedViewEditor();
            }
        } else {
            editor = new DefaultGeneratedViewEditor();
        }
        splitPane.setRightComponent(editor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassTitle(
        final Class classObject) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JButton getAddButton() {
        /* TODO Auto-generated method stub */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JButton getRemoveButton() {
        /* TODO Auto-generated method stub */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JButton getUpButton() {
        /* TODO Auto-generated method stub */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JButton getDownButton() {
        /* TODO Auto-generated method stub */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeCellRenderer getCellRenderer() {
        /* TODO Auto-generated method stub */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        treeBuilder.rowChanged(editingRow);
        treeBuilder.rowChildrenChanged(editingRow);
        treeBuilder.getTree().expandRow(
            editingRow);
        treeBuilder.updateSelectedRow(editingRow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isModelModified() {
        return true;
    }
}
