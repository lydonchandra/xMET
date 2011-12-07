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

import java.io.Serializable;

/**
 * Encodes the basic information needed to generate a property sheet.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class CustomReflectivePropertySheetItem
    implements
    Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The label displayed for the item. */
    private String label;

    /** The description of the item. */
    private String description;

    /** The type of PSE. */
    private Class type;

    /** The Object instance whose field is to be edited. */
    private Object item;

    /** The field name to be edited. */
    private String fieldName;

    /** The list of parameters the PSE takes as input. */
    private String[] params;

    /**
     * Instantiates a new custom reflective property sheet item.
     * @param aLabel the label
     * @param aDescription the description
     * @param aType the type
     * @param aItem the item
     * @param aFieldName the field name
     * @param aParams the params
     */

    public CustomReflectivePropertySheetItem(
        final String aLabel,
        final String aDescription,
        final Class aType,
        final Object aItem,
        final String aFieldName,
        final String... aParams) {
        super();
        this.setLabel(aLabel);
        this.setDescription(aDescription);
        this.setType(aType);
        this.setItem(aItem);
        this.setFieldName(aFieldName);
        this.params = aParams;
    }

    /**
     * Instantiates a new custom reflective property sheet item.
     * @param aLabel the label
     * @param aType the type
     * @param aParams the params
     */
    public CustomReflectivePropertySheetItem(
        final String aLabel,
        final Class aType,
        final String... aParams) {
        this(aLabel, aLabel, aType, null, "", aParams);
    }

    /**
     * Gets the label displayed for the item.
     * @return the label displayed for the item
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the description of the item.
     * @return the description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the type of PSE.
     * @return the type of PSE
     */
    public Class getType() {
        return type;
    }

    /**
     * Gets the Object instance whose field is to be edited.
     * @return the Object instance whose field is to be edited
     */
    public Object getItem() {
        return item;
    }

    /**
     * Gets the field name to be edited.
     * @return the field name to be edited
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Gets the list of parameters the PSE takes as input.
     * @return the list of parameters the PSE takes as input
     */
    public String[] getParams() {
        return params.clone();
    }

    /**
     * Sets the label displayed for the item.
     * @param aLabel the new label displayed for the item
     */
    public void setLabel(
        final String aLabel) {
        label = aLabel;
    }

    /**
     * Sets the description of the item.
     * @param aDescription the new description of the item
     */
    public void setDescription(
        final String aDescription) {
        description = aDescription;
    }

    /**
     * Sets the type of PSE.
     * @param aType the new type of PSE
     */
    public void setType(
        final Class aType) {
        type = aType;
    }

    /**
     * Sets the Object instance whose field is to be edited.
     * @param aItem the new Object instance whose field is to be edited
     */
    public void setItem(
        final Object aItem) {
        item = aItem;
    }

    /**
     * Sets the field name to be edited.
     * @param aFieldName the new field name to be edited
     */
    public void setFieldName(
        final String aFieldName) {
        fieldName = aFieldName;
    }

    /**
     * Sets the list of parameters the PSE takes as input.
     * @param aParams the new list of parameters the PSE takes as input
     */
    protected void setParams(
        final String[] aParams) {
        params = aParams;
    }

}
