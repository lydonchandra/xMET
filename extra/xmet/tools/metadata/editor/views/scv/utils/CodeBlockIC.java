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

import xmet.tools.metadata.editor.views.scv.impl.CodeBlock;
import xmet.tools.metadata.editor.views.scv.impl.ModelItem;
import xmet.tools.metadata.editor.views.scv.impl.ParentItem;
import xmet.tools.metadata.editor.views.scv.impl.Sheet;

/**
 * IC for CodeBlock.
 * @author Nahid Akbar
 */
public class CodeBlockIC
    extends InitializationContext {

    /** The cb. */
    private final CodeBlock cb;

    /**
     * Instantiates a new code block ic.
     * @param sheet the sheet
     * @param parent the parent
     * @param aCb the cb
     */
    public CodeBlockIC(
        final Sheet sheet,
        final ParentItem parent,
        final CodeBlock aCb) {
        super(sheet, parent);
        this.cb = aCb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelItem getItem() {
        return cb;
    }

}
