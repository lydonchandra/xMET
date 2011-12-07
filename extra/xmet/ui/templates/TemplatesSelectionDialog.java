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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import n.ui.GenericSingleItemSelectionListener;
import n.ui.SwingUtils;
import xmet.ClientContext;
import xmet.profiles.editorSheet.EditorSheetTemplate;
import xmet.profiles.editorSheet.ProfileEditorSheet;

/**
 * A selection dialog wrapper for a TemplatesManagementPane.
 * @author Nahid Akbar
 */
public class TemplatesSelectionDialog
    implements
    GenericSingleItemSelectionListener,
    ActionListener {

    /** The cmp. */
    private TemplatesManagementPane cmp;
    /**
     * The ok button.
     */
    private JButton okButton;

    /**
     * The cancel button.
     */
    private JButton cancelButton;

    /** The selected template. */
    private EditorSheetTemplate selectedTemplate = null;

    /** The dialog. */
    private JDialog dialog;

    /**
     * Instantiates a new templates selection dialog.
     * @param editorSheet the editor sheet
     * @param context the context
     */
    public TemplatesSelectionDialog(
        final ProfileEditorSheet editorSheet,
        final ClientContext context) {

        setCmp(new TemplatesManagementPane(
            editorSheet,
            context));

        getCmp().addSelectionListener(
            this);

        final JPanel buttonsPanel = SwingUtils.BoxLayouts.getHorizontalPanel();
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(setOkButton(new JButton(
            "Ok")));
        getOkButton().addActionListener(
            this);
        buttonsPanel.add(setCancelButton(new JButton(
            "Cancel")));
        getCancelButton().addActionListener(
            this);

        final JPanel panel = SwingUtils.BorderLayouts.getNew();

        panel.add(
            new JLabel(
                "Please select a template"),
            BorderLayout.NORTH);
        panel.add(getCmp());
        panel.add(
            buttonsPanel,
            BorderLayout.SOUTH);
        // CHECKSTYLE OFF: MagicNumber
        panel.setBorder(BorderFactory.createEmptyBorder(
            10,
            10,
            10,
            10));

        setDialog(SwingUtils.DIALOG.createDialog(
            panel,
            "Select a template...",
            640,
            480,
            true));
        // CHECKSTYLE ON: MagicNumber
        getDialog().setDefaultCloseOperation(
            WindowConstants.DISPOSE_ON_CLOSE);

    }

    /**
     * Show selection window.
     * @return the editor sheet template
     */
    public EditorSheetTemplate showSelectionWindow() {
        if (getCmp().getEditorSheet().getTemplates().size() > 1) {
            getDialog().setVisible(
                true);
        } else if (getCmp().getEditorSheet().getTemplates().size() == 1) {
            setSelectedTemplate(getCmp().getEditorSheet().getTemplates().get(
                0));
        } else {
            setSelectedTemplate(null);
        }
        return getSelectedTemplate();
    }

    /* == ActionListener Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(
        final ActionEvent arg0) {
        if (arg0.getSource() == getOkButton()) {
            if (getSelectedTemplate() != null) {
                getDialog().setVisible(
                    false);
            }
        } else { /* cancel button */
            setSelectedTemplate(null);
            getDialog().setVisible(
                false);
        }

    }

    /*
     * == GenericSingleItemSelectionListener Interface Implementation
     * ================
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void selectionChanged(
        final Object source,
        final Object selectedItem,
        final Object selectedItemIndex) {
        setSelectedTemplate((EditorSheetTemplate) selectedItem);
    }

    /**
     * Gets the selected template.
     * @return the selected template
     */
    public EditorSheetTemplate getSelectedTemplate() {
        return selectedTemplate;
    }

    /**
     * Gets the cmp.
     * @return the cmp
     */
    TemplatesManagementPane getCmp() {
        return cmp;
    }

    /**
     * Sets the cmp.
     * @param aCmp the new cmp
     */
    void setCmp(
        final TemplatesManagementPane aCmp) {
        this.cmp = aCmp;
    }

    /**
     * Gets the ok button.
     * @return the ok button
     */
    JButton getOkButton() {
        return okButton;
    }

    /**
     * Sets the ok button.
     * @param aOkButton the ok button
     * @return the j button
     */
    JButton setOkButton(
        final JButton aOkButton) {
        this.okButton = aOkButton;
        return aOkButton;
    }

    /**
     * Gets the cancel button.
     * @return the cancel button
     */
    JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Sets the cancel button.
     * @param aCancelButton the cancel button
     * @return the j button
     */
    JButton setCancelButton(
        final JButton aCancelButton) {
        this.cancelButton = aCancelButton;
        return aCancelButton;
    }

    /**
     * Sets the selected template.
     * @param aSelectedTemplate the new selected template
     */
    void setSelectedTemplate(
        final EditorSheetTemplate aSelectedTemplate) {
        this.selectedTemplate = aSelectedTemplate;
    }

    /**
     * Gets the dialog.
     * @return the dialog
     */
    JDialog getDialog() {
        return dialog;
    }

    /**
     * Sets the dialog.
     * @param aDialog the new dialog
     */
    void setDialog(
        final JDialog aDialog) {
        this.dialog = aDialog;
    }

}
