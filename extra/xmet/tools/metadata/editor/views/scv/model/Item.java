/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br />
 * <br />
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br />
 * <br />
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br />
 * <br />
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br />
 * <br />
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.tools.metadata.editor.views.scv.model;

import java.util.ArrayList;
import java.util.List;

import n.io.CS;
import n.io.CSC;
import n.io.CSL;

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
    private ValidationInformation validation;

    /**
     * Gets the serialversionuid.
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * Gets the title or the label of the item (presented).
     * @return the title or the label of the item (presented)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Checks if is the whether the item is visible to the user or not.
     * @return the whether the item is visible to the user or not
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Gets the description text (displayed).
     * @return the description text (displayed)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the hover text (displayed).
     * @return the hover text (displayed)
     */
    public String getHover() {
        return hover;
    }

    /**
     * Gets the custom column span.
     * @return the custom column span
     */
    public int getColumnSpan() {
        return columnSpan;
    }

    /**
     * Gets the default value loaded during initialization.
     * @return the default value loaded during initialization
     */
    public Default getDefaultValue() {
        return defaultValue;
    }

    /**
     * Gets the help Context ID.
     * @return the help Context ID
     */
    public String getHelpContextID() {
        return helpContextID;
    }

    /**
     * Gets the type of GUIObject to be used.
     * @return the type of GUIObject to be used
     */
    public EditorType getType() {
        return type;
    }

    /**
     * Gets the parameters taken by the control.
     * @return the parameters taken by the control
     */
    public List<Param> getParams() {
        return params;
    }

    /**
     * Gets the code to be executed on data load.
     * @return the code to be executed on data load
     */
    public Code getOnDataLoad() {
        return onDataLoad;
    }

    /**
     * Gets the code to be executed on change.
     * @return the code to be executed on change
     */
    public Code getOnDataChange() {
        return onDataChange;
    }

    /**
     * Gets the name of the item (for identification).
     * @return the name of the item (for identification)
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the xpath of the item.
     * @return the xpath of the item
     */
    public String getXpath() {
        return xpath;
    }

    /**
     * Gets the validation.
     * @return the validation
     */
    public ValidationInformation getValidation() {
        return validation;
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
     * Sets the whether the item is visible to the user or not.
     * @param aVisible the new whether the item is visible to the user or not
     */
    public void setVisible(
        final boolean aVisible) {
        visible = aVisible;
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
     * Sets the hover text (displayed).
     * @param aHover the new hover text (displayed)
     */
    public void setHover(
        final String aHover) {
        hover = aHover;
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
     * Sets the default value loaded during initialization.
     * @param aDefaultValue the new default value loaded during initialization
     */
    public void setDefaultValue(
        final Default aDefaultValue) {
        defaultValue = aDefaultValue;
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
     * Sets the type of GUIObject to be used.
     * @param aType the new type of GUIObject to be used
     */
    public void setType(
        final EditorType aType) {
        type = aType;
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
     * Sets the code to be executed on data load.
     * @param aOnDataLoad the new code to be executed on data load
     */
    public void setOnDataLoad(
        final Code aOnDataLoad) {
        onDataLoad = aOnDataLoad;
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
     * Sets the name of the item (for identification).
     * @param aName the new name of the item (for identification)
     */
    public void setName(
        final String aName) {
        name = aName;
    }

    /**
     * Sets the xpath of the item.
     * @param aXpath the new xpath of the item
     */
    public void setXpath(
        final String aXpath) {
        xpath = aXpath;
    }

    /**
     * Sets the validation.
     * @param aValidation the new validation
     */
    public void setValidation(
        final ValidationInformation aValidation) {
        validation = aValidation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        visitor.preVisitItem(this);
        if (getOnDataLoad() != null) {
            visitor.preVisitCode(
                this,
                "onDataLoad");
            getOnDataLoad().accept(
                visitor);
            visitor.postVisitCode(
                this,
                "onDataLoad");
        }
        if (getOnDataChange() != null) {
            visitor.preVisitCode(
                this,
                "onDataChange");
            getOnDataChange().accept(
                visitor);
            visitor.postVisitCode(
                this,
                "onDataChange");
        }
        visitor.postVisitItem(this);
    }

}
