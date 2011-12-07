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
package xmet.tools.metadata.manager;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

import n.reporting.Reporting;
import n.ui.GenericSelectionListener;
import xmet.ClientContext;

/**
 * This is a Metadata Management Viewing Panel Tree Cell Renderer which renders
 * MetadataFile nodes with check boxes and maintains a list of checked nodes.
 * @author Nahid Akbar
 */
public class ExportFilesListMMVPTCR
    extends MMVPTCR
    implements
    TreeCellRenderer,
    TreeCellEditor,
    ActionListener {

    /** The selected list. */
    private final ArrayList<MetadataFile> selectedList =
        new ArrayList<MetadataFile>();

    /** The selected plugins - selection can be across plugins as well. */
    private final ArrayList<MMGMTPlugin> selectedPlugins =
        new ArrayList<MMGMTPlugin>();

    /** The current. */
    private MetadataFile current;

    /** The plugin. */
    private MMGMTPlugin plugin;

    /** The client. */
    // ClientContext client;

    /** The metadata management viewing panel. */
    private MetadataManagementViewingPanel mmvp;

    /** The selection listener for making callbacks. */
    private GenericSelectionListener selectionListener;

    /**
     * Gets the selection listener.
     * @return the selection listener
     */
    public GenericSelectionListener getSelectionListener() {
        return selectionListener;
    }

    /**
     * Sets the selection listener.
     * @param aSelectionListener the new selection listener
     */
    public void setSelectionListener(
        final GenericSelectionListener aSelectionListener) {
        this.selectionListener = aSelectionListener;
    }

    /**
     * Gets the selected list.
     * @return the selected list
     */
    public ArrayList<MetadataFile> getSelectedList() {
        return selectedList;
    }

    /**
     * Gets the selected plugins.
     * @return the selected plugins
     */
    public ArrayList<MMGMTPlugin> getSelectedPlugins() {
        return selectedPlugins;
    }

    /**
     * Instantiates a new export files list mmvptcr.
     * @param client the client
     */
    public ExportFilesListMMVPTCR(
        final ClientContext client) {
        super();
        // this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installCellRenderer(
        final MetadataManagementViewingPanel aMmvp) {
        try {
            this.mmvp = aMmvp;
            aMmvp.getFilesTree().setCellRenderer(
                this);
            aMmvp.getFilesTree().setEditable(
                true);
            aMmvp.getFilesTree().setCellEditor(
                this);
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }
    }

    /* == TreeCellRenderer Interface Implementation == */
    /** The renderer label. */
    private JCheckBox rendererLabel;

    /** The selected color. */
    private static Color selectedColor;

    /** The normal color. */
    private static Color normalColor;

    static {
        selectedColor = UIManager.getColor("Tree.selectionBackground");
        normalColor = UIManager.getColor("Tree.nackground");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(
        final JTree tree,
        final Object value,
        final boolean selected,
        final boolean expanded,
        final boolean leaf,
        final int row,
        final boolean hasFocus) {
        try {
            if (rendererLabel == null) { /* initialize */
                rendererLabel = new JCheckBox();
            }
            if (value instanceof MetadataFile) {
                final MetadataFile file = (MetadataFile) value;
                rendererLabel.setText(file.getName());
                if (file.isFolder()) {
                    rendererLabel.setBorder(BorderFactory
                        .createLineBorder(Color.BLACK));
                } else {
                    rendererLabel.setBorder(BorderFactory.createEmptyBorder());
                }
            }
            if (selected) {
                rendererLabel.setBackground(selectedColor);
            } else {
                rendererLabel.setBackground(normalColor);
            }
            rendererLabel.setSelected(selectedList.indexOf(value) != -1);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return rendererLabel;
    }

    /* == TreeCellEditor Interface Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void addCellEditorListener(
        final CellEditorListener arg0) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelCellEditing() {

    }

    /**
     * Select item.
     * @param aCurrent the current
     * @param aPlugin the plugin
     * @param b the b
     */
    private void selectItem(
        final MetadataFile aCurrent,
        final MMGMTPlugin aPlugin,
        final boolean b) {
        if (b) {
            if (selectedList.indexOf(aCurrent) == -1) {
                selectedList.add(aCurrent);
                selectedPlugins.add(aPlugin);
            }
        } else {
            int i;
            i = selectedList.indexOf(aCurrent);
            if ((i) != -1) {
                selectedList.remove(i);
                selectedPlugins.remove(i);
            }
        }
        if (aCurrent.isFolder()) {
            for (final MetadataFile file : aCurrent.getChildren()) {
                selectItem(
                    file,
                    aPlugin,
                    b);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCellEditorValue() {
        return current;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(
        final EventObject arg0) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCellEditorListener(
        final CellEditorListener arg0) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldSelectCell(
        final EventObject arg0) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopCellEditing() {
        cancelCellEditing();
        return true;
    }

    /** The editor label. */
    private JCheckBox editorLabel;

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellEditorComponent(
        final JTree tree,
        final Object value,
        final boolean selected,
        final boolean expanded,
        final boolean leaf,
        final int row) {
        try {
            current = (MetadataFile) value;
            plugin = mmvp.getSelectedPlugin();
            if (editorLabel == null) { /* initialize */
                editorLabel = new JCheckBox();
                editorLabel.addActionListener(this);
            }
            if (value instanceof MetadataFile) {
                final MetadataFile file = (MetadataFile) value;
                editorLabel.setText(file.getName());
            }
            if (selected) {
                editorLabel.setBackground(selectedColor);
            } else {
                editorLabel.setBackground(normalColor);
            }
            editorLabel.setSelected(selectedList.indexOf(value) != -1);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return editorLabel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        if (editorLabel.isSelected()) {
            selectItem(
                current,
                plugin,
                true);
        } else {
            selectItem(
                current,
                plugin,
                false);
        }
        mmvp.getFilesTree().repaint();
        if (selectionListener != null) {
            selectionListener.selectionChanged(this);
        }
    }

}
