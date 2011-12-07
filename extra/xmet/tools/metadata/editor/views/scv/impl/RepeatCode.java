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

import n.io.CS;
import n.io.CSC;
import xmet.tools.metadata.editor.views.scv.utils.RepeatedIC;
import xmet.tools.metadata.editor.views.scv.view.RepeatedItemDC;

/**
 * Code that repeats a subcode for all the sub elements of a repeated entity.
 * @author Nahid Akbar
 */
@CSC("repeated")
public class RepeatCode
    extends Code
    implements
    RepeatedItem<Code> {

    /* == Properties == */

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** path to the repeated element. */
    @CS
    private String base;

    /** code to execute. */
    @CSC
    private Code code;

    /** The ic. */
    private transient RepeatedIC<Code> ic;

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatedIC<Code> getRIC() {
        return getIc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatedItemDC<Code> getRDC() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBase() {
        return base;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Code getItem() {
        return getCode();
    }

    /**
     * Gets the code to execute.
     * @return the code to execute
     */
    public Code getCode() {
        return code;
    }

    /**
     * Sets the code to execute.
     * @param aCode the new code to execute
     */
    public void setCode(
        final Code aCode) {
        code = aCode;
    }

    /**
     * Gets the ic.
     * @return the ic
     */
    public RepeatedIC<Code> getIc() {
        return ic;
    }

    /**
     * Sets the ic.
     * @param aIc the new ic
     */
    public void setIc(
        final RepeatedIC<Code> aIc) {
        ic = aIc;
    }

    /**
     * Sets the path to the repeated element.
     * @param aBase the new path to the repeated element
     */
    public void setBase(
        final String aBase) {
        base = aBase;
    }

}
