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

import java.util.ArrayList;

import n.reporting.Reporting;
import xmet.profiles.contacts.ContactsInformation;
import xmet.profiles.geometry.SpatialExtent;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.Settable;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * Variation of SetableIC that is just for Items and CompositeItems.
 * @author Nahid Akbar
 */
public class ItemIC
    extends SetableIC {

    /** The item this IC belongs to. */
    private final Item item;

    /**
     * Instantiates a new item ic.
     * @param sheet the sheet
     * @param parent the parent
     * @param aItem the item
     */
    public ItemIC(
        final Sheet sheet,
        final ParentItem parent,
        final Item aItem) {
        super(sheet, parent);
        this.item = aItem;
    }

    /*
     * == called when user modifies anything regarding the item ================
     */
    /**
     * Call when modifying the value of item.
     */
    public void executeOnChange() {
        if (isInitialized()) {
            if (item.getOnDataChange() != null) {
                CodeExecutorUtil.executeCode(
                    getSheet(),
                    item,
                    item.getOnDataChange());
            }
        }
    }

    /**
     * Call when loading the value of item.
     */
    public void executeOnLoad() {
        if (isInitialized()) {
            if (item.getOnDataLoad() != null) {
                CodeExecutorUtil.executeCode(
                    getSheet(),
                    item,
                    item.getOnDataLoad());
            }
        }
    }

    /* == Spetial helper methods for Composite Items == */

    /**
     * Gets the spatial extent values.
     * @return the spatial extent values
     */
    public Object[] getSpatialExtentValues() {
        if (item instanceof CompositeItem) {
            final CompositeItem aItem = (CompositeItem) this.item;
            if (getEntity() != null) {
                final Entity parent = getEntity().getParent();
                if (parent instanceof Repeated) {
                    final Repeated repeated = (Repeated) parent;
                    final ArrayList<SpatialExtent> ses =
                        new ArrayList<SpatialExtent>();
                    for (final Entity entity : repeated.getEntities()) {
                        final Entity subE = ModelUtils.softTraceXPath(
                            entity,
                            aItem.getRelative());
                        if (subE != null) {
                            final SpatialExtent se =
                                getProfile().getSpatialCodec(
                                    getContext()).extractSpatialExtent(
                                    subE);
                            if (se != null) {
                                ses.add(se);
                            }
                        }
                    }
                    return ses.toArray(new SpatialExtent[ses.size()]);
                }
            }
        }
        return new Object[0];
    }

    /**
     * Gets the contact info values.
     * @return the contact info values
     */
    public Object[] getContactInfoValues() {
        if (item instanceof CompositeItem) {
            final CompositeItem aItem = (CompositeItem) this.item;
            if (getEntity() != null) {
                final Entity parent = getEntity().getParent();
                if (parent instanceof Repeated) {
                    final Repeated repeated = (Repeated) parent;
                    final ArrayList<ContactsInformation> ses =
                        new ArrayList<ContactsInformation>();
                    for (final Entity entity : repeated.getEntities()) {
                        final Entity subE = ModelUtils.softTraceXPath(
                            entity,
                            aItem.getRelative());
                        if (subE != null) {
                            final ContactsInformation se =
                                getProfile().getContactsCodec(
                                    getContext()).extractContactInformation(
                                    subE);
                            if (se != null) {
                                ses.add(se);
                            }
                        }
                    }
                    return ses.toArray(new ContactsInformation[ses.size()]);
                }
            }
        }
        return new Object[0];
    }

    /**
     * Gets the values.
     * @return the values
     */
    public Object[] getValues() {
        if (item instanceof CompositeItem) {
            final CompositeItem aItem = (CompositeItem) this.item;
            if (getEntity() != null) {
                final Entity parent = getEntity().getParent();
                if (parent instanceof Repeated) {
                    final Repeated repeated = (Repeated) parent;
                    final ArrayList<String> ses = new ArrayList<String>();
                    for (final Entity entity : repeated.getEntities()) {
                        final Entity subE = ModelUtils.softTraceXPath(
                            entity,
                            aItem.getRelative());
                        if (subE != null) {
                            final Settable se = ModelUtils.getSetable(subE);
                            if ((se != null)
                                && (se.getValue() != null)) {
                                ses.add(se.getValue());
                            }
                        }
                    }
                    return ses.toArray(new String[ses.size()]);
                }
            }
        }
        return new String[0];
    }

    /**
     * Sets the spatial extent values.
     * @param values the new spatial extent values
     */
    public void setSpatialExtentValues(
        final Object[] values) {
        if (item instanceof CompositeItem) {
            final CompositeItem aItem = (CompositeItem) this.item;
            if (getEntity() != null) {
                final Entity parent = getEntity().getParent();
                if (parent instanceof Repeated) {
                    final Repeated repeated = (Repeated) parent;
                    setModified(true);
                    repeated.getEntities().clear();
                    for (final Object object : values) {
                        if (object instanceof SpatialExtent) {
                            final SpatialExtent value = (SpatialExtent) object;
                            final Entity e = repeated.addNewEntity();
                            if (e != null) {
                                final Entity ex = ModelUtils.hardTraceXPath(
                                    e,
                                    aItem.getRelative());
                                if (ex != null) {
                                    getProfile().getSpatialCodec(
                                        getContext()).insertSpatialExtent(
                                        value,
                                        ex);
                                } else {
                                    Reporting
                                        .logExpected("could not trace xpath");
                                }
                            } else {
                                Reporting.logExpected("could not add "
                                    + "new repeated entity");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the contact info values.
     * @param values the new contact info values
     */
    public void setContactInfoValues(
        final Object[] values) {
        if (item instanceof CompositeItem) {
            final CompositeItem aItem = (CompositeItem) this.item;
            if (getEntity() != null) {
                final Entity parent = getEntity().getParent();
                if (parent instanceof Repeated) {
                    final Repeated repeated = (Repeated) parent;
                    repeated.getEntities().clear();
                    setModified(true);
                    for (final Object object : values) {
                        if (object instanceof ContactsInformation) {
                            final ContactsInformation value =
                                (ContactsInformation) object;
                            final Entity e = repeated.addNewEntity();
                            if (e != null) {
                                final Entity ex = ModelUtils.hardTraceXPath(
                                    e,
                                    aItem.getRelative());
                                if (ex != null) {
                                    getProfile().getContactsCodec(
                                        getContext()).insertContactInformation(
                                        value,
                                        ex);
                                } else {
                                    Reporting
                                        .logExpected("could not trace xpath");
                                }
                            } else {
                                Reporting.logExpected("could not add "
                                    + "new repeated entity");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the values.
     * @param values the new values
     */
    public void setValues(
        final Object[] values) {
        if (item instanceof CompositeItem) {
            final CompositeItem aItem = (CompositeItem) this.item;
            if (getEntity() != null) {
                final Entity parent = getEntity().getParent();
                if (parent instanceof Repeated) {
                    final Repeated repeated = (Repeated) parent;
                    repeated.getEntities().clear();
                    setModified(true);
                    for (final Object object : values) {
                        if (object instanceof String) {
                            final String value = (String) object;
                            final Entity e = repeated.addNewEntity();
                            if (e != null) {
                                final Settable ex =
                                    ModelUtils.hardTraceSetableXpath(
                                        e,
                                        aItem.getRelative());
                                if (ex != null) {
                                    ex.setValue(value);
                                } else {
                                    Reporting
                                        .logExpected("could not trace xpath");
                                }
                            } else {
                                Reporting.logExpected("could not add "
                                    + "new repeated entity");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the keywords list values.
     * @param values the new keywords list values
     */
    public void setKeywordsListValues(
        final Object[] values) {
        Reporting.logUnexpected();
    }

}
