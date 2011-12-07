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
 * repeated version of Page.
 * @author Nahid Akbar
 */
@CSC("repeatedPage")
public class RepeatedPage
    extends Page
    implements
    RepeatedItem<Page> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /** The base xpath. */
    @CS
    private String base;

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
     * {@inheritDoc}
     */
    @Override
    public void accept(
        final ModelVisitor visitor) {
        visitor.preVisitRepeatedPage(this);
        if (this.getOnInitialize() != null) {
            visitor.preVisitCode(
                this,
                "onInitialize");
            getOnInitialize().accept(
                visitor);
            visitor.postVisitCode(
                this,
                "onInitialize");
        }
        if (this.getOnValidation() != null) {
            visitor.preVisitCode(
                this,
                "onValidation");
            getOnValidation().accept(
                visitor);
            visitor.postVisitCode(
                this,
                "onValidation");
        }
        for (final PageSubitem iog : this.getItems()) {
            if (iog != null) {
                iog.accept(visitor);
            }
        }
        visitor.postVisitRepeatedPage(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return getTitle();
    }

}
