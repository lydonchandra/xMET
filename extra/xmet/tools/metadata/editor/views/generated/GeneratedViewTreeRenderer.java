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
package xmet.tools.metadata.editor.views.generated;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * The renderer element for this view.
 * @author Nahid Akbar
 */
@SuppressWarnings("serial")
public class GeneratedViewTreeRenderer
    extends DefaultTreeCellRenderer {

    /** The view. */
    // private final GeneratedView view;

    /**
     * Instantiates a new generated view tree renderer.
     * @param aView the view
     */
    public GeneratedViewTreeRenderer(
        final GeneratedView aView) {
        // this.view = aView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(
        final JTree tree,
        final Object value,
        final boolean sel,
        final boolean expanded,
        final boolean leaf,
        final int row,
        final boolean hasFocus) {

        final Component ret = super.getTreeCellRendererComponent(
            tree,
            value,
            sel,
            expanded,
            leaf,
            row,
            hasFocus);
        //
        /* if (value instanceof Optional) { */
        /* Optional mee = ((Optional) value); */
        /* setText("[" + getName(mee) + "]"); */
        //
        /* if (mee.isPresent()) { */
        /* if (mee.getSetTerm().asElementDeclaration().hasSubtitutables()) { */
        /* setText(getText() + "+"); */
        // }
        // }
        // } else if (value instanceof Repeated) {
        /* Entity mee = ((Entity) value); */
        /* setText(getName(mee) + "*"); */
        // } else if (value instanceof ElementDeclaration) {
        /* ElementDeclaration edn = (ElementDeclaration) value; */
        // /*System.out.println(((ElementDeclaration) value).getName()); */
        /* if (edn.hasSubtitutables()) { */
        /* setText(getName(edn) + "+"); */
        // } else {
        /* setText(getName(edn)); */
        // }
        // } else if (value instanceof Entity) {
        /* Entity mee = ((Entity) value); */
        /* setText(getName(mee)); */
        // } else {
        /* Reporting.unimplemented(); */
        // }

        return ret;
    }
}
