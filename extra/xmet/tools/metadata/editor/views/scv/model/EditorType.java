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
package xmet.tools.metadata.editor.views.scv.model;

/**
 * Enumeration of the types of editors Semi custom view is allowed to have. They
 * must reflect appropriate GUI object class names in
 * xmet.client.tools.metadata.editor.views.custom package Note: remember to
 * update doesAutoScale and getPreferredColumnSpan methods of SCVUtils when this
 * list grows or shortens
 * @author Nahid Akbar
 */
public enum EditorType {

    /**
     * Editor is a checkbox that can be checked to set a certain value.
     */
    Checkbox,

    /**
     * Editor is a list of checkboxes with labels that can have multiple values
     * checked.
     */
    CheckedList,

    /**
     * Editor is something that allows multiple items from multiple codelists to
     * be selected inside a catalog.
     */
    CodelistCatalogList,

    /**
     * The editor allows contact information to be displayed and edited.
     */
    ContactInfoEditor,
    /**
     * The editor allows date values to be edited.
     */
    DatePicker,
    /**
     * The editor allows date and time values to be edited.
     */
    DateTimePicker,

    /**
     * The editor allows digital size of contents to be specified and edited.
     */
    DigitalContentSizeEditor,

    /**
     * The editor allows one value to be selected from a list of values.
     */
    DropDownList,

    /**
     * The editor allows file urls to be entered.
     */
    FileBrowser,
    /** The Keywords list editor. */
    KeywordsListEditor,
    /**
     * The editor is just a label that shows some text.
     */
    Label,

    /**
     * The editor is a double list that allows multiple values to be selected.
     */
    MoveOverList,

    /**
     * The editor is a multi line text box which allows free text input.
     */
    MultiLineText,

    /**
     * The editor is a single line text box which allows free text input.
     */
    SingleLineText,

    /**
     * The editor is something that allows spatial extents to be entered, viewed
     * and editted.
     */
    SpatialExtent,

    /**
     * The Unspecified - item is automatically set to hidden.
     */
    Unspecified
}
