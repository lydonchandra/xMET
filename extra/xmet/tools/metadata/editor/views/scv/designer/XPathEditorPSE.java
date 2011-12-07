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
package xmet.tools.metadata.editor.views.scv.designer;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import n.ui.SwingUtils;
import n.ui.TreeCellRendererInterceptor;
import n.ui.patterns.callback.RunnableCallback;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;
import xmet.profiles.model.Entity;
import xmet.ui.profileModel.ModelBrowserDialog;

/**
 * This PSE Allows XPaths to be entered via a ModelBrowserDialog.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class XPathEditorPSE
    extends JPanel
    implements
    PropertySheetEditor,
    Runnable {

    /* == Parameters == */
    /** The model - for xpath editing. */
    private Entity model;

    /**
     * The selection mask - the types of elements allowed to be selected in the
     * model browser dialog.
     */
    private int selectionMask =
        ModelBrowserDialog.SELECT_SETABLE_ELEMENT_DECLARATION
            | ModelBrowserDialog.SELECT_ATTRIBUTE;
    /** The model borwser cell render interceptor. */
    private TreeCellRendererInterceptor modelBorwserCellRenderInterceptor;

    /**
     * The name selection mask - for things like spatial extent editor and
     * contacts info editor - they might only take in certain node names.
     */
    private String nameSelectionMask = null;

    /* == UI elements == */
    /** The text field. */
    private final JTextField textField;

    /** The set button. */
    private final JButton setButton;

    /**
     * Instantiates a new x path editor PSE.
     */
    public XPathEditorPSE() {
        textField = new JTextField();
        setButton = SwingUtils.BUTTON.getNew(
            "set",
            new RunnableCallback(
                this));
        setLayout(new BorderLayout());
        add(textField);
        add(
            setButton,
            BorderLayout.EAST);
    }

    /* == Runnable Implementation == */

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        final ModelBrowserDialog mbd = new ModelBrowserDialog();
        mbd.setRoot(getModel());
        mbd.setModal(true);
        mbd.setSelectionMask(getSelectionMask());
        mbd.setNameSelectionMask(nameSelectionMask);
        if (getModelBorwserCellRenderInterceptor() != null) {
            mbd.getBrowserControl().setCellRendererInterceptor(
                getModelBorwserCellRenderInterceptor());
        }
        mbd.showSelectionDialog();
        final String xpath = mbd.getSelected();
        if ((xpath != null)
            && (xpath.trim().length() > 0)) {
            textField.setText(xpath);
        }
    }

    /* == PropertySheerEditor Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        textField.setText((String) value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        return textField.getText();
    }

    /**
     * Gets the model - for xpath editing.
     * @return the model - for xpath editing
     */
    public Entity getModel() {
        return model;
    }

    /**
     * Sets the model - for xpath editing.
     * @param aModel the new model - for xpath editing
     */
    public void setModel(
        final Entity aModel) {
        model = aModel;
    }

    /**
     * Gets the model borwser cell render interceptor.
     * @return the model borwser cell render interceptor
     */
    public TreeCellRendererInterceptor getModelBorwserCellRenderInterceptor() {
        return modelBorwserCellRenderInterceptor;
    }

    /**
     * Sets the model borwser cell render interceptor.
     * @param aModelBorwserCellRenderInterceptor the new model borwser cell
     *            render interceptor
     */
    public void setModelBorwserCellRenderInterceptor(
        final TreeCellRendererInterceptor aModelBorwserCellRenderInterceptor) {
        modelBorwserCellRenderInterceptor = aModelBorwserCellRenderInterceptor;
    }

    /**
     * Gets the name selection mask - for things like spatial extent editor and
     * contacts info editor - they might only take in certain node names.
     * @return the name selection mask - for things like spatial extent editor
     *         and contacts info editor - they might only take in certain node
     *         names
     */
    public String getNameSelectionMask() {
        return nameSelectionMask;
    }

    /**
     * Sets the name selection mask - for things like spatial extent editor and
     * contacts info editor - they might only take in certain node names.
     * @param aNameSelectionMask the new name selection mask - for things like
     *            spatial extent editor and contacts info editor - they might
     *            only take in certain node names
     */
    public void setNameSelectionMask(
        final String aNameSelectionMask) {
        nameSelectionMask = aNameSelectionMask;
    }

    /**
     * Sets the selection mask - the types of elements allowed to be selected in
     * the model browser dialog.
     * @param aSelectionMask the new selection mask - the types of elements
     *            allowed to be selected in the model browser dialog
     */
    void setSelectionMask(
        final int aSelectionMask) {
        this.selectionMask = aSelectionMask;
    }

    /**
     * Gets the selection mask - the types of elements allowed to be selected in
     * the model browser dialog.
     * @return the selection mask - the types of elements allowed to be selected
     *         in the model browser dialog
     */
    int getSelectionMask() {
        return selectionMask;
    }
}
