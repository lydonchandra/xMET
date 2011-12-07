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
package xmet.tools.metadata.editor.views.scv.impl;

import java.util.ArrayList;
import java.util.List;

import n.io.CS;
import n.io.CSC;
import n.io.CSL;
import n.reporting.Reporting;
import xmet.tools.metadata.editor.views.scv.utils.InitializationContext;
import xmet.tools.metadata.editor.views.scv.utils.ItemIC;
import xmet.tools.metadata.editor.views.scv.view.DisplayContext;
import xmet.tools.metadata.editor.views.scv.view.ItemDC;

/**
 * Represents an item i.e. (label, control type, xpath) tuple It was basically
 * that but has evolved over the years.
 * @author Nahid Akbar
 */
@CSC("item")
public class Item
    implements
    GroupSubitem,
    PageSubitem,
    HideableNamedItem,
    ContentNamedItem,
    CodeParent {

    /* == Properties == */

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* -- Display Properties ---------------- */
    /** The title or the label of the item (presented). */
    @CS
    private String title;

    /** The whether the item is visible to the user or not. */
    @CS
    private boolean visible = true;

    /** The description text (displayed). */
    @CS
    private String description;

    /** The hover text (displayed). */
    @CS
    private String hover;

    /** Custom column span. */
    @CS
    private int columnSpan = -1;

    /**
     * default value loaded during initialization.
     */
    @CSC
    private Default defaultValue;

    /** Help Context ID. */
    @CS
    private String helpContextID;

    /* -- Display Control Properties ---------------- */
    /**
     * The type of GUIObject to be used. if left with Unspecified, the item is
     * not displayed
     */
    @CS
    private EditorType type = EditorType.Unspecified;

    /**
     * parameters taken by the control.
     */
    @CSL(listMode = CSL.LIST_LINIENT_MODE)
    @CSC
    private List<Param> params = new ArrayList<Param>();

    /** code to be executed on data load. */
    @CSC
    private Code onDataLoad;

    /** code to be executed on change. */
    @CSC
    private Code onDataChange;

    /** The name of the item (for identification). */
    @CS
    private String name;

    /* -- Profile Model Link Properties ---------------- */

    /** The xpath of the item. */
    @CS
    private String xpath;

    /* -- Validation Properties ---------------- */
    /** The validation. */
    @CSC
    private ValidationInfo validation;

    /** Whether the item is mandatory or not. */

    /**
     * Instantiates a new item.
     */
    public Item() {
    }

    /* == Runtime Helper Stuff == */

    /**
     * The display context.
     */
    private transient ItemDC dc;

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayContext getDC() {
        return getDc();
    }

    /**
     * The initialization context.
     */
    private transient ItemIC ic;

    /**
     * {@inheritDoc}
     */
    @Override
    public InitializationContext getIC() {
        return getIc();
    }

    /* == Manipulable implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(
        final boolean aVisible) {
        if (aVisible != this.visible) {
            this.visible = aVisible;
            if (getIc() != null) {
                getIc().visibilityChanged(
                    this);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(
        final String value) {
        if (getDc() != null) {
            getDc().loadSetValue(
                value);
        } else if (getIc() != null) {
            Reporting.logUnexpected();
        } else {
            assert (false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMandatory(
        final boolean mandatory) {
        if (mandatory) {
            if (getValidation() == null) {
                setValidation(new ValidationInfo());
            }
        }
        if (getValidation() != null) {
            getValidation().setMandatory(
                true);
            if (getValidation().hasDefaultValues()) {
                setValidation(null);
            }
        }
        if (getDc() != null) {
            getDc().mandatoryChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        if (getDc() != null
            && getDc().getControl() != null) {
            return getDc().getControl().getValue();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXpath() {
        return xpath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
            "Item[%1$s]",
            getType().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidationError(
        final String validationError) {
        if (getDc() != null) {
            getDc().setValidationErrorMessage(
                validationError);
            getDc().updatePanel();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(
        final String aName) {
        this.name = aName;
    }

    /**
     * Checks if is mandatory.
     * @return true, if is mandatory
     */
    public boolean isMandatory() {
        return getValidation() != null
            && getValidation().isMandatory();
    }

    /**
     * Gets the title or the label of the item (presented).
     * @return the title or the label of the item (presented)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title or the label of the item (presented).
     * @param aTitle the new title or the label of the item (presented)
     */
    public void setTitle(
        final String aTitle) {
        title = aTitle;
    }

    /**
     * Gets the description text (displayed).
     * @return the description text (displayed)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description text (displayed).
     * @param aDescription the new description text (displayed)
     */
    public void setDescription(
        final String aDescription) {
        description = aDescription;
    }

    /**
     * Gets the hover text (displayed).
     * @return the hover text (displayed)
     */
    public String getHover() {
        return hover;
    }

    /**
     * Sets the hover text (displayed).
     * @param aHover the new hover text (displayed)
     */
    public void setHover(
        final String aHover) {
        hover = aHover;
    }

    /**
     * Gets the custom column span.
     * @return the custom column span
     */
    public int getColumnSpan() {
        return columnSpan;
    }

    /**
     * Sets the custom column span.
     * @param aColumnSpan the new custom column span
     */
    public void setColumnSpan(
        final int aColumnSpan) {
        columnSpan = aColumnSpan;
    }

    /**
     * Gets the default value loaded during initialization.
     * @return the default value loaded during initialization
     */
    public Default getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value loaded during initialization.
     * @param aDefaultValue the new default value loaded during initialization
     */
    public void setDefaultValue(
        final Default aDefaultValue) {
        defaultValue = aDefaultValue;
    }

    /**
     * Gets the help Context ID.
     * @return the help Context ID
     */
    public String getHelpContextID() {
        return helpContextID;
    }

    /**
     * Sets the help Context ID.
     * @param aHelpContextID the new help Context ID
     */
    public void setHelpContextID(
        final String aHelpContextID) {
        helpContextID = aHelpContextID;
    }

    /**
     * Gets the type of GUIObject to be used.
     * @return the type of GUIObject to be used
     */
    public EditorType getType() {
        return type;
    }

    /**
     * Sets the type of GUIObject to be used.
     * @param aType the new type of GUIObject to be used
     */
    public void setType(
        final EditorType aType) {
        type = aType;
    }

    /**
     * Gets the parameters taken by the control.
     * @return the parameters taken by the control
     */
    public List<Param> getParams() {
        return params;
    }

    /**
     * Sets the parameters taken by the control.
     * @param aParams the new parameters taken by the control
     */
    public void setParams(
        final List<Param> aParams) {
        params = aParams;
    }

    /**
     * Gets the code to be executed on data load.
     * @return the code to be executed on data load
     */
    public Code getOnDataLoad() {
        return onDataLoad;
    }

    /**
     * Sets the code to be executed on data load.
     * @param aOnDataLoad the new code to be executed on data load
     */
    public void setOnDataLoad(
        final Code aOnDataLoad) {
        onDataLoad = aOnDataLoad;
    }

    /**
     * Gets the code to be executed on change.
     * @return the code to be executed on change
     */
    public Code getOnDataChange() {
        return onDataChange;
    }

    /**
     * Sets the code to be executed on change.
     * @param aOnDataChange the new code to be executed on change
     */
    public void setOnDataChange(
        final Code aOnDataChange) {
        onDataChange = aOnDataChange;
    }

    /**
     * Gets the validation.
     * @return the validation
     */
    public ValidationInfo getValidation() {
        return validation;
    }

    /**
     * Sets the validation.
     * @param aValidation the new validation
     */
    public void setValidation(
        final ValidationInfo aValidation) {
        validation = aValidation;
    }

    /**
     * Gets the display context.
     * @return the display context
     */
    public ItemDC getDc() {
        return dc;
    }

    /**
     * Sets the display context.
     * @param aDc the new display context
     */
    public void setDc(
        final ItemDC aDc) {
        dc = aDc;
    }

    /**
     * Gets the initialization context.
     * @return the initialization context
     */
    public ItemIC getIc() {
        return ic;
    }

    /**
     * Sets the initialization context.
     * @param aIc the new initialization context
     */
    public void setIc(
        final ItemIC aIc) {
        ic = aIc;
    }

    /**
     * Checks if is the whether the item is visible to the user or not.
     * @return the whether the item is visible to the user or not
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the xpath of the item.
     * @param aXpath the new xpath of the item
     */
    public void setXpath(
        final String aXpath) {
        xpath = aXpath;
    }

}
