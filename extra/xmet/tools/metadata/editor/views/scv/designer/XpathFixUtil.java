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

import xmet.tools.metadata.editor.views.scv.model.ChoiceItem;
import xmet.tools.metadata.editor.views.scv.model.Choices;
import xmet.tools.metadata.editor.views.scv.model.CompositeItem;
import xmet.tools.metadata.editor.views.scv.model.DefaultModelPathVisitor;
import xmet.tools.metadata.editor.views.scv.model.IfCode;
import xmet.tools.metadata.editor.views.scv.model.Item;
import xmet.tools.metadata.editor.views.scv.model.RepeatedGroup;
import xmet.tools.metadata.editor.views.scv.model.RepeatedPage;
import xmet.tools.metadata.editor.views.scv.model.SetPathValueCode;
import xmet.tools.metadata.editor.views.scv.model.Sheet;

/**
 * Utility for fixing xpath references and stuff in a sheet model tree. Useful
 * design time.
 * @author Nahid Akbar
 */
public class XpathFixUtil
    extends DefaultModelPathVisitor {

    /**
     * Instantiates a new xpath fix util.
     * @param sheet the sheet
     */
    public XpathFixUtil(
        final Sheet sheet) {
        sheet.accept(this);
    }

    /**
     * Fix xpath.
     * @param xpath the xpath
     * @return the string
     */
    private String fixXpath(
        final String xpath) {
        String varXpath = xpath;
        if ((peekLastXpath() != null)
            && (varXpath != null)
            && (varXpath.startsWith("$"))) {
            if (varXpath.startsWith(peekLastXpath())) {
                varXpath = "$"
                    + varXpath.substring(peekLastXpath().length());
            }
        }
        return varXpath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitChoiceItem(
        final ChoiceItem aChoice,
        final Choices aChoices,
        final int aIndex) {
        super.preVisitChoiceItem(
            aChoice,
            aChoices,
            aIndex);
        aChoice.setTestXpath(fixXpath(aChoice.getTestXpath()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedPage(
        final RepeatedPage repeated) {
        repeated.setBase(fixXpath(repeated.getBase()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitRepeatedGroup(
        final RepeatedGroup repeated) {
        repeated.setBase(fixXpath(repeated.getBase()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitItem(
        final Item item) {
        item.setXpath(fixXpath(item.getXpath()));
        super.postVisitItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitSetPathValueCode(
        final SetPathValueCode setPathValue) {
        setPathValue.setDest(fixXpath(setPathValue.getDest()));
        super.visitSetPathValueCode(setPathValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postVisitCompositeItem(
        final CompositeItem item) {
        if ((item.getBase() != null)
            && (item.getRelative() != null)) {
            if (item.getRelative().startsWith(
                item.getBase())) {
                item.setRelative(item.getRelative().substring(
                    item.getBase().length()));
            }
        }
        item.setBase(fixXpath(item.getBase()));
        /* if ((item.relative != null) && item.relative.startsWith("$/")) { */
        /* item.relative = item.relative.substring(2); */
        // }
        super.postVisitCompositeItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preVisitIfCode(
        final IfCode ifcode) {
        ifcode.setBase(fixXpath(ifcode.getBase()));
    }

}
