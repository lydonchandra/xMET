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
package xmet.ui.templates;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import n.ui.GenericSingleItemSelectionListener;
import n.ui.JOptionPaneUtils;
import n.ui.SwingUtils;
import n.ui.patterns.callback.ClassMethodCallback;
import xmet.ClientContext;
import xmet.profiles.editorSheet.EditorSheetTemplate;
import xmet.profiles.editorSheet.ProfileEditorSheet;

import com.jgoodies.common.collect.ArrayListModel;

/**
 * A management pane for templates of an editor sheet.
 * @author Nahid Akbar
 */
public class TemplatesManagementPane
    extends JPanel
    implements
    ListSelectionListener {

    /** The context. */
    private ClientContext context;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The editor sheet. */
    private ProfileEditorSheet editorSheet = null;

    /** The templates list. */
    private JList templatesList;

    /** The selection listeners. */
    private ArrayList<GenericSingleItemSelectionListener> selectionListeners =
        new ArrayList<GenericSingleItemSelectionListener>();

    /**
     * Instantiates a new templates management pane.
     * @param aEditorSheet the editor sheet
     * @param aContext the context
     */
    public TemplatesManagementPane(
        final ProfileEditorSheet aEditorSheet,
        final ClientContext aContext) {
        this.setContext(aContext);
        this.setEditorSheet(aEditorSheet);
        setLayout(new BorderLayout());
        setTemplatesList(new JList(
            new ArrayListModel<EditorSheetTemplate>(
                aEditorSheet.getTemplates())));
        getTemplatesList().setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        getTemplatesList().getSelectionModel().addListSelectionListener(
            this);
        final JToolBar editingToolbar = new JToolBar(
            "Templates Management",
            SwingConstants.VERTICAL);
        final Object[] params = {};
        editingToolbar.add(SwingUtils.BUTTON.getNewV(
            "Rename",
            aContext.getResources().getImageIconResource(
                "images/toolbar.common.renameTemplate.png"),
            new ClassMethodCallback(
                this,
                "renameTemplate",
                params)));
        final Object[] params1 = {};
        editingToolbar.add(SwingUtils.BUTTON.getNewV(
            "Delete",
            aContext.getResources().getImageIconResource(
                "images/toolbar.common.deleteTemplate.png"),
            new ClassMethodCallback(
                this,
                "deleteTemplate",
                params1)));
        editingToolbar.setFloatable(false);
        add(new JScrollPane(
            getTemplatesList()));
        add(
            editingToolbar,
            BorderLayout.EAST);
    }

    /* == Buttion Callback Implementations == */

    /**
     * Rename template.
     */
    public void renameTemplate() {
        try {
            final int rowIndex = getTemplatesList().getSelectedIndex();
            if ((rowIndex >= 0)
                && (rowIndex < getEditorSheet().getTemplates().size())) {
                final EditorSheetTemplate template =
                    getEditorSheet().getTemplates().get(
                        rowIndex);
                String newName = JOptionPaneUtils.getUserInputString(
                    "Please enter a new name",
                    template.getName());
                if (newName != null
                    && newName.trim().length() > 0
                    && !template.getName().equals(
                        newName.trim())) {
                    newName = newName.trim().replaceAll(
                        "[^\\p{Alnum}\\p{Blank}]",
                        "_").trim();
                    getEditorSheet().renameTemplate(
                        template,
                        newName,
                        getContext().getResources());
                    getTemplatesList().setModel(
                        new ArrayListModel<EditorSheetTemplate>(
                            getEditorSheet().getTemplates()));
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete template.
     */
    public void deleteTemplate() {
        try {
            final int rowIndex = getTemplatesList().getSelectedIndex();
            if ((rowIndex >= 0)
                && (rowIndex < getEditorSheet().getTemplates().size())) {
                final EditorSheetTemplate template =
                    getEditorSheet().getTemplates().get(
                        rowIndex);
                if (JOptionPaneUtils.getYesNoConfirmation("Delete Template?")) {
                    template.deleteTemplate(getContext().getResources());
                    getTemplatesList().setModel(
                        new ArrayListModel<EditorSheetTemplate>(
                            getEditorSheet().getTemplates()));
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the selection listener.
     * @param e the e
     * @return true, if successful
     */
    public boolean addSelectionListener(
        final GenericSingleItemSelectionListener e) {
        return getSelListeners().add(
            e);
    }

    /**
     * Removes the selection listener.
     * @param o the o
     * @return true, if successful
     */
    public boolean removeSelectionListener(
        final Object o) {
        return getSelListeners().remove(
            o);
    }

    /* == ListSelectionListener Interface Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(
        final ListSelectionEvent e) {
        int rowIndex = getTemplatesList().getSelectedIndex();
        EditorSheetTemplate template = null;
        if ((rowIndex >= 0)
            && (rowIndex < getEditorSheet().getTemplates().size())) {
            template = getEditorSheet().getTemplates().get(
                rowIndex);
        } else {
            rowIndex = -1;
        }
        for (final GenericSingleItemSelectionListener l : getSelListeners()) {
            l.selectionChanged(
                this,
                template,
                rowIndex);
        }
    }

    /**
     * Gets the editor sheet.
     * @return the editor sheet
     */
    public ProfileEditorSheet getEditorSheet() {
        return editorSheet;
    }

    /**
     * Gets the context.
     * @return the context
     */
    public ClientContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     * @param aContext the context to set
     */
    public void setContext(
        final ClientContext aContext) {
        this.context = aContext;
    }

    /**
     * Sets the editor sheet.
     * @param aEditorSheet the editorSheet to set
     */
    public void setEditorSheet(
        final ProfileEditorSheet aEditorSheet) {
        this.editorSheet = aEditorSheet;
    }

    /**
     * Gets the templates list.
     * @return the templatesList
     */
    public JList getTemplatesList() {
        return templatesList;
    }

    /**
     * Sets the templates list.
     * @param aTemplatesList the templatesList to set
     */
    public void setTemplatesList(
        final JList aTemplatesList) {
        this.templatesList = aTemplatesList;
    }

    /**
     * Gets the selection listeners.
     * @return the selectionListeners
     */
    public ArrayList<GenericSingleItemSelectionListener> getSelListeners() {
        return selectionListeners;
    }

    /**
     * Sets the selection listeners.
     * @param aSelListeners the selectionListeners to set
     */
    public void setSelListeners(
        final ArrayList<GenericSingleItemSelectionListener> aSelListeners) {
        this.selectionListeners = aSelListeners;
    }

    /* == ListModel Interface Implementation == */

}
