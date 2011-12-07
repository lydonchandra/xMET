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

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import n.java.ReflectionUtils;
import n.reporting.Reporting;
import n.ui.patterns.propertySheet.ObjectChoicePSE;
import n.ui.patterns.propertySheet.PropertySheetEditor;
import n.ui.patterns.propertySheet.PropertySheetItem;

/**
 * This PSE is for editing a list of Item types which also sends out a
 * notification when the type is changed.
 * @author Nahid Akbar
 */
public class ItemTypePSE
    extends Observable
    implements
    PropertySheetEditor {

    /** The ocpse. */
    private final ObjectChoicePSE ocpse = new ObjectChoicePSE();

    /** notification callback method. */
    private String callbackMethodName = null;

    /** The callback method object. */
    private Object callbackMethodObject = null;

    /** The callback method params. */
    private Object[] callbackMethodParams = null;

    /** The callback enabled flag - was added for infinite loop prevention. */
    private boolean enableCallback = true;

    /**
     * Instantiates a new item type pse.
     */
    public ItemTypePSE() {
        ocpse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(
                final ActionEvent e) {
                if (enableCallback) {
                    setChanged();
                    notifyObservers();
                    Reporting.logExpected(getCallbackMethodName());
                    if ((getCallbackMethodObject() != null)
                        && (getCallbackMethodName() != null)) {
                        EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    ReflectionUtils.callMethodByNameWithParams(
                                        getCallbackMethodObject(),
                                        getCallbackMethodName(),
                                        getCallbackMethodParams());
                                } catch (final Exception e) {
                                    Reporting.reportUnexpected(e);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /* == wrapper objects == */

    /**
     * Sets the choices.
     * @param values the new choices
     */
    public void setChoices(
        final Object[] values) {
        ocpse.setChoices(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        return ocpse.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditor(
        final Object value,
        final PropertySheetItem item) {
        enableCallback = false;
        final Component c = ocpse.getEditor(
            value,
            item);
        enableCallback = true;
        return c;
    }

    /**
     * Gets the notification callback method.
     * @return the notification callback method
     */
    public String getCallbackMethodName() {
        return callbackMethodName;
    }

    /**
     * Sets the notification callback method.
     * @param aCallbackMethodName the new notification callback method
     */
    public void setCallbackMethodName(
        final String aCallbackMethodName) {
        callbackMethodName = aCallbackMethodName;
    }

    /**
     * Gets the callback method object.
     * @return the callback method object
     */
    public Object getCallbackMethodObject() {
        return callbackMethodObject;
    }

    /**
     * Sets the callback method object.
     * @param aCallbackMethodObject the new callback method object
     */
    public void setCallbackMethodObject(
        final Object aCallbackMethodObject) {
        callbackMethodObject = aCallbackMethodObject;
    }

    /**
     * Gets the callback method params.
     * @return the callback method params
     */
    public Object[] getCallbackMethodParams() {
        return callbackMethodParams.clone();
    }

    /**
     * Sets the callback method params.
     * @param aCallbackMethodParams the new callback method params
     */
    public void setCallbackMethodParams(
        final Object[] aCallbackMethodParams) {
        callbackMethodParams = aCallbackMethodParams.clone();
    }
}
