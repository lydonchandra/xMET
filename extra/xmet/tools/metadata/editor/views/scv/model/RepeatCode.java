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

import n.io.CS;
import n.io.CSC;

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

    /**
     * Gets the base.
     * @return the base
     */
    @Override
    public String getBase() {
        return base;
    }

    /**
     * Sets the base.
     * @param aBase the new base
     */
    public void setBase(
        final String aBase) {
        this.base = aBase;
    }

    /**
     * Gets the code.
     * @return the code
     */
    public Code getCode() {
        return code;
    }

    /**
     * Sets the code.
     * @param aCode the new code
     */
    public void setCode(
        final Code aCode) {
        this.code = aCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        visitor.preVisitRepeatCode(this);

        if (code != null) {
            code.accept(visitor);
        }

        visitor.postVisitRepeatCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return "";
    }
}
