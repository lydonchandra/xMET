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

import n.reporting.Reporting;
import xmet.profiles.contacts.ContactsInformation;
import xmet.profiles.geometry.SpatialExtent;
import xmet.profiles.keywords.KeywordsList;
import xmet.profiles.model.Entity;
import xmet.profiles.model.ModelUtils;
import xmet.profiles.model.Repeated;
import xmet.profiles.model.Settable;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * This represents an IC that is mapped to an entity to the data model.
 * @author Nahid Akbar
 */
public class EntityIC
    extends InitializationContext {

    /** The entity. */
    private Entity entity = null;

    /* TODO: change so a wide range of conditions can be fulfilled */
    /** The operand. */
    private String operand = null;

    /** The equals operator. */
    private boolean equalsOperator = true;

    /* == Constructors == */

    /**
     * Instantiates a new entity ic.
     * @param sheet the sheet
     * @param parent the parent
     */
    public EntityIC(
        final Sheet sheet,
        final ParentItem parent) {
        super(sheet, parent);
    }

    /* == Helper getters and setters == */
    /**
     * Gets the validation error from model.
     * @return the validation error
     */
    public String getValidationError() {
        return entity.getValidationError();
    }

    /**
     * Sets the setable value from this entity.
     * @param toSet the new setable value
     */
    public void setSetableValue(
        final String toSet) {
        final Settable setable = ModelUtils.getSetable(entity);
        if (setable != null) {
            setable.setValue(toSet);
            setModified(true);
        }
    }

    /**
     * Gets the setable value from this entity.
     * @return the setable value
     */
    public String getSetableValue() {
        final Settable setable = ModelUtils.getSetable(entity);
        if (setable != null) {
            if (setable.getValue() != null) {
                return setable.getValue().trim();
            }
        }
        return null;
    }

    /**
     * Gets the spatial extent value from this entity.
     * @return the spatial extent value
     */
    public SpatialExtent getSpatialExtentValue() {
        if (entity != null) {
            return getProfile().getSpatialCodec(
                getContext()).extractSpatialExtent(
                entity);
        } else {
            Reporting.logUnexpected();
        }
        return null;
    }

    /**
     * Sets the spatial extent value.
     * @param spatialExtent the new spatial extent value
     */
    public void setSpatialExtentValue(
        final SpatialExtent spatialExtent) {
        if (entity != null) {
            getProfile().getSpatialCodec(
                getContext()).insertSpatialExtent(
                spatialExtent,
                entity);
            setModified(true);
        } else {
            Reporting.logUnexpected();
        }
    }

    /**
     * Gets the contact info value from this entity.
     * @return the contact info value
     */
    public ContactsInformation getContactInfoValue() {
        if (entity != null) {
            return getProfile().getContactsCodec(
                getContext()).extractContactInformation(
                entity);
        } else {
            Reporting.logUnexpected();
        }
        return null;
    }

    /**
     * Gets the keywords list value.
     * @return the keywords list value
     */
    public KeywordsList getKeywordsListValue() {
        if (entity != null) {
            return getProfile().getKeywordsListCodec(
                getContext()).extractKeywordsList(
                entity);
        } else {
            Reporting.logUnexpected();
        }
        return null;
    }

    /**
     * Sets the contact info value to this entity.
     * @param contactInformation the new contact info value
     */
    public void setContactInfoValue(
        final ContactsInformation contactInformation) {
        if (entity != null) {
            getProfile().getContactsCodec(
                getContext()).insertContactInformation(
                contactInformation,
                entity);
            setModified(true);
        } else {
            Reporting.logUnexpected();
        }
    }

    /**
     * Sets the keywords list value.
     * @param keywordsList the new keywords list value
     */
    public void setKeywordsListValue(
        final KeywordsList keywordsList) {
        Reporting.logExpected("setKeywordsListValue");
        if (entity != null) {
            getProfile().getKeywordsListCodec(
                getContext()).insertKeywordsList(
                keywordsList,
                entity);
            setModified(true);
        } else {
            Reporting.logUnexpected();
        }
    }

    /**
     * Gets the entity.
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the entity.
     * @param aEntity the new entity
     */
    public void setEntity(
        final Entity aEntity) {
        this.entity = aEntity;
    }

    /* == Methods to do with tracing the entity == */

    /**
     * Method tries to locate the entity in the model Do not use this method as
     * it does not set the last trace xpath. It was put here to be overridden by
     * children for spetialized xpath tracing
     * @param xpath the xpath
     * @param hardTrace the hard trace
     */
    protected void traceXpath(
        final String xpath,
        final boolean hardTrace) {
        /* Reporting.log("traceXpath(%1$s, %2$s)", xpath, hardTrace ? "true" */
        // : "false");
        String varXpath = xpath;
        if (varXpath != null
            && varXpath.trim().length() > 0
            && !varXpath.startsWith("$")
            && !varXpath.startsWith("%")) {
            // {
            int i;
            if ((varXpath.indexOf('!')) != -1) {
                i = varXpath.indexOf('!');
                operand = varXpath.substring(i + 1);
                varXpath = varXpath.substring(
                    0,
                    i);
                equalsOperator = false;
            } else if ((varXpath.indexOf('=')) != -1) {
                i = varXpath.indexOf('=');
                operand = varXpath.substring(i + 1);
                varXpath = varXpath.substring(
                    0,
                    i);
                equalsOperator = true;
            } else {
                operand = null;
                equalsOperator = true;
            }
            // }
            try {
                if (hardTrace) {
                    entity = ModelUtils.hardTraceXPath(
                        getRoot(),
                        varXpath);
                    if (operand != null) {
                        final Settable setable = ModelUtils.getSetable(entity);
                        if (setable != null) {
                            if (equalsOperator) {
                                /* Reporting.log("set value %1$s", operand); */
                                setable.setValue(operand);
                            } else {
                                /* if (setable.getValue() == null) { */
                                setable.setValue(" ");
                                // }
                            }
                        }
                    }
                    setModified(true);
                } else {
                    entity = ModelUtils.softTraceXPath(
                        getRoot(),
                        varXpath);
                }
            } catch (final Throwable e) {
                e.printStackTrace();
            }
            if ((entity == null)
                && hardTrace) {
                Reporting.logExpected(
                    "Could not trace %1$s",
                    varXpath);
            }
        } else {
            entity = null;
        }
    }

    /** The last traced xpath. */
    private String lastParentPath;

    /**
     * Gets the last parent path.
     * @return the last parent path
     */
    public String getLastParentPath() {
        return lastParentPath;
    }

    /**
     * Trace substitutable xpath.
     * @param substitution the substitution
     * @param substitutable the base - can contain $ which will be substituted
     *            by substitution
     * @param hardTrace the hard trace
     * @return the traced xpath
     */
    private String traceSubstitutableXpath(
        final String substitution,
        final String substitutable,
        final boolean hardTrace) {
        if ((substitution == null)
            && (substitutable != null)
            && (substitutable.startsWith("$"))) {
            return getNodeXpath();
        }
        if ((substitutable != null)
            && (substitutable.startsWith("$"))) {
            if (substitution != null) {
                setNodeXpath(substitutable.replaceAll(
                    "^\\$",
                    substitution));
                lastParentPath = substitution;
            } else {
                Reporting.logExpected("---");
            }
        } else {
            setNodeXpath(substitutable);
        }
        traceXpath(
            getNodeXpath(),
            hardTrace);
        return getNodeXpath();
    }

    /**
     * Hard retrace last xpath.
     */
    public void hardRetraceLastXpath() {
        traceXpath(
            getNodeXpath(),
            true);
    }

    /**
     * Hard trace substitutable xpath.
     * @param substitution the substitution
     * @param substitutable the base - can contain $ which will be substituted
     *            by substitution
     * @return the traced xpath
     */
    public String hardTraceSubstitutableXpath(
        final String substitution,
        final String substitutable) {
        return traceSubstitutableXpath(
            substitution,
            substitutable,
            true);
    }

    /**
     * Soft trace substitutable xpath.
     * @param substitution the substitution
     * @param substitutable the base - can contain $ which will be substituted
     *            by substitution
     * @return the traced xpath
     */
    public String softTraceSubstitutableXpath(
        final String substitution,
        final String substitutable) {
        return traceSubstitutableXpath(
            substitution,
            substitutable,
            false);
    }

    /**
     * Soft retrace xpath.
     * @param substitution the substitution
     * @param substitutable the base - can contain $ which will be substituted
     *            by substitution
     * @return the traced xpath
     */
    public String softRetraceXpath(
        final String substitution,
        final String substitutable) {
        return traceSubstitutableXpath(
            substitution,
            substitutable,
            false);
    }

    /**
     * Soft retrace last xpath.
     */
    public void softRetraceLastXpath() {
        traceXpath(
            getNodeXpath(),
            false);
    }

    /**
     * Checks if is present.
     * @return true, if is present
     */
    public boolean isPresent() {
        boolean traces =
            ((getNodeXpath() != null) && (getNodeXpath().length() > 0));
        if (traces) {
            softRetraceLastXpath();
            traces = entity != null;
            if (traces
                && (operand != null)) {
                final Settable setable = ModelUtils.getSetable(entity);
                if (setable != null) {
                    final String varValue = setable.getValue();
                    return (equalsOperator && operand.equals(varValue))
                        || (!equalsOperator && ((varValue == null) || varValue
                            .isEmpty()));
                }
            }
        }
        return traces;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelItem getItem() {
        return null;
    }

    /**
     * Checks if is xpath present.
     * @param lastTracedXpath the last traced xpath
     * @return true, if is xpath present
     */
    public boolean isXpathPresent(
        final String lastTracedXpath) {
        Entity aEntity;
        aEntity = ModelUtils.softTraceXPath(
            getRoot(),
            lastTracedXpath);
        final boolean ret = aEntity != null
            && ModelUtils.isPresent(aEntity);
        /* Reporting.log("isPresent %1$s %2$s", lastTracedXpath, ret); */
        return ret;
    }

    /**
     * Gets the repeated item.
     * @param lastTracedXpath the last traced xpath
     * @return the repeated item
     */
    public Repeated getRepeatedItem(
        final String lastTracedXpath) {
        final Entity aEntity = ModelUtils.hardTraceXPath(
            getRoot(),
            lastTracedXpath);
        if (aEntity != null) {
            final Entity parent = aEntity.getParent();
            if (parent instanceof Repeated) {
                return (Repeated) parent;
            }
        }
        return null;
    }

}
