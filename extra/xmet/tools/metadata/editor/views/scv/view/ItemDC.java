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
package xmet.tools.metadata.editor.views.scv.view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import n.java.ReflectionUtils;
import n.reporting.Reporting;
import xmet.ClientContext;
import xmet.profiles.catalogs.model.Codelist;
import xmet.profiles.catalogs.model.CodelistCatalog;
import xmet.tools.metadata.editor.views.scv.impl.CompositeItem;
import xmet.tools.metadata.editor.views.scv.impl.EditorType;
import xmet.tools.metadata.editor.views.scv.impl.Item;
import xmet.tools.metadata.editor.views.scv.impl.Param;
import xmet.tools.metadata.editor.views.scv.utils.SCVUtils;
import xmet.ui.controls.GUIObject;

/**
 * DC for Item.
 * @author Nahid Akbar
 */
@SuppressWarnings("rawtypes")
public class ItemDC
    extends DisplayContext
    implements
    Observer {

    /** The Constant MAX_COLUMN_SPAN. */
    private static final int MAX_COLUMN_SPAN = 30;

    /* == Item Label == */

    /** The item label. */
    private JLabel itemLabel;

    /**
     * Gets the item label.
     * @return the item label
     */
    public JLabel getItemLabel() {
        return itemLabel;
    }

    /**
     * Sets the item label.
     * @param label the new item label
     */
    public void setItemLabel(
        final JLabel label) {
        itemLabel = label;
    }

    /**
     * Checks for label.
     * @return true, if successful
     */
    public boolean hasItemLabel() {
        return itemLabel == null;
    }

    /* == the GUIObject associated with this item == */
    /** The control. */
    private SCVGUIObjectWrapper control;

    /**
     * Gets the control.
     * @return the control
     */
    public SCVGUIObjectWrapper getControl() {
        return control;
    }

    /**
     * Sets the control.
     * @param aControl the new control
     */
    public void setControl(
        final SCVGUIObjectWrapper aControl) {
        this.control = aControl;
    }

    /* == the item this belongs to == */

    /** The item. */
    private final Item item;

    /**
     * Instantiates a new item display context.
     * @param aItem the item
     * @param aItemLabel the item label
     * @param itemHover the item hover
     * @param context the context
     */
    public ItemDC(
        final Item aItem,
        final JLabel aItemLabel,
        final String itemHover,
        final ClientContext context) {
        this.item = aItem;
        try {
            final Class classByName =
                ReflectionUtils.getClassByName(("xmet.ui.controls." + aItem
                    .getType()));
            GUIObject pcontrol = null;

            try {
                pcontrol = (GUIObject) classByName.newInstance();
            } catch (final Exception e) {
                if (!(e instanceof InstantiationException)) {
                    e.printStackTrace();
                }
            }
            if (pcontrol == null) {
                pcontrol = (GUIObject) ReflectionUtils.getNewInstanceOfClass(
                    classByName,
                    context);
            }
            pcontrol.setContext(context);
            control = new SCVGUIObjectWrapper(
                aItem.getIc().getContext(),
                pcontrol);
            if (aItem.isMandatory()) {
                control.setValidationErrorMessage("item is mandatory");
            }
            control.setHelpContextID(aItem.getHelpContextID());
            control.rebuildPanel();
        } catch (final Exception e) {
            Reporting.reportUnexpected(e);
        }

        initControlParams(aItem);

        if (control != null) {
            control.addObserver(this);

            if (!SCVUtils.doesAutoScale(aItem.getType())) {
                // CHECKSTYLE OFF: MagicNumber
                int width = 100;
                int height = 30;

                height = height
                    * getColumnSpan();
                control.setPreferredSize(new Dimension(
                    width,
                    height));
                width = 99999;
                control.setMaximumSize(new Dimension(
                    width,
                    height));
                // CHECKSTYLE ON: MagicNumber
            }
        }

        if ((aItem.getType() != EditorType.Label)
            && aItemLabel != null) { /* Labels */
            /* are */
            /* wide */
            this.itemLabel = aItemLabel;
            aItemLabel.setToolTipText(itemHover);
        }
    }

    /**
     * Inits the control params.
     * @param aItem the item
     */
    private void initControlParams(
        final Item aItem) {
        if ((control != null)
            && (aItem.getParams() != null)) {
            for (final Param param : aItem.getParams()) {
                if ((param.getName() != null)
                    && (param.getName().trim().length() > 0)) {
                    try {
                        String name = param.getName().trim();
                        name = ReflectionUtils.getSetName(name);
                        /* final Method setMethod = ReflectionUtils */
                        // .getClassMethodByName(control.getGuiObject()
                        // .getClass(), name);
                        final String value = param.getValue().trim();
                        if (name.equals("setCodeList")) {
                            if (value.indexOf('|') != -1) {
                                final String[] urls = value.split("\\|");
                                for (final String string : urls) {
                                    final Codelist c =
                                        aItem
                                            .getIc()
                                            .getProfile()
                                            .getCodelistByURL(
                                                string,
                                                aItem.getIc().getContext());
                                    if (c != null) {
                                        ReflectionUtils
                                            .callMethodByNameWithParams(
                                                control.getGuiObject(),
                                                name,
                                                c);
                                    }
                                }
                            } else {
                                final Codelist c =
                                    aItem
                                        .getIc()
                                        .getProfile()
                                        .getCodelistByURL(
                                            value,
                                            aItem.getIc().getContext());
                                if (c != null) {
                                    ReflectionUtils.callMethodByNameWithParams(
                                        control.getGuiObject(),
                                        name,
                                        c);
                                }
                            }
                        } else if (name.equals("setCatalog")) {
                            if (value.indexOf('|') != -1) {
                                final String[] urls = value.split("\\|");
                                for (final String string : urls) {
                                    final CodelistCatalog c =
                                        aItem
                                            .getIc()
                                            .getProfile()
                                            .getCatalogByURL(
                                                string,
                                                aItem.getIc().getContext());

                                    if (c != null) {
                                        ReflectionUtils
                                            .callMethodByNameWithParams(
                                                control.getGuiObject(),
                                                name,
                                                c);
                                    }
                                }
                            } else {
                                final CodelistCatalog c =
                                    aItem.getIc().getProfile().getCatalogByURL(
                                        value,
                                        aItem.getIc().getContext());
                                if (c != null) {
                                    ReflectionUtils.callMethodByNameWithParams(
                                        control.getGuiObject(),
                                        name,
                                        c);
                                }
                            }
                        } else {
                            ReflectionUtils.callMethodByNameWithParams(
                                control.getGuiObject(),
                                name,
                                value);
                        }
                    } catch (final Throwable e) {
                        e.printStackTrace();
                        Reporting.logExpected(
                            "%1$s not found",
                            param.getName().trim());
                    }
                } else {
                    Reporting.reportUnexpected(
                        "invalid param name %1$s",
                        param.getName());
                }
            }
        } /* params == null */
    }

    /* == Misc Implementation == */
    /**
     * This method returns a column span value for an item.
     * @return the column span
     */
    private int getColumnSpan() {
        /*
         * get setting from the item but if the setting is not set or invalid,
         * get the default from the control
         */
        if ((item.getColumnSpan() > 0)
            && (item.getColumnSpan() < MAX_COLUMN_SPAN)) {
            return item.getColumnSpan();
        }
        return SCVUtils.getPreferredColumnSpan(item.getType());
    }

    /**
     * Gets the preferred column span.
     * @return the preferred column span
     */
    public int getPreferredColumnSpan() {
        return getColumnSpan();
    }

    /**
     * Load set value.
     * @param value the value
     */
    public void loadSetValue(
        final String value) {
        if (control != null) {
            control.setValue(value);
            onChangeUpdate();
        }
    }

    /**
     * Sets the default value.
     * @param value the new default value
     */
    public void setDefaultValue(
        final String value) {
        if (control != null) {
            control.setValue(value);
            onDefaultValueLoad();
        }
    }

    /**
     * Load value.
     */
    public void loadValue() {
        if (control != null) {
            final boolean validXpathItem = (item.getXpath() != null)
                && (item.getXpath().trim().length() > 0);
            final boolean validCompositeItem =
                (item.getClass() == CompositeItem.class)
                    && (((CompositeItem) item).getBase() != null)
                    && (((CompositeItem) item).getBase().length() > 0);
            if ((validXpathItem || validCompositeItem)) {
                if (item.getClass() == CompositeItem.class) {
                    if (control.isCompositeControl()) {
                        if (control.isSEGUIObject()) {
                            control.setValues(item
                                .getIc()
                                .getSpatialExtentValues());
                        } else if (control.isCIGUIObject()) {
                            control.setValues(item
                                .getIc()
                                .getContactInfoValues());
                        } else {
                            control.setValues(item.getIc().getValues());
                        }
                    } else {
                        Reporting.logUnexpected("Expecting composite control");
                    }
                } else {
                    if (control.isSEGUIObject()) {
                        control.setSpatialExtent(item
                            .getIc()
                            .getSpatialExtentValue());
                    } else if (control.isCIGUIObject()) {
                        control.setContactInformation(item
                            .getIc()
                            .getContactInfoValue());
                    } else if (control.isKLGUIObject()) {
                        control.setKeywordsList(item
                            .getIc()
                            .getKeywordsListValue());
                    } else {
                        control.setValue(item.getIc().getSetableValue());
                    }
                }
            }
            if ((item.getType() == EditorType.Label)
                && !validXpathItem) {
                if (item.getDefaultValue() != null) {
                    control.setValue(item.getDefaultValue().getValuetrim());
                }
            }
        }
    }

    /**
     * Save value.
     */
    private void saveValue() {
        if (control != null) {
            if (item.getClass() == CompositeItem.class) {
                if (control.isSEGUIObject()) {
                    item.getIc().setSpatialExtentValues(
                        control.getValues());
                } else if (control.isCIGUIObject()) {
                    item.getIc().setContactInfoValues(
                        control.getValues());
                } else if (control.isKLGUIObject()) {
                    item.getIc().setKeywordsListValues(
                        control.getValues());
                } else {
                    item.getIc().setValues(
                        control.getValues());
                }
            } else {
                if (control.isSEGUIObject()) {
                    item.getIc().setSpatialExtentValue(
                        control.getSpatialExtent());
                } else if (control.isCIGUIObject()) {
                    item.getIc().setContactInfoValue(
                        control.getContactInformation());
                } else if (control.isKLGUIObject()) {
                    item.getIc().setKeywordsListValue(
                        control.getKeywordsList());
                } else {
                    item.getIc().setSetableValue(
                        control.getValue());
                }
            }
        }
    }

    /* == DC Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public Component getDisplayPanel() {
        return control;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildDisplayPanel() {
        /* nothing to do */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshPanel() {
        /* nothing to do */
    }

    /* == Observer Implementation == */
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(
        final Observable o,
        final Object arg) {
        onChangeUpdate();
    }

    /**
     * On change update.
     */
    public void onChangeUpdate() {
        saveValue();
        item.getIc().reValidate();
        item.getIc().executeOnChange();
    }

    /**
     * On load update.
     */
    public void onLoadUpdate() {
        item.getIc().reValidate();
        item.getIc().executeOnLoad();
    }

    /**
     * On default value load.
     */
    public void onDefaultValueLoad() {
        saveValue();
    }

    /**
     * Update panel.
     */
    public void updatePanel() {
        control.setValidationErrorMessage(getValidationErrorMessage());
        control.updatePanel();
    }

    /**
     * Mandatory changed.
     */
    public void mandatoryChanged() {
        item.getIc().reValidate();
    }

}
