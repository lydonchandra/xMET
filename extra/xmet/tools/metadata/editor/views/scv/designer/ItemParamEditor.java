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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xmet.tools.metadata.editor.views.scv.model.Param;

/**
 * Helper superclass for Parameter Editing PSEs.
 * @author Nahid Akbar
 */
public class ItemParamEditor {

    /** The params. */
    private ArrayList<Param> params;

    /**
     * Gets the params.
     * @return the params
     */
    public ArrayList<Param> getParams() {
        return params;
    }

    /**
     * Clear params.
     */
    protected void clearParams() {
        getParams().clear();
    }

    /**
     * Clear params except the allowed list.
     * @param allowedParams the allowed params
     */
    protected void clearParams(
        final String... allowedParams) {
        final Map<String, String> allowed = new HashMap<String, String>();
        for (int i = 0; i < allowedParams.length; i++) {
            allowed.put(
                allowedParams[i],
                allowedParams[i]);
        }
        if (getParams() != null) {
            for (int i = 0; i < getParams().size(); i++) {
                final Param param = getParams().get(
                    i);
                if ((param.getName() == null)
                    || (allowed.get(param.getName()) == null)) {
                    getParams().remove(
                        i--);
                }
            }
        } else {
            assert (false);
        }
    }

    /**
     * Sets the params.
     * @param aParams the new params
     */
    public void setParams(
        final ArrayList<Param> aParams) {
        if (aParams != null) {
            this.params = aParams;
        } else {
            assert (false);
        }
    }

    /**
     * Gets the param texttrim.
     * @param name the name
     * @return the param texttrim
     */
    protected String getParamTexttrim(
        final String name) {
        if (getParams() != null) {
            for (final Param param : getParams()) {
                if ((param.getName() != null)
                    && param.getName().equals(
                        name)
                    && (param.getValue() != null)) {
                    return param.getValue().trim();
                }
            }
        }
        return null;
    }

    /**
     * Sets the param.
     * @param name the name
     * @param text the text
     */
    protected void setParam(
        final String name,
        final String text) {

        String varText = text;
        if ((varText != null)
            && (varText.trim().length() > 0)) {
            varText = varText.trim();
            if (getParams() != null) {
                for (final Param param : getParams()) {
                    if ((param.getName() != null)
                        && param.getName().equals(
                            name)) {
                        param.setValue(varText);
                        return;
                    }
                }
                getParams().add(
                    new Param(
                        name,
                        varText));
            } else {
                assert (false); /*
                                 * If I have to make a new list and return, data
                                 */
                /* will be overwritten if there are more than */
                /* one ItemParamEditor PSE object */
            }
        }
    }

    /**
     * Removes the param.
     * @param name the name
     */
    protected void removeParam(
        final String name) {
        if (getParams() != null) {
            for (int i = 0; i < getParams().size(); i++) {
                final Param param = getParams().get(
                    i);
                if ((param.getName() == null)
                    || param.getName().equals(
                        name)) {
                    getParams().remove(
                        i--);
                }
            }
        } else {
            assert (false);
        }
    }
}
