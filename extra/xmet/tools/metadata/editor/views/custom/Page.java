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
package xmet.tools.metadata.editor.views.custom;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import n.io.CS;
import n.io.CSC;
import n.io.CSL;
import xmet.ui.controls.GUIObject;

/**
 * Page.
 * @author Shaan
 */

@CSC("Page")
public class Page {

    /** The name. */
    @CS
    private String name;

    /** The parent. */
    private Page parent;

    /** The children. */
    @CSL
    @CSC
    private final List<Page> children = new ArrayList<Page>();

    /** The panel. */
    private JPanel panel = new JPanel();

    /** The gui objects. */
    @CSL
    @CSC
    private List<GUIObject> guiObjects = new ArrayList<GUIObject>();

    /** The column spec. */
    @CS
    private String rowSpec, columnSpec;

    /** The update panel. */
    private Boolean updatePanel = true;

    /**
     * Creates a page with the specified name and with the specified parent.
     * @param aParent Parent Page.
     * @param aName The name of the page.
     */
    public Page(
        final Page aParent,
        final String aName) {
        this.parent = aParent;
        this.name = aName;
    }

    /**
     * Creates a Page with the specified name. Parent is null.
     * @param aName The name of the page.
     */
    public Page(
        final String aName) {
        this.name = aName;
    }

    /**
     * Instantiates a new page.
     * @param aName The name of the page.
     * @param aColumnSpec The formLayout column specification for the page.
     * @param aRowSpec The formLayout specification for the page.
     */
    public Page(
        final String aName,
        final String aColumnSpec,
        final String aRowSpec) {
        this.name = aName;
        this.columnSpec = aColumnSpec;
        this.rowSpec = aRowSpec;
    }

    /**
     * Instantiates a new page.
     */
    public Page() {
    }

    /**
     * Sets the name.
     * @param aName the name to set
     */
    public void setName(
        final String aName) {
        this.name = aName;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the parent.
     * @return the parent
     */
    public Page getParent() {
        return parent;
    }

    /**
     * Sets the parent.
     * @param aParent the new parent
     */
    public void setParent(
        final Page aParent) {
        this.parent = aParent;
    }

    /**
     * Gets the children.
     * @return the children
     */
    public List<Page> getChildren() {
        return children;
    }

    /**
     * Gets the row spec.
     * @return the row spec
     */
    public String getRowSpec() {
        return rowSpec;
    }

    /**
     * Sets the row spec.
     * @param aRowSpec the new row spec
     */
    public void setRowSpec(
        final String aRowSpec) {
        this.rowSpec = aRowSpec;
    }

    /**
     * Gets the column spec.
     * @return the column spec
     */
    public String getColumnSpec() {
        return columnSpec;
    }

    /**
     * Sets the column spec.
     * @param aColumnSpec the new column spec
     */
    public void setColumnSpec(
        final String aColumnSpec) {
        this.columnSpec = aColumnSpec;
    }

    /**
     * Gets the panel.
     * @return the panel
     */
    public JPanel getPanel() {
        if (updatePanel) {
            buildPanel();
        }
        return panel;
    }

    /**
     * Sets the panel.
     * @param aPanel the new panel
     */
    public void setPanel(
        final JPanel aPanel) {
        updatePanel = false;
        this.panel = aPanel;
    }

    /**
     * Adds the.
     * @param index the index
     * @param element the element
     */
    public void add(
        final int index,
        final Page element) {
        children.add(
            index,
            element);
        element.setParent(this);
    }

    /**
     * Adds the.
     * @param e the e
     * @return true, if successful
     */
    public boolean add(
        final Page e) {
        e.setParent(this);
        return children.add(e);
    }

    /**
     * Clear.
     */
    public void clear() {
        children.clear();
    }

    /**
     * Gets the.
     * @param index the index
     * @return the page
     */
    public Page get(
        final int index) {
        return children.get(index);
    }

    /**
     * Removes the.
     * @param index the index
     * @return the page
     */
    public Page remove(
        final int index) {
        return children.remove(index);
    }

    /**
     * Removes the.
     * @param o the o
     * @return true, if successful
     */
    public boolean remove(
        final Object o) {
        return children.remove(o);
    }

    /**
     * Gets the gui objects.
     * @return the guiObjects
     */
    public List<GUIObject> getGuiObjects() {
        return guiObjects;
    }

    /**
     * Sets the gui objects.
     * @param aGuiObjects the guiObjects to set
     */
    public void setGuiObjects(
        final List<GUIObject> aGuiObjects) {
        this.guiObjects = aGuiObjects;
    }

    /**
     * Builds the panel.
     */
    private void buildPanel() {
        /* try { */
        /* final FormLayout layout = new FormLayout(columnSpec, rowSpec); */
        /* final DefaultFormBuilder builder = new DefaultFormBuilder(layout); */
        /* for (final GUIObject guiObject : guiObjects) { */
        /* builder.add(guiObject, guiObject.getConstraints()); */
        // }
        /* panel = builder.getPanel(); */
        // } catch (final Exception e) {
        // //
        /* panel = new JPanel(); */
        // } finally {
        //
        // }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }
}
