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

import java.util.ArrayList;
import java.util.List;

import n.io.CSC;
import n.io.CSL;
import xmet.tools.metadata.editor.views.scv.utils.InitializationContext;

/**
 * Represents an block of code items. Use this to have multiple code children
 * items where only one is allowed
 * @author Nahid Akbar
 */
@CSC("codeBlock")
public class CodeBlock
    extends Code {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The sub code items. */
    @CSL(listMode = CSL.LIST_LINIENT_MODE)
    @CSC
    private List<Code> code = new ArrayList<Code>();

    /** Initilization context. */
    private transient InitializationContext ic;

    /**
     * Gets the sub code items.
     * @return the sub code items
     */
    public List<Code> getCode() {
        return code;
    }

    /**
     * Sets the sub code items.
     * @param aCode the new sub code items
     */
    public void setCode(
        final List<Code> aCode) {
        code = aCode;
    }

    /**
     * Gets the initilization context.
     * @return the initilization context
     */
    public InitializationContext getIc() {
        return ic;
    }

    /**
     * Sets the initilization context.
     * @param aIc the new initilization context
     */
    public void setIc(
        final InitializationContext aIc) {
        ic = aIc;
    }

}
