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
package xmet.tools.metadata.editor.views.scv.utils;

import java.util.HashMap;

import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.config.Config;
import xmet.profiles.Profile;
import xmet.profiles.ProfileManager;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Settable;
import xmet.resources.ResourceManager;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.NamedItem;
import xmet.tools.metadata.editor.views.scv.impl.PageSubitem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;
import xmet.tools.metadata.editor.views.scv.view.DisplayContext;

/**
 * Parent class of all InitializationContexts for various model elements
 * Initialization context is the glue that binds other aspects in the client
 * such as resource manager or the model element or what not else which may be
 * conceptually unrelated to the model but is needed to make the model usable.
 * All initialization context must inherit from this.
 * @author Nahid Akbar
 */
public abstract class InitializationContext {

    /** Link to the root element. */
    private final Sheet sheet;

    /** The parent item of this item this is the initial context of. */
    private final ParentItem parent;

    /** The node xpath. */
    private String nodeXpath = null;

    /* == Constructors == */
    /**
     * Instantiates a new initialization context.
     * @param aSheet the sheet
     * @param aParent the parent
     */
    public InitializationContext(
        final Sheet aSheet,
        final ParentItem aParent) {
        super();
        this.sheet = aSheet;
        this.parent = aParent;
    }

    /* == helper getters and setters == */

    /**
     * Gets the sheet.
     * @return the sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * Gets the parent.
     * @return the parent
     */
    public ParentItem getParent() {
        return parent;
    }

    /**
     * Gets the profile this sheet is currently being applied to.
     * @return the profile
     */
    public Profile getProfile() {
        return sheet.getIc().getProfile();
    }

    /**
     * Gets the profile manager.
     * @return the profile manager
     */
    public ProfileManager getProfileManager() {
        return sheet.getIc().getClient().getProfiles();
    }

    /**
     * Gets the client context.
     * @return the client
     */
    public ClientContext getContext() {
        return sheet.getIc().getClient();
    }

    /**
     * Gets the resource manager.
     * @return the resource manager
     */
    public ResourceManager getResourceManager() {
        return sheet.getIc().getClient().getResources();
    }

    /**
     * Gets the configuration.
     * @return the config
     */
    public Config getConfig() {
        return sheet.getIc().getClient().getConfig();
    }

    /**
     * Checks if it is initialized.
     * @return true, if is initialized
     */
    public boolean isInitialized() {
        return (sheet != null)
            && sheet.getIc().isOnLoadExecuted();
    }

    /**
     * Sets the modified flag to true.
     * @param b the new modified
     */
    public void setModified(
        final boolean b) {
        SCVUtils.setSheetModified(
            sheet,
            b);
    }

    /**
     * Gets the root of the data model.
     * @return the root
     */
    public Entity getRoot() {
        return sheet.getIc().getRoot();
    }

    /**
     * Re validate.
     */
    public void reValidate() {
        if (parent != null
            && parent.getIC() != null) {
            parent.getIC().reValidate();
        } else {
            Reporting.logUnexpected(
                "failed revalidate %1$s",
                getItem());
        }
    }

    /**
     * Gets the path at value.
     * @param xpath the xpath
     * @return the path value
     */
    public String getPathValue(
        final String xpath) {
        final Entity entity = ModelUtils.softTraceXPath(
            sheet.getIc().getRoot(),
            xpath);
        if (entity != null) {
            final Settable setable = ModelUtils.getSetable(entity);
            if (setable != null) {
                return setable.getValue();
            } else {
                Reporting.reportUnexpected("xpath "
                    + xpath
                    + " does not point to anything getable");
            }
        } else {
            Reporting.reportUnexpected(
                "getPathValue: xpath %1$s could not be traced.",
                xpath);
        }
        return null;
    }

    /**
     * Sets the path value.
     * @param xpath the xpath
     * @param value the value
     */
    public void setPathValue(
        final String xpath,
        final String value) {

        SCVUtils.setSheetModified(
            sheet,
            true);

        final Entity entity = ModelUtils.hardTraceXPath(
            sheet.getIc().getRoot(),
            xpath);
        if (entity != null) {
            final Settable setable = ModelUtils.getSetable(entity);
            if (setable != null) {
                setable.setValue(value);
            } else {
                Reporting.reportUnexpected("xpath "
                    + xpath
                    + " does not point to anything setable");
            }
        } else {
            Reporting.reportUnexpected("setpathValue: xpath "
                + xpath
                + " could not be traced.");
        }

    }

    /* == When item visibility changes == */
    /**
     * Visibility changed.
     * @param item the item
     */
    public void visibilityChanged(
        final PageSubitem item) {
        /* rebuild parent item */
        if (item.getDC() != null) {
            DisplayContext.rebuildParent(this);
        }
    }

    /* == Named Item Processing == */
    /** The named items. */
    private HashMap<String, NamedItem> namedItems = null;

    /**
     * Gets a named item by name.
     * @param name the name
     * @return the named item
     */
    public NamedItem getNamedItem(
        final String name) {
        /* either it is in the current context or one context above if it */
        /* happens to be a global name */
        if (namedItems != null) {
            final NamedItem item = namedItems.get(name);
            if (item != null) {
                return item;
            }
        }
        if ((parent != null)
            && (parent.getIC() != null)) {
            return parent.getIC().getNamedItem(
                name);
        }
        return null;
    }

    /**
     * Checks if is global name.
     * @param name the name
     * @return true, if is global name
     */
    public static boolean isGlobalName(
        final String name) {
        return (name != null)
            && name.startsWith("$");
    }

    /**
     * Checks if is global name.
     * @param name the name
     * @return true, if is global name
     */
    public static boolean isParentName(
        final String name) {
        return (name != null)
            && name.startsWith("^");
    }

    /**
     * Checks if is queue name.
     * @param name the name
     * @return true, if is queue name
     */
    public static boolean isQueueName(
        final String name) {
        return (name != null)
            && name.endsWith("!");
    }

    /**
     * Adds the named item.
     * @param name the name
     * @param item the item
     */
    public void addNamedItem(
        final String name,
        final NamedItem item) {
        if (isGlobalName(name)
            && parent != null
            && parent.getIC() != null) {
            parent.getIC().addNamedItem(
                name,
                item);
            return;
        }
        if (isParentName(name)
            && parent != null
            && parent.getIC() != null) {
            item.setName(name.substring(1));
            parent.getIC().addNamedItem(
                name.substring(1),
                item);
            return;
        }
        addToNamesList(
            name,
            item);
    }

    /**
     * Removes the named item.
     * @param name the name
     * @param item the item
     */
    public void removeNamedItem(
        final String name,
        final NamedItem item) {
        if (isGlobalName(name)
            && parent != null
            && parent.getIC() != null) {
            parent.getIC().removeNamedItem(
                name,
                item);
            return;
        }
        removeFromNamesList(
            name,
            item);
    }

    /* NamesListMonitoringUtil util; */

    /**
     * Adds the to names list.
     * @param name the name
     * @param item the item
     */
    protected void addToNamesList(
        final String name,
        final NamedItem item) {
        if (namedItems == null) {
            namedItems = new HashMap<String, NamedItem>();
            /* util = new NamesListMonitoringUtil(this); */
        }

        final NamedItem itm = namedItems.get(name);
        if (itm == null) {
            namedItems.put(
                name,
                item);
        } else {
            if (!isQueueName(name)) { /* ignore */
                if (itm instanceof ListManipulableItem) {
                    ((ListManipulableItem) itm).add(item);
                } else {
                    final ListManipulableItem list =
                        new ListManipulableItem(name);
                    list.add(itm);
                    list.add(item);
                    namedItems.put(
                        name,
                        list);
                }
            } else {
                if (itm instanceof QueueManipulableItem) {
                    ((QueueManipulableItem) itm).add(item);
                } else {
                    final QueueManipulableItem list =
                        new QueueManipulableItem(name);
                    list.add(itm);
                    list.add(item);
                    namedItems.put(
                        name,
                        list);
                }
            }
        }
    }

    /**
     * Removes the from names list.
     * @param name the name
     * @param item the item
     */
    protected void removeFromNamesList(
        final String name,
        final NamedItem item) {
        if (namedItems != null) {
            final NamedItem itm = namedItems.get(name);
            if (itm != null) {

                if (itm instanceof ListManipulableItem) {
                    ((ListManipulableItem) itm).remove(item);
                    if (((ListManipulableItem) itm).size() == 0) {
                        namedItems.remove(name);
                    }
                } else if (itm instanceof QueueManipulableItem) {
                    ((QueueManipulableItem) itm).remove(item);
                    if (((QueueManipulableItem) itm).size() == 0) {
                        namedItems.remove(name);
                    }
                } else {
                    namedItems.remove(name);
                }
            }
            if (namedItems.size() == 0) {
                /*
                 * util.dispose(); util = null;
                 */
                namedItems = null;
            }
        }
    }

    /**
     * Gets the named items.
     * @return the named items
     */
    public HashMap<String, NamedItem> getNamedItems() {
        return namedItems;
    }

    /**
     * Gets the item.
     * @return the item
     */
    public abstract ModelItem getItem();

    /**
     * Gets the node xpath.
     * @return the node xpath
     */
    public String getNodeXpath() {
        return nodeXpath;
    }

    /**
     * Sets the node xpath.
     * @param aNodeXpath the new node xpath
     */
    public void setNodeXpath(
        final String aNodeXpath) {
        this.nodeXpath = aNodeXpath;
    }

}
